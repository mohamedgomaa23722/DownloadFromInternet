package com.udacity

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.provider.Telephony.Mms.Part.FILENAME
import androidx.core.app.NotificationCompat
import com.udacity.ui.DetailActivity
import com.udacity.ui.MainActivity
import com.udacity.utils.constants
import com.udacity.utils.constants.CHANNEL_ID
import com.udacity.utils.constants.FILENAME
import com.udacity.utils.constants.NOTIFICATION_ID
import com.udacity.utils.constants.STATUE


fun NotificationManager.sendNotification(
    fileName: String,
    statue: String,
    applicationContext: Context
) {
    // Create the content intent for the notification, which launches
    // this activity
    // TODO: Step 1.11 create intent
    val contentIntent = Intent(applicationContext, MainActivity::class.java)
    // TODO: Step 1.12 create PendingIntent
    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    // TODO: Step 2.0 add style
    val eggImage = BitmapFactory.decodeResource(
        applicationContext.resources,
        R.drawable.ic_assistant_black_24dp
    )


    // TODO: Step 2.2 add snooze action
    val changedIntent = Intent(applicationContext, DetailActivity::class.java).let {
        it.putExtra(constants.FILENAME, fileName)
        it.putExtra(STATUE, statue)
    }
    // TODO: Step 1.12 create PendingIntent
    val ChangedPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        changedIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    // TODO: Step 1.2 get an instance of NotificationCompat.Builder
    // Build the notification
    val builder = NotificationCompat.Builder(
        applicationContext,
        CHANNEL_ID
    )

        .setSmallIcon(R.drawable.ic_assistant_black_24dp)
        .setContentTitle(
            applicationContext
                .getString(R.string.notification_title)
        )
        .setContentText(applicationContext.getString(R.string.notification_description))

        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)

        .addAction(
            R.drawable.abc_vector_test,
            applicationContext.getString(R.string.notification_button),
            ChangedPendingIntent
        )


        // TODO: Step 2.5 set priority
        .setPriority(NotificationCompat.PRIORITY_HIGH)
    // TODO: Step 1.4 call notify
    notify(NOTIFICATION_ID, builder.build())
}