package com.example.christopher.ultihub

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import com.example.christopher.ultihub.Utils.database
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.create_game.*
import kotlinx.android.synthetic.main.create_team.*
import kotlinx.android.synthetic.main.create_tournament.*
import kotlinx.android.synthetic.main.team_detail.*
import kotlinx.android.synthetic.main.view_stats.*

class StatsActivity : AppCompatActivity() {

    lateinit var playerList : List<Player>

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_stats)

        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_home_button)

        playerList = mutableListOf()

        val database = Utils.database
        val name = intent.getStringExtra("teamName")
        val teamList = database.getReference("users").child(Utils.userID).child("teams")
        val playerRef = teamList.child(name).child("players")

        playerRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                dataSnapshot.run{

                    val players = children.mapNotNull {
                        it.getValue(PlayerResponse::class.java)
                    }

                    playerList = players.map(PlayerResponse::mapToPlayer)
                    createTable()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(HomeActivity.TAG, "Failed to read value.", error.toException())
            }
        })
    }

    fun createTable() {
        /* Create a new row to be added. */

        val nameIndex = TextView(this)
        nameIndex.text

        for (player in playerList) {
            val tr = TableRow(this)
            val trParams = TableLayout.LayoutParams(table.width, TableRow.LayoutParams.WRAP_CONTENT)
            tr.layoutParams = trParams

            val params = TableRow.LayoutParams(table.width/(playerList.size), TableRow.LayoutParams.MATCH_PARENT, 1.0f)

            val name = TextView(this)
            name.text = player.name
            name.textAlignment = View.TEXT_ALIGNMENT_CENTER
            name.textSize = 18f
            name.layoutParams = params
            name.setTypeface(null, Typeface.BOLD)
            tr.addView(name, params)

            val goals = TextView(this)
            goals.text = player.goals.toString()
            goals.textAlignment = View.TEXT_ALIGNMENT_CENTER
            goals.textSize = 18f
            goals.layoutParams = params
            tr.addView(goals, params)

            val assists = TextView(this)
            assists.text = player.assists.toString()
            assists.textAlignment = View.TEXT_ALIGNMENT_CENTER
            assists.textSize = 18f
            assists.layoutParams = params
            tr.addView(assists, params)

            val passes = TextView(this)
            passes.text = player.passes.toString()
            passes.textAlignment = View.TEXT_ALIGNMENT_CENTER
            passes.textSize = 18f
            passes.layoutParams = params
            tr.addView(passes, params)

            val catches = TextView(this)
            catches.text = player.catches.toString()
            catches.textAlignment = View.TEXT_ALIGNMENT_CENTER
            catches.textSize = 18f
            catches.layoutParams = params
            tr.addView(catches, params)

            val drops = TextView(this)
            drops.text = player.drops.toString()
            drops.textAlignment = View.TEXT_ALIGNMENT_CENTER
            drops.textSize = 18f
            drops.layoutParams = params
            tr.addView(drops, params)

            val throwaways = TextView(this)
            throwaways.text = player.throwaways.toString()
            throwaways.textAlignment = View.TEXT_ALIGNMENT_CENTER
            throwaways.textSize = 18f
            throwaways.layoutParams = params
            tr.addView(throwaways, params)

            val ds = TextView(this)
            ds.text = player.Ds.toString()
            ds.textAlignment = View.TEXT_ALIGNMENT_CENTER
            ds.textSize = 18f
            ds.layoutParams = params
            tr.addView(ds, params)

            val stalls = TextView(this)
            stalls.text = player.stalls.toString()
            stalls.textAlignment = View.TEXT_ALIGNMENT_CENTER
            stalls.textSize = 18f
            stalls.layoutParams = params
            tr.addView(stalls, params)

            val timesStalled = TextView(this)
            timesStalled.text = player.stalled.toString()
            timesStalled.textAlignment = View.TEXT_ALIGNMENT_CENTER
            timesStalled.textSize = 18f
            timesStalled.layoutParams = params
            tr.addView(timesStalled, params)

            val callahans = TextView(this)
            callahans.text = player.callahans.toString()
            callahans.textAlignment = View.TEXT_ALIGNMENT_CENTER
            callahans.textSize = 18f
            callahans.layoutParams = params
            tr.addView(callahans, params)

            val callahansThrown = TextView(this)
            callahansThrown.text = player.callahansThrown.toString()
            callahansThrown.textAlignment = View.TEXT_ALIGNMENT_CENTER
            callahansThrown.textSize = 18f
            callahansThrown.layoutParams =params
            tr.addView(callahansThrown, params)

            table.addView(tr, trParams)
        }
    }
}