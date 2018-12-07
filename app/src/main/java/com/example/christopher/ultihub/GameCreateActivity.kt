package com.example.christopher.ultihub

import android.app.DatePickerDialog
import android.app.TimePickerDialog
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
import java.text.SimpleDateFormat
import java.time.Month
import java.util.*
import javax.xml.datatype.DatatypeConstants.MONTHS

class GameCreateActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_game)

        val teamName = intent.getStringExtra("teamName")
        val tournamentName = intent.getStringExtra("tournamentName")

        gameDateButton.setOnClickListener{
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                // Display Selected date in textbox
                val updatedMonth = (monthOfYear.toInt()+1).toString()
                selectedDate.setText("" + updatedMonth + "-" + dayOfMonth + "-" + year)
            }, year, month, day)
            dpd.show()
        }

        gameTimeButton.setOnClickListener{
            val c = Calendar.getInstance()
            val hour = c.get(Calendar.HOUR_OF_DAY)
            val minute = c.get(Calendar.MINUTE)

            val timeSetListener = TimePickerDialog.OnTimeSetListener { view, hour, minute ->
                c.set(Calendar.HOUR_OF_DAY, hour)
                c.set(Calendar.MINUTE, minute)
                c.set(Calendar.SECOND, 0)
                selectedTime.setText(SimpleDateFormat("HH:mm:ss").format(c.time))
            }
            TimePickerDialog(this, timeSetListener, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show()
        }

        startGameButton.setOnClickListener{
            val opponent = opponentNameInput.text.toString()
            val maxScore = maxScoreInput.text.toString()
            val softCap = softCapInput.text.toString()
            val hardCap = hardCapInput.text.toString()
            val tosPerHalf = tosPerHalfInput.text.toString()
            val tosFloater = tosFloaterInput.text.toString()
            val startDate = selectedDate.text.toString()
            val startTime = selectedTime.text.toString()

            if(opponent != "" && maxScore != "" && startDate != "" && startTime != ""
                && softCap != "" && hardCap != "" && tosPerHalf != "" && tosFloater != "") {

                val date = "" + selectedDate.text.toString() + " " + selectedTime.text.toString()

                val database = Utils.database
                val team = database.getReference("users").child(Utils.userID).child("teams").child(teamName)
                val tournament = team.child("tournaments").child(tournamentName)
                val gamesRef = tournament.child("games")
                val thisGame = gamesRef.child("$opponent")
                //thisGame.child("title").setValue("vs. $opponent")
                thisGame.child("opponent").setValue(opponent)
                thisGame.child("maxScore").setValue(maxScore.toInt())
                thisGame.child("ourScore").setValue(0)
                thisGame.child("oppScore").setValue(0)
                thisGame.child("softCap").setValue(softCap.toInt())
                thisGame.child("hardCap").setValue(hardCap.toInt())
                thisGame.child("ourTOsFirstHalf").setValue(tosPerHalf.toInt())
                thisGame.child("ourTOsSecondHalf").setValue(tosPerHalf.toInt())
                thisGame.child("ourTOsFloater").setValue(tosFloater.toInt())
                thisGame.child("oppTOsFirstHalf").setValue(tosPerHalf.toInt())
                thisGame.child("oppTOsSecondHalf").setValue(tosPerHalf.toInt())
                thisGame.child("oppTOsFloater").setValue(tosFloater.toInt())
                thisGame.child("startDate").setValue(date)
                thisGame.child("onOffense").setValue(offenseButton.isChecked)
                thisGame.child("startingOnOffense").setValue(offenseButton.isChecked)
                thisGame.child("gameFinished").setValue(false)

                val intent = Intent(this, LiveGameActivity::class.java).apply {
                    putExtra("teamName", teamName)
                    putExtra("tournamentName", tournamentName)
                    putExtra("opponent", opponent)
                }

                startActivity(intent)
            }
        }
    }
}