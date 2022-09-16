package com.udacity.ui

import android.app.NotificationManager
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import com.udacity.R
import com.udacity.utils.constants.FILENAME
import com.udacity.utils.constants.NOTIFICATION_ID
import com.udacity.utils.constants.STATUE
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)
        // First Cancel notification
        val notificationManager = this.getSystemService(NotificationManager::class.java)
        notificationManager.cancel(NOTIFICATION_ID)
        //Second get download info which sends via pending intent
        val fileName = intent.getStringExtra(FILENAME)
        val statue = intent.getStringExtra(STATUE)
        //Third set file name and statue text
        fileName_txt.text = fileName
        statue_txt.text = statue
        //Handle onclick listener
        submit.setOnClickListener{
            submit.handleClick()
            GoToMainActivity()
        }


    }

    /**
     * Handle On back pressed button and go to main activity when
     * clicked on buttin
     */
    override fun onBackPressed() {
        super.onBackPressed()
        GoToMainActivity()
    }

    /**
     * Handle get back to main activity and finish details activity
     */
    fun GoToMainActivity(){
        val intent = Intent(applicationContext,MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}
