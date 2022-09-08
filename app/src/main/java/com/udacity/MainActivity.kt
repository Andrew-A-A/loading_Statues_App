package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.io.File


class MainActivity : AppCompatActivity() {
    private lateinit var radioGroup: RadioGroup
    private var downloadID: Long = 0
    private lateinit var notificationManager: NotificationManager
//    private lateinit var pendingIntent: PendingIntent
//    private lateinit var action: NotificationCompat.Action

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        radioGroup=findViewById(R.id.radioGroup)
        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        notificationManager= ContextCompat.getSystemService(
            applicationContext,
            NotificationManager::class.java
        )as NotificationManager
        //Create Notification Channel
        createChannel()

        custom_button.setOnClickListener {

            val name : String
            if (radioGroup.checkedRadioButtonId==R.id.glide)
                name=applicationContext.getString(R.string.glide_image_loading_library_by_bumptech)
            else if(radioGroup.checkedRadioButtonId==R.id.loadApp)
                name=applicationContext.getString(R.string.loadapp_current_repository_by_udacity)
            else
                name=applicationContext.getString(R.string.retrofit_type_safe_http_client_for_android_and_java_by_square_inc)

            if (radioGroup.checkedRadioButtonId==-1)
                Toast.makeText( applicationContext,"Please select the file to download", Toast.LENGTH_SHORT).show()
            else
            download(name)
        }
    }

    private fun createChannel() {

        val channelId= applicationContext.getString(R.string.notification_channel_id)
        val channelName=applicationContext.getString(R.string.notification_channel_name)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
                .apply {
                    setShowBadge(false)
                }

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = getString(R.string.notification_description)

            val notificationManager = this.getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)

        }

    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
        }
    }

    private fun download(name: String) {

        URL =if(radioGroup.checkedRadioButtonId==R.id.loadApp)
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        else if(radioGroup.checkedRadioButtonId==R.id.glide)
            "https://github.com/bumptech/glide"
        else "https://github.com/square/retrofit"

        Log.i("text",URL)
        val request =
            DownloadManager.Request(Uri.parse(URL))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
        val filter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        val downloadReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                if (downloadId == -1L) return

                // query download status
                val cursor: Cursor =
                    downloadManager.query(DownloadManager.Query().setFilterById(downloadId))
                if (cursor.moveToFirst()) {
                    val status: Int =
                        cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                    if (status == DownloadManager.STATUS_SUCCESSFUL) {
                        // download is successful
                        notificationManager.sendNotification(
                            applicationContext.getString(R.string.notification_description),applicationContext,name
                            ,true
                        )
                        val uri: String =
                            cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI))
                        val file = File(Uri.parse(uri).path)
                    } else {
                        // download is cancelled
                        notificationManager.sendNotification(
                            applicationContext.getString(R.string.notification_description),applicationContext,name
                            ,false
                        )

                    }
                } else {
                    // download is cancelled
                    notificationManager.sendNotification(
                        applicationContext.getString(R.string.notification_description),applicationContext,name
                        ,false
                    )
                }
            }
        }

        registerReceiver(downloadReceiver, filter)
    }

    companion object {

        private var URL = "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val CHANNEL_ID = "channelId"
    }

}
