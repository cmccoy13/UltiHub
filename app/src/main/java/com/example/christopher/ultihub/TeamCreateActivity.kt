package com.example.christopher.ultihub

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.create_team.*

class TeamCreateActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_team)

        //val userID = intent.getStringExtra("id")

        val submitButton = findViewById<Button>(R.id.submitButton)
        submitButton.setOnClickListener{
            val name = teamNameInput.text.toString()

            if(name != "")
            {
                val database = Utils.database
                val users = database.getReference("users")
                val userRef = users.child(Utils.userID)
                val teamsRef = userRef.child("teams")
                val newTeam = Team(name, ArrayList<Tournament>(), ArrayList<Player>())
                val myTeam = teamsRef.child(name)
                myTeam.child("name").setValue(name)
                //myTeam.child("Tournaments").child("name").setValue("Tourney1")
                //myTeam.child("Players").child("McCoy").child("Number").setValue(13)

                var intent = Intent(this, HomeActivity::class.java)
                //intent.putExtra("id", userID)
                startActivity(intent)
            }
        }
    }
}