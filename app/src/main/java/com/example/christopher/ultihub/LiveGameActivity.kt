package com.example.christopher.ultihub

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.SystemClock
import android.support.v4.app.NavUtils
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
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
        val gameName = intent.getStringExtra("gameName")

        val database = Utils.database
        val team = database.getReference("users").child(Utils.userID).child("teams").child(teamName)
        val tournament = team.child("tournaments").child(tournamentName)
        val game = tournament.child("games").child(gameName)
        

        //TODO: REMOVE THESE AND GET FROM DB INSTEAD!!

        val opponent = intent.getStringExtra("opponent")
        val maxScore = intent.getStringExtra("maxScore")
        val startDT = intent.getStringExtra("startDT")
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
        val startTime = SimpleDateFormat("MM-dd-yyyy HH:mm:ss").parse(startDT)
        val startMS = startTime.time

        val timeUntilStart = startMS-currentMS

        object : CountDownTimer((timeUntilStart + hardCap.toInt()*60*1000), 1000) {

            override fun onTick(millisUntilFinished: Long) {
                val minutesUntilFinished = ((millisUntilFinished / 1000)/60)
                val secondsUntilFinished = (millisUntilFinished / 1000)%60

                if(millisUntilFinished >= hardCap.toInt()*1000*60) { //if game has not yet started
                    val minutesUntilStart = ((millisUntilFinished - hardCap.toInt()*1000*60)/1000)/60
                    val secondsUntilStart = ((millisUntilFinished - hardCap.toInt()*1000%60)/1000)%60

                    if(minutesUntilStart.toInt() == 0 && secondsUntilStart.toInt() == 0){
                        gameClockText.setText("Game Start")
                    }
                    else {
                        if(secondsUntilStart < 10){
                            gameClockText.setText("Game beginning in $minutesUntilStart:0$secondsUntilStart")
                        }
                        else {
                            gameClockText.setText("Game beginning in $minutesUntilStart:$secondsUntilStart")
                        }
                    }
                }
                else
                    if(secondsUntilFinished < 10)
                        gameClockText.setText("$minutesUntilFinished:0$secondsUntilFinished")
                    else
                        gameClockText.setText("$minutesUntilFinished:$secondsUntilFinished")
            }

            override fun onFinish() {
                gameClockText.setText("Hard Cap Reached")
            }
        }.start()

        ourTeamText.text = teamName
        opponentTeamText.text = opponent
    }

    /*override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item!!.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return false
    }*/
}
