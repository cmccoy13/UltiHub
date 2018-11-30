package com.example.christopher.ultihub

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.SystemClock
import android.support.v4.app.NavUtils
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.create_game.*
import kotlinx.android.synthetic.main.create_team.*
import kotlinx.android.synthetic.main.create_tournament.*
import kotlinx.android.synthetic.main.live_game.*
import kotlinx.android.synthetic.main.team_detail.*
import java.sql.Time
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.*
import kotlin.math.floor

class LiveGameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.live_game)

        val teamName = intent.getStringExtra("teamName")
        val tournamentName = intent.getStringExtra("tournamentName")
        val opponent = intent.getStringExtra("opponent")
        val maxScore = intent.getStringExtra("maxScore")
        val startHour = intent.getIntExtra("startHour")
        val startMinute = intent.getIntExtra("startMinute")
        val softCap = intent.getStringExtra("softCap")
        val hardCap = intent.getStringExtra("hardCap")
        val tosPerHalf = intent.getStringExtra("tosPerHalf")
        val tosFloater = intent.getStringExtra("tosFloater")
        val offense = intent.getBooleanExtra("offense", true)

        var ourScoreCurrent = 0
        var opponentScoreCurrent = 0
        val halftime = maxScore.toInt()/2

        val simpleTimeFormat = SimpleDateFormat("HH:mm:ss")
        val currentMS = Calendar.getInstance().timeInMillis
        var startTime = Calendar.getInstance()
        startTime.set(Calendar.HOUR_OF_DAY, startHour)
        startTime.set(Calendar.MINUTE, startMinute)
        val startMS = startTime.timeInMillis

        val timeUntilStart = startMS-currentMS

        object : CountDownTimer((timeUntilStart + hardCap.toInt()*60*1000), 1000) {

            override fun onTick(millisUntilFinished: Long) {
                gameClockText.setText("" + ((millisUntilFinished/60)%60) + ":" + (millisUntilFinished / 1000)%60)
            }

            override fun onFinish() {
                gameClockText.setText("Hard Cap Reached")
            }
        }.start()

        ourTeamText.text = teamName
        opponentTeamText.text = opponent
    }
}

private fun Intent.getIntExtra(s: String): Int {
    return s.toInt()
}
