package com.example.christopher.ultihub

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import kotlinx.android.synthetic.main.create_tournament.*

class TournamentCreateActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_tournament)

        val teamName = intent.getStringExtra("teamName")

        button.setOnClickListener{
            val name = tournamentNameInput.text.toString()
            val location = tournamentLocationInput.text.toString()

            if(name != "" && location != "")
            {
                val database = Utils.database
                val userRef = database.getReference("users").child(Utils.userID)
                val teamRef = userRef.child("teams").child(teamName)
                val tournament = teamRef.child("tournaments").child(name)
                tournament.child("name").setValue(name)
                tournament.child("location").setValue(location)

                val tourney = Tournament(tournamentNameInput.text.toString(), tournamentLocationInput.text.toString())

                startActivity(Intent(this, TeamDetailActivity::class.java).putExtra("Name", teamName))
                //finish()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item!!.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return false
    }
}