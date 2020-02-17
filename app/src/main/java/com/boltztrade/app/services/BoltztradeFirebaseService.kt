package com.boltztrade.app.services

import android.util.Log
import com.boltztrade.app.utils.GenerateBoltztradeNotification
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class BoltztradeFirebaseService: FirebaseMessagingService(){
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d("Firebase message","${message.data}")
        if(message.notification != null){
            GenerateBoltztradeNotification.generateNotification(applicationContext,message?.notification?.body ?:"",
                message?.notification?.title ?:"")
        }else{
            GenerateBoltztradeNotification.generateNotification(applicationContext,message.data.toString(),"something else")
        }

    }

    override fun onNewToken(token: String) {
        Log.d("New Token",token)
    }
}