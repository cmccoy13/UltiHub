package com.example.christopher.ultihub

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.MenuItem
import android.view.View
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
        for (player in playerList) {
            val tr = TableRow(this)
            tr.layoutParams = TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f)
            tr.gravity

            val name = TextView(this)
            name.text = player.name
            name.textAlignment = View.TEXT_ALIGNMENT_CENTER
            name.gravity = 1
            name.textSize = 18f
            name.layoutParams =
                    TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT)
            tr.addView(name)

            val goals = TextView(this)
            goals.text = player.goals.toString()
            goals.textAlignment = View.TEXT_ALIGNMENT_CENTER
            goals.gravity = 1
            goals.textSize = 18f
            goals.layoutParams =
                    TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT)
            tr.addView(goals)

            val assists = TextView(this)
            assists.text = player.assists.toString()
            assists.textAlignment = View.TEXT_ALIGNMENT_CENTER
            assists.gravity = 1
            assists.textSize = 18f
            assists.layoutParams =
                    TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT)
            tr.addView(assists)

            val passes = TextView(this)
            passes.text = player.passes.toString()
            passes.textAlignment = View.TEXT_ALIGNMENT_CENTER
            passes.gravity = 1
            passes.textSize = 18f
            passes.layoutParams =
                    TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT)
            tr.addView(passes)

            val catches = TextView(this)
            catches.text = player.catches.toString()
            catches.textAlignment = View.TEXT_ALIGNMENT_CENTER
            catches.gravity = 1
            catches.textSize = 18f
            catches.layoutParams =
                    TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT)
            tr.addView(catches)

            val drops = TextView(this)
            drops.text = player.drops.toString()
            drops.textAlignment = View.TEXT_ALIGNMENT_CENTER
            drops.gravity = 1
            drops.textSize = 18f
            drops.layoutParams =
                    TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT)
            tr.addView(drops)

            val throwaways = TextView(this)
            throwaways.text = player.throwaways.toString()
            throwaways.textAlignment = View.TEXT_ALIGNMENT_CENTER
            throwaways.gravity = 1
            throwaways.textSize = 18f
            throwaways.layoutParams =
                    TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT)
            tr.addView(throwaways)

            val ds = TextView(this)
            ds.text = player.Ds.toString()
            ds.textAlignment = View.TEXT_ALIGNMENT_CENTER
            ds.gravity = 1
            ds.textSize = 18f
            ds.layoutParams =
                    TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT)
            tr.addView(ds)

            val stalls = TextView(this)
            stalls.text = player.stalls.toString()
            stalls.textAlignment = View.TEXT_ALIGNMENT_CENTER
            stalls.gravity = 1
            stalls.textSize = 18f
            stalls.layoutParams =
                    TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT)
            tr.addView(stalls)

            val timesStalled = TextView(this)
            timesStalled.text = player.stalled.toString()
            timesStalled.textAlignment = View.TEXT_ALIGNMENT_CENTER
            timesStalled.gravity = 1
            timesStalled.textSize = 18f
            timesStalled.layoutParams =
                    TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT)
            tr.addView(timesStalled)

            val callahans = TextView(this)
            callahans.text = player.callahans.toString()
            callahans.textAlignment = View.TEXT_ALIGNMENT_CENTER
            callahans.gravity = 1
            callahans.textSize = 18f
            callahans.layoutParams =
                    TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT)
            tr.addView(callahans)

            val callahansThrown = TextView(this)
            callahansThrown.text = player.callahansThrown.toString()
            callahansThrown.textAlignment = View.TEXT_ALIGNMENT_CENTER
            callahansThrown.gravity = 1
            callahansThrown.textSize = 18f
            callahansThrown.layoutParams =
                    TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT)
            tr.addView(callahansThrown)

            table.addView(
                tr,
                TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT)
            )
        }
    }
}