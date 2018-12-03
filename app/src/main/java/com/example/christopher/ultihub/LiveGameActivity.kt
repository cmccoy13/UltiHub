package com.example.christopher.ultihub

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.SystemClock
import android.support.v4.app.NavUtils
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import com.google.firebase.database.*
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

    lateinit var teamName : String
    lateinit var tournamentName : String

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.live_game)

        teamName = intent.getStringExtra("teamName")
        tournamentName = intent.getStringExtra("tournamentName")
        val opponent = intent.getStringExtra("opponent")

        val database = Utils.database
        val team = database.getReference("users").child(Utils.userID).child("teams").child(teamName)
        val tournament = team.child("tournaments").child(tournamentName)
        val gameRef = tournament.child("games").child(opponent)

        gameRef.child("title").setValue("vs. $opponent")

        val maxScore : Int
        val startDT : String
        val softCap : Int
        val hardCap : Int
        var ourTOsFirstHalf : Int
        var ourTOsFSecondHalf : Int
        var ourTOsFloater : Int
        var oppTOsFirstHalf : Int
        var oppTOsFSecondHalf : Int
        var oppTOsFloater : Int
        var onOffense : Boolean
        var ourScore : Int
        var oppScore : Int


        gameRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val game = dataSnapshot.getValue(GameResponse::class.java)
                begin(game!!)
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(HomeActivity.TAG, "Failed to read value.", error.toException())
            }
        })
    }

    fun begin(game : GameResponse) {
        //val halftime = maxScore/2

        val currentMS = Calendar.getInstance().timeInMillis
        val startTime = SimpleDateFormat("MM-dd-yyyy HH:mm:ss").parse(game.startDate)
        val startMS = startTime.time

        val timeUntilStart = startMS-currentMS

        object : CountDownTimer((timeUntilStart + game.hardCap*60*1000), 1000) {

            override fun onTick(millisUntilFinished: Long) {
                val minutesUntilFinished = ((millisUntilFinished / 1000)/60)
                val secondsUntilFinished = (millisUntilFinished / 1000)%60

                if(millisUntilFinished >= game.hardCap*1000*60) { //if game has not yet started
                    val minutesUntilStart = ((millisUntilFinished - game.hardCap*1000*60)/1000)/60
                    val secondsUntilStart = ((millisUntilFinished - game.hardCap*1000%60)/1000)%60

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
        opponentTeamText.text = game.opponent
    }

    /*override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item!!.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return false
    }*/
}
