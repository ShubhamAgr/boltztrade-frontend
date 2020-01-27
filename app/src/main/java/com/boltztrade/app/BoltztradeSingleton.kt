package com.boltztrade.app

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.provider.Settings
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId

class BoltztradeSingleton {

    companion object {
        lateinit var mSharedPreferences: SharedPreferences
        lateinit var mSharedPreferencesEditor: SharedPreferences.Editor

        fun initializeSharedPreferences(context: Context) {
            mSharedPreferences = context.getSharedPreferences("boltztradePrefs", Context.MODE_PRIVATE)
            mSharedPreferencesEditor = mSharedPreferences.edit()
        }

        fun initializeFirebase(){
            FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        return@OnCompleteListener
                    }
                    val token = task.result?.token
                    Log.e("firebase_token","$token")
                    mSharedPreferencesEditor.putString(SharedPrefKeys.FIREBASE_TOKEN_KEY, token).apply()
                })
        }

        fun GetDeviceID(context: Context):String{
            return try{
                val android_ID = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
                Log.d("android_id::",android_ID)
                "${Build.MODEL}_$android_ID"
            }catch (e:Exception){
                e.printStackTrace()
                Build.MODEL
            }
        }

    }
}