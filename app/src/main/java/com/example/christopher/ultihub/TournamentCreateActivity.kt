package com.example.christopher.ultihub

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.create_tournament.*

class TournamentCreateActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_tournament)

        button.setOnClickListener{
            val tourney = Tournament(tournamentNameInput.text.toString(), tournamentLocationInput.text.toString())
            startActivity(Intent(this, TeamDetailActivity::class.java))
        }
    }
}