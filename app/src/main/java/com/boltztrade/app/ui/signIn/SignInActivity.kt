package com.boltztrade.app.ui.signIn

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.boltztrade.app.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import android.content.Intent
import android.util.Log
import android.view.Window
import android.view.WindowManager
import com.boltztrade.app.BoltztradeSingleton
import com.boltztrade.app.MainActivity
import com.boltztrade.app.SharedPrefKeys
import com.boltztrade.app.apis.BoltztradeRetrofit
import com.boltztrade.app.model.Tokens
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.Scope
import com.google.firebase.messaging.FirebaseMessaging
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class SignInActivity : AppCompatActivity() {

    private val LOG_TAG = SignInActivity::class.java.canonicalName
    private val RC_SIGN_IN = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        supportActionBar?.hide()
        BoltztradeSingleton.initializeSharedPreferences(this)
        BoltztradeSingleton.initializeFirebase()
        subscribeToTopic()
        val signInButton = findViewById<SignInButton>(R.id.sign_in_button)
        signInButton.setSize(SignInButton.SIZE_STANDARD)
        signInButton.setOnClickListener { signIn() }
    }

    private fun signIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestScopes(Scope(Scopes.DRIVE_APPFOLDER))
            .requestIdToken("910071125271-6t5gknh7jpseg5n7tdvlgbdpbnoao35i.apps.googleusercontent.com")
            .requestServerAuthCode("910071125271-6t5gknh7jpseg5n7tdvlgbdpbnoao35i.apps.googleusercontent.com")
            .requestEmail()
            .build()
        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
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
            Log.d(LOG_TAG,"Login Successfull ... ${account?.idToken}")
           val disposible =  BoltztradeRetrofit.getInstance().loginWithGmail(Tokens("${account?.idToken}"))
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
                   Log.d("token",it.token)
                   BoltztradeSingleton.mSharedPreferencesEditor.putString(SharedPrefKeys.boltztradeToken,it.token).apply()
                   BoltztradeSingleton.mSharedPreferencesEditor.putString(SharedPrefKeys.boltztradeUser,it.username).apply()
                   startActivity(Intent(this,MainActivity::class.java))
                },{
                    it.printStackTrace()
                },{
                    Log.d(LOG_TAG,"google login api call completed")
                })
        } catch (e: ApiException) {
            e.printStackTrace()
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
           Log.e(LOG_TAG,"login failed...")
        }

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
}
