package com.udacity.ui

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.udacity.ButtonState
import com.udacity.R
import com.udacity.sendNotification
import com.udacity.utils.constants
import com.udacity.utils.constants.CHANNEL_ID
import com.udacity.utils.constants.CHANNEL_NAME
import com.udacity.utils.constants.GLIDE_URL
import com.udacity.utils.constants.LOAD_APP_URL
import com.udacity.utils.constants.RETROFIT_URL
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0
    private var url = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        //register receiver with Intent Filter includes an action of download Complete
        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        //create channel
        createChannel()
        //add selected url listener in the radio group
        selectedUrl()
        custom_button.setOnClickListener {
            custom_button.handleClick()
            if (url != "") {
                download(url)
                Toast.makeText(this, url, Toast.LENGTH_SHORT).show()
            } else
                Toast.makeText(this, getString(R.string.alert), Toast.LENGTH_SHORT).show()
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            //Get file name by the url
            val fileName = getFileName(url)
            //Change statue of button state to Completed
            custom_button.buttonState = ButtonState.Completed
            //Declare Notification manager
            val notificationManager = ContextCompat.getSystemService(
                context!!,
                NotificationManager::class.java
            ) as NotificationManager
            //Declare Download Manager
            val downloadManager = context.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            // Get cursor data by the download id
            val cursor = downloadManager.query(id?.let { DownloadManager.Query().setFilterById(it) })
            //check if cursor contains data
            if (cursor.moveToFirst()) {
                //Get download statue from this cursor
                val downloadStatue = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                if (downloadStatue == DownloadManager.STATUS_SUCCESSFUL) {
                    // if success send notification with file name and statue of Success
                    notificationManager.sendNotification(fileName, constants.SUCCESS, context)
                } else {
                    // if fail send notification with file name and statue of Fail
                    notificationManager.sendNotification(fileName, constants.FAIL, context)
                }
            }
        }
    }

    /**
     * Download File from internet with specific url
     */
    private fun download(url: String) {
        val request =
            DownloadManager.Request(Uri.parse(url))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }

    /**
     * Create channel for android O and above
     */
    fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
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

    /**
     * This function let us notify which radio button is selected
     * and then set url depend on it
     */
    fun selectedUrl() {
        radioGroup.setOnCheckedChangeListener { _, id ->
            url = when (id) {
                R.id.glideRadioButton -> GLIDE_URL
                R.id.loadAppRadioButton -> LOAD_APP_URL
                R.id.retrofitRadioButton -> RETROFIT_URL
                else -> ""
            }
        }
    }

    /**
     * This function get file name by pass the url
     */
    fun getFileName(url: String): String = when (url) {
        GLIDE_URL -> getString(R.string.glid)
        LOAD_APP_URL -> getString(R.string.loadapp)
        else -> getString(R.string.retrofit)
    }


    override fun onPause() {
        super.onPause()
        finish()
    }
}

