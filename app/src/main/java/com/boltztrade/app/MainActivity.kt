package com.boltztrade.app

import android.os.Bundle
import android.provider.Settings
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import com.boltztrade.app.apis.BoltztradeRetrofit
import com.boltztrade.app.model.FcmService
import com.boltztrade.app.model.Strategies
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers



class MainActivity : AppCompatActivity() {

    private val LOG_TAG = MainActivity::class.java.canonicalName

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var userNameTextView: TextView
    private lateinit var emailTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        addFcmToken()

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        userNameTextView =  navView.getHeaderView(0).findViewById<TextView>(R.id.username)
        emailTextView = navView.getHeaderView(0).findViewById<TextView>(R.id.user_email_id)

        userNameTextView.text =  BoltztradeSingleton.mSharedPreferences.getString(
            SharedPrefKeys.fullnameFromGmail,"")

        emailTextView.text =  BoltztradeSingleton.mSharedPreferences.getString(
            SharedPrefKeys.boltztradeUser,"")
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,R.id.nav_articles,R.id.nav_billings,R.id.nav_watchlist,R.id.nav_news,
                R.id.nav_strategies,R.id.nav_profile,R.id.nav_help
            ), drawerLayout
        )


        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    fun addFcmToken(){
        val username = BoltztradeSingleton.mSharedPreferences.getString(SharedPrefKeys.boltztradeUser,"")
        val fcmToken = BoltztradeSingleton.mSharedPreferences.getString(SharedPrefKeys.FIREBASE_TOKEN_KEY,"")
        val device = Settings.Secure.getString(this.applicationContext.contentResolver, Settings.Secure.ANDROID_ID)

        val disp = BoltztradeRetrofit.getInstance().addFcmToken("Bearer ${BoltztradeSingleton.mSharedPreferences.getString(
            SharedPrefKeys.boltztradeToken,"")!!}", FcmService.AddFCMToken(username!!,fcmToken!!,"android_$device")).
            subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
            Log.d(LOG_TAG,it.toString())
        },{
            Log.e(LOG_TAG,"unable to add firebase token")
            it.printStackTrace()
        },{
            Log.i(LOG_TAG,"Get All Strategies Call Completed..")
        })
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val item = menu.findItem(R.id.action_settings)
        item.isVisible = false
        super.onPrepareOptionsMenu(menu)
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
