package com.boltztrade.app.ui.signIn

import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.os.Bundle
import android.text.format.Formatter
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.boltztrade.app.BoltztradeSingleton
import com.boltztrade.app.MainActivity
import com.boltztrade.app.R
import com.boltztrade.app.SharedPrefKeys
import com.boltztrade.app.apis.BoltztradeRetrofit
import com.boltztrade.app.apis.KiteRetrofit
import com.boltztrade.app.model.Instrument
import com.boltztrade.app.model.Tokens
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.*
import java.util.*


class SignInActivity : AppCompatActivity() {

    private val LOG_TAG = SignInActivity::class.java.canonicalName
    private val RC_SIGN_IN = 1
    private lateinit var signInButton:Button
    private val c = Calendar.getInstance()!!
    private val mYear = c.get(Calendar.YEAR)
    private val mMonth = c.get(Calendar.MONTH)
    private val mDay = c.get(Calendar.DAY_OF_MONTH)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        supportActionBar?.hide()
        signInButton   = findViewById(R.id.sign_in_button)
        signInButton.visibility = View.GONE
        subscribeToTopic()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("910071125271-6t5gknh7jpseg5n7tdvlgbdpbnoao35i.apps.googleusercontent.com")
            .requestServerAuthCode("910071125271-6t5gknh7jpseg5n7tdvlgbdpbnoao35i.apps.googleusercontent.com")
            .requestEmail()
            .build()
        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
       val instrumentFetchedOn =  BoltztradeSingleton.mSharedPreferences.getString("instrumentFetchedOn","")
        if(!instrumentFetchedOn.equals("$mYear-$mMonth-$mDay")){
            tryToDownloadCSV()
        }

        checkIfUserAlreadySignedIn(mGoogleSignInClient)
        signInButton.setOnClickListener { signIn(mGoogleSignInClient) }
    }

    private fun checkIfUserAlreadySignedIn(mGoogleSignInClient: GoogleSignInClient){
        mGoogleSignInClient.silentSignIn()
            .addOnCompleteListener(this) {
                    task -> handleSignInResult(task)
            }
    }

    private fun signIn(mGoogleSignInClient:GoogleSignInClient) {
        mGoogleSignInClient.silentSignIn()
        val signInIntent = mGoogleSignInClient.getSignInIntent()
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            // Signed in successfully, show authenticated UI.

            if(account == null) signInButton.visibility = View.VISIBLE
            Log.d(LOG_TAG,"Login Successfull ... ${account?.idToken}")
            BoltztradeSingleton.mSharedPreferencesEditor.putString(SharedPrefKeys.fullnameFromGmail,account?.displayName).apply()
            checkWithBackend("${account?.idToken}")

        } catch (e: ApiException) {
            e.printStackTrace()
            signInButton.visibility = View.VISIBLE
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
           Log.e(LOG_TAG,"login failed...")
        }

    }

    fun checkWithBackend(idToken:String){
        val disposible =  BoltztradeRetrofit.getInstance().loginWithGmail(Tokens("$idToken"))
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
                Log.d("token",it.token)
                BoltztradeSingleton.mSharedPreferencesEditor.putString(SharedPrefKeys.boltztradeToken,it.token).apply()
                BoltztradeSingleton.mSharedPreferencesEditor.putString(SharedPrefKeys.boltztradeUser,it.username).apply()
                startActivity(Intent(this,MainActivity::class.java))
//                startActivity(Intent(this,NewArticleActivity::class.java))
            },{
                signInButton.visibility = View.VISIBLE
                it.printStackTrace()

            },{
                Log.d(LOG_TAG,"google login api call completed")
            })
    }

    fun tryToDownloadCSV(){
        val disposible = KiteRetrofit.getInstance().getInstruments("X-Kite-Version: 3").
        subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
            val file = File("instrumentList")
            it.byteStream()
            val bis: InputStream = BufferedInputStream(it.byteStream(), 1024 * 8)
            var sCurrentLine: String?
            val disposible = Observable.create(ObservableOnSubscribe<Boolean> {
                try {
                    BoltztradeSingleton.mDatabase.instrumentDao().nukeTable()
                    val r = BufferedReader(InputStreamReader(bis))
//                    val total = StringBuilder()
                    var line: String?
                    Log.d(LOG_TAG,r.readLine())
                    while (r.readLine().also { line = it } != null) {
                        val values = line?.split(",")
                        if(values?.size == 12 ){
                            BoltztradeSingleton.mDatabase.instrumentDao().insertAll(
                                Instrument(
                                    values[0], values[1].toInt(), values[2],values[3], values[4].toInt(), values[5],
                                    values[6].toDouble(), values[7].toDouble(), values[8].toInt(),
                                    values[9], values[10], values[11]
                                )
                            )
                        }else{
                            Log.d("values","$line")
                        }
//                        total.append(line).append('\n')
                    }
                    it.onNext(true)
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
                BoltztradeSingleton.mSharedPreferencesEditor.putString("instrumentFetchedOn","$mYear-$mMonth-$mDay").apply()
            },{
                Log.d(LOG_TAG,"something went wrong while adding instruments..")
                it.printStackTrace()
            },{ Log.d(LOG_TAG,"onComplete")})

        },{
            it.printStackTrace()
        },{

        })
    }

    fun subscribeToTopic(){
        FirebaseMessaging.getInstance().subscribeToTopic("highScores")
            .addOnCompleteListener { task ->
                var msg = "Subscribed to topic"
                if (!task.isSuccessful) {
                    msg = "Subscription Failed"
                }
                Log.d("subscribed","$msg")
            }
    }

    fun UnSubscribeToTopic(){
        FirebaseMessaging.getInstance().unsubscribeFromTopic("weather")
            .addOnCompleteListener { task ->
                var msg = "UnSubscribed to topic"
                if (!task.isSuccessful) {
                    msg = "UnSubscription Failed"
                }

                Log.d("unsubscribed","$msg")
            }
    }


    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemUI()
    }

    private fun hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    // Shows the system bars by removing all the flags
// except for the ones that make the content appear under the system bars.
    private fun showSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }
    fun getIPAddress(){

        val wm = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val ip = Formatter.formatIpAddress(wm.connectionInfo.ipAddress)
    }
}
