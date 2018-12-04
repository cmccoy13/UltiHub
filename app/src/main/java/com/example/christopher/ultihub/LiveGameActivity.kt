package com.example.christopher.ultihub

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.os.SystemClock
import android.support.v4.app.Fragment
import android.support.v4.app.NavUtils
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.create_game.*
import kotlinx.android.synthetic.main.create_team.*
import kotlinx.android.synthetic.main.create_tournament.*
import kotlinx.android.synthetic.main.fragment_defense.view.*
import kotlinx.android.synthetic.main.list_item.view.*
import kotlinx.android.synthetic.main.live_game.*
import kotlinx.android.synthetic.main.team_detail.*
import java.sql.Time
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.*
import kotlin.math.floor

class LiveGameActivity : AppCompatActivity(), OnItemClick, DataPasser {

    lateinit var teamName : String
    lateinit var tournamentName : String
    lateinit var rosterList : MutableList<Player>
    lateinit var currentFrag : GameFragment
    lateinit var currentLineup : ArrayList<Player>
    lateinit var playerRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.live_game)

        rosterList = mutableListOf()

        teamName = intent.getStringExtra("teamName")
        tournamentName = intent.getStringExtra("tournamentName")
        val opponent = intent.getStringExtra("opponent")

        val database = Utils.database
        val team = database.getReference("users").child(Utils.userID).child("teams").child(teamName)
        playerRef = team.child("players")
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

        /*rosterRecycler.layoutManager = LinearLayoutManager(this)
        rosterRecycler.adapter = RosterAdapter(rosterList, this, teamName, this)*/
        val act = this

        playerRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                dataSnapshot.run{

                    val players = children.mapNotNull {
                        it.getValue(PlayerResponse::class.java)
                    }

                    val rosterList = players.map(PlayerResponse::mapToPlayer)

                    rosterRecycler.layoutManager = LinearLayoutManager(act)
                    rosterRecycler.adapter = RosterAdapter(rosterList, baseContext, teamName, act)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(HomeActivity.TAG, "Failed to read value.", error.toException())
            }
        })

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

    override fun onClick(players : ArrayList<Player>) {
        currentLineup = players
        currentFrag.updatePlayers(currentLineup)
    }

    override fun passData(player : String, action: String) {
        var stat : String = ""

        when(action) {
            "throwaway" -> stat = "throwaways"
            "pass" -> stat = "passes"
            "drop" -> stat = "drops"
            "goal" -> stat = "goals"
            "assist" -> stat = "assists"
            "stalled" -> stat = "stalled"
            "threw callahan" -> stat = "callahans thrown"
            "catch" -> stat = "catches"
            "D" -> stat = "Ds"
            "callahan" -> stat = "callahans"
        }

        val statRef = playerRef.child(player).child(stat)

        statRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val data = dataSnapshot.value
                var newVal = data.toString().toInt()
                newVal++
                statRef.setValue(newVal)
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }

    fun begin(game : GameResponse) {
        //val halftime = maxScore/2

        startClock(game.startDate, game.hardCap)
        if(game.onOffense) {
            val frag = OffenseFragment()

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            frag.arguments = intent.extras

            // Add the fragment to the 'fragment_container' FrameLayout
            supportFragmentManager.beginTransaction()
                .add(R.id.gameLayout, frag).commit()

            currentFrag = frag
        }
        else {
            val frag = DefenseFragment()

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            frag.arguments = intent.extras

            // Add the fragment to the 'fragment_container' FrameLayout
            supportFragmentManager.beginTransaction()
                .add(R.id.gameLayout, frag).commit()

            currentFrag = frag
        }

        ourTeamText.text = teamName
        opponentTeamText.text = game.opponent
    }

    fun startClock(startDT : String, hardCap : Int) {
        val currentMS = Calendar.getInstance().timeInMillis
        val startTime = SimpleDateFormat("MM-dd-yyyy HH:mm:ss").parse(startDT)
        val startMS = startTime.time

        val timeUntilStart = startMS-currentMS

        object : CountDownTimer((timeUntilStart + hardCap*60*1000), 1000) {

            override fun onTick(millisUntilFinished: Long) {
                val minutesUntilFinished = ((millisUntilFinished / 1000)/60)
                val secondsUntilFinished = (millisUntilFinished / 1000)%60

                if(millisUntilFinished >= hardCap*1000*60) { //if game has not yet started
                    val minutesUntilStart = ((millisUntilFinished - hardCap*1000*60)/1000)/60
                    val secondsUntilStart = ((millisUntilFinished - hardCap*1000%60)/1000)%60

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
    }

    class RosterAdapter(val items : List<Player>, val context: Context, val teamName : String, listener : OnItemClick) : RecyclerView.Adapter<RosterAdapter.ViewHolder>() {

        var selectedPlayers : ArrayList<Player> = arrayListOf()
        var numSelected = 0
        private val onClickListener : View.OnClickListener

        init {
            onClickListener = View.OnClickListener { v ->
                val item = v.tag as Player

                if(selectedPlayers.contains(item)) {
                    v.setBackgroundColor(Color.WHITE)
                    selectedPlayers.remove(item)
                    numSelected--
                }
                else {
                    v.setBackgroundColor(Color.LTGRAY)
                    numSelected++
                    selectedPlayers.add(item)

                    if(numSelected == 7){
                        listener.onClick(selectedPlayers)
                        numSelected = 0
                        selectedPlayers.clear()
                    }
                }
            }
        }

        override fun getItemCount(): Int {
            return items.size
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item, parent, false))
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            //holder.view.tvTeamName.text = items.get(position).name
            val item = items[position]
            holder.itemView.itemNameText.text = items.get(position).name

            with(holder.itemView) {
                tag = item
                setOnClickListener(onClickListener)
            }
        }

        inner class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
            val tvPlayerName = view.itemNameText
        }
    }

    /*override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item!!.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return false
    }*/
}
