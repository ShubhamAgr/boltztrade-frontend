package com.boltztrade.app.services

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class BoltztradeFirebaseService: FirebaseMessagingService(){
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d("Firebase message","${message.data}")
    }

    override fun onNewToken(token: String) {
        Log.d("New Token",token)
    }
}