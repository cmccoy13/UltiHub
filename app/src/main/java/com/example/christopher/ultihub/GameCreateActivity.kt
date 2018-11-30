package com.example.christopher.ultihub

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.create_game.*
import kotlinx.android.synthetic.main.create_team.*
import kotlinx.android.synthetic.main.create_tournament.*
import kotlinx.android.synthetic.main.team_detail.*
import android.widget.TimePicker

class GameCreateActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_game)

        val teamName = intent.getStringExtra("teamName")
        val tournamentName = intent.getStringExtra("tournamentName")

        startGameButton.setOnClickListener{
            val opponent = opponentNameInput.text.toString()
            val maxScore = maxScoreInput.text.toString()
            val softCap = softCapInput.text.toString()
            val hardCap = hardCapInput.text.toString()
            val tosPerHalf = tosPerHalfInput.text.toString()
            val tosFloater = tosFloaterInput.text.toString()
            val startHour = timePicker1.hour
            val startMinute = timePicker1.minute

            if(opponent != "" && maxScore != "" && startHour != null && startMinute != null
                && softCap != "" && hardCap != "" && tosPerHalf != "" && tosFloater != "") {

                val database = Utils.database
                val team = database.getReference("users").child(Utils.userID).child("teams").child(teamName)
                val tournament = team.child("tournaments").child(tournamentName)
                val gamesRef = tournament.child("games")
                val thisGame = gamesRef.child("$opponent")
                thisGame.child("title").setValue("vs. $opponent")
                thisGame.child("opponent").setValue(opponent)
                thisGame.child("maxScore").setValue(maxScore)

                val intent = Intent(this, LiveGameActivity::class.java).apply {
                    putExtra("teamName", teamName)
                    putExtra("tournamentName", tournamentName)
                    putExtra("opponent", opponent)
                    putExtra("maxScore", maxScore)
                    putExtra("startHour", startHour.toString())
                    putExtra("startMinute", startMinute.toString())
                    putExtra("softCap", softCap)
                    putExtra("hardCap", hardCap)
                    putExtra("tosPerHalf", tosPerHalf)
                    putExtra("tosFloater", tosFloater)
                    putExtra("offense", offenseButton.isSelected)
                }

                startActivity(intent)
            }
        }
    }
}