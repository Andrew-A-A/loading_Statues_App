package com.udacity

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat


private val NOTIFICATION_ID=0
    private val REQUEST_CODE=0
    private val FLAGS=0

    fun NotificationManager.sendNotification(messageBody : String,
                                             applicationContext: Context,
                                             fileName: String,
                                             isSuccesful: Boolean){
        //Create the Content intent for the notification, which launches this activity
        val contentIntent=Intent(applicationContext,DetailActivity::class.java)
            contentIntent.putExtra("fileName",fileName)
            contentIntent.putExtra("isSuccesful",isSuccesful)

        val contentPendingIntent = PendingIntent.getActivity(
            applicationContext,
            NOTIFICATION_ID,
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )


        //Style
        val notificationStyle= NotificationCompat.BigTextStyle()
        //Instance of Notification Builder
        val builder=NotificationCompat.Builder(
            applicationContext,
            applicationContext.getString(R.string.notification_channel_id)
        )
            //Set Title, text and icon
            .setSmallIcon(R.drawable.ic_assistant_black_24dp)
            .setContentTitle(applicationContext.getString(R.string.notification_title))
            .setContentText(applicationContext.getString(R.string.notification_description))
            //Set content Intent
            .setContentIntent(contentPendingIntent)
            .setAutoCancel(true)

            //Set Style for Builder
            .setStyle(notificationStyle)
            //add Check Download Action
            .addAction(R.drawable.ic_assistant_black_24dp,
                applicationContext.getString(R.string.check_statues)
            ,contentPendingIntent
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        notify(NOTIFICATION_ID,builder.build())

//        fun NotificationManager.cancelNotifications(){
//            canelAll()
//        }
}