package com.example.christopher.ultihub

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.Button
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.create_game.*
import kotlinx.android.synthetic.main.create_player.*
import kotlinx.android.synthetic.main.create_team.*
import kotlinx.android.synthetic.main.create_tournament.*
import kotlinx.android.synthetic.main.team_detail.*

class PlayerCreateActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_player)

        val teamName = intent.getStringExtra("teamName")
        if(intent.getStringExtra("playerName") != null)
        {
            playerNameInput.setText(intent.getStringExtra("playerName"))
            playerNumInput.setText(intent.getStringExtra("playerNum"))
            playerPosInput.setText(intent.getStringExtra("playerPos"))
            checkBox.isChecked = intent.getStringExtra("playerCaptain").toBoolean()
            createPlayerButton.setText("Update Player")
        }

       createPlayerButton.setOnClickListener{
            val name = playerNameInput.text.toString()
            val number = playerNumInput.text.toString()
            val position = playerPosInput.text.toString()
            val captain = checkBox.isChecked

            if(name != "")
            {
                val database = Utils.database
                val userRef = database.getReference("users").child(Utils.userID)
                val teamRef = userRef.child("teams").child(teamName)
                val player = teamRef.child("players").child(name)
                player.child("name").setValue(name)
                if(number != "")
                    player.child("number").setValue(number)
                if(position != "")
                    player.child("position").setValue(position)
                player.child("captain").setValue(captain)
                player.child("passes").setValue(0)
                player.child("drops").setValue(0)
                player.child("goals").setValue(0)
                player.child("assists").setValue(0)
                player.child("stalled").setValue(0)
                player.child("callahans thrown").setValue(0)
                player.child("catches").setValue(0)
                player.child("Ds").setValue(0)
                player.child("callahans").setValue(0)
                player.child("throwaways").setValue(0)
                player.child("stalls").setValue(0)

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
