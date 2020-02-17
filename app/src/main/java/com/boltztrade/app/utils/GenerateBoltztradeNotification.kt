package com.boltztrade.app.utils

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.boltztrade.app.R
import java.util.*
import kotlin.random.Random

object GenerateBoltztradeNotification {

    fun generateNotification(context: Context,message: String,descriptionText: String){

        showCradlewiseNotification(context, message,
            descriptionText,Random.nextInt(1,1000))
    }

    @SuppressLint("SimpleDateFormat")
    private fun showCradlewiseNotification(context: Context, message: String, descriptionText:String, notificationId:Int){


        val mcurrentTime = Calendar.getInstance()
        val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
        val minute = mcurrentTime.get(Calendar.MINUTE)


        val mBuilder = NotificationCompat.Builder(context, "boltztrade_channel_id")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentText(message)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(message))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "boltztrade channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("boltztrade_channel_id", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            notify(notificationId, mBuilder.build())
        }


    }
}