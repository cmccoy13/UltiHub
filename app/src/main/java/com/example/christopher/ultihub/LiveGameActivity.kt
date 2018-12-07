package com.example.christopher.ultihub

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.list_item.view.*
import kotlinx.android.synthetic.main.live_game.*
import java.text.SimpleDateFormat
import java.util.*

class LiveGameActivity : AppCompatActivity(), DataPasser {

    lateinit var teamName : String
    lateinit var tournamentName : String
    lateinit var rosterList : List<Player>
    lateinit var currentLineup : ArrayList<String>
    lateinit var playerRef : DatabaseReference
    lateinit var gameRef : DatabaseReference
    var ourScore = 0
    var oppScore = 0
    var ourTOsFirstHalf = 0
    var ourTOsSecondHalf = 0
    var ourTOsFloater = 0
    var oppTOsFirstHalf = 0
    var oppTOsSecondHalf = 0
    var oppTOsFloater = 0
    var maxScore = 0
    var halftime = 0
    var softCapIsOn = false
    var hardCapIsOn = false
    var onOffense = false
    var secondHalf = false
    var startingOnOffense = false

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.live_game)

        teamName = intent.getStringExtra("teamName")
        tournamentName = intent.getStringExtra("tournamentName")
        val opponent = intent.getStringExtra("opponent")

        val database = Utils.database
        val team = database.getReference("users").child(Utils.userID).child("teams").child(teamName)
        playerRef = team.child("players")
        val tournament = team.child("tournaments").child(tournamentName)
        gameRef = tournament.child("games").child(opponent)

        gameRef.child("title").setValue("vs. $opponent")

        val act = this
        val frag = LiveGameFragment()

        supportFragmentManager.beginTransaction()
            .add(R.id.gameLayout, frag).commit()

        playerRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                dataSnapshot.run{

                    val players = children.mapNotNull {
                        it.getValue(PlayerResponse::class.java)
                    }

                    rosterList = players.map(PlayerResponse::mapToPlayer)

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

    override fun beginPoint(players : ArrayList<String>) {
        currentLineup = players

        if(onOffense) {
            val frag = OffenseFragment.newInstance(players)

            supportFragmentManager.beginTransaction()
                .replace(R.id.gameLayout, frag).commit()
        }
        else {
            val frag = DefenseFragment.newInstance(players)

            supportFragmentManager.beginTransaction()
                .replace(R.id.gameLayout, frag).commit()
        }
    }

    override fun switchPossession(players : ArrayList<String>) {
        if(onOffense) {
            val frag = DefenseFragment.newInstance(players)

            supportFragmentManager.beginTransaction()
                .replace(R.id.gameLayout, frag).commit()
        }
        else {
            val frag = OffenseFragment.newInstance(players)

            supportFragmentManager.beginTransaction()
                .replace(R.id.gameLayout, frag).commit()
        }

        onOffense = !onOffense

        val posRef = gameRef.child("onOffense")
        posRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                posRef.setValue(onOffense)
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }

    override fun newLineDefense() {
        onOffense = false
        ourScore++
        ourScoreText.text = ourScore.toString()

        val scoreRef = gameRef.child("ourScore")
        scoreRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val data = dataSnapshot.value
                var newVal = data.toString().toInt()
                newVal++
                scoreRef.setValue(newVal)
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })

        val posRef = gameRef.child("onOffense")
        posRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                posRef.setValue(onOffense)
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })

        val frag = LiveGameFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.gameLayout, frag).commit()

        rosterRecycler.layoutManager = LinearLayoutManager(this)
        rosterRecycler.adapter = RosterAdapter(rosterList, this, teamName, this)
    }

    override fun newLineOffense() {
        onOffense = true
        oppScore++
        opponentScoreText.text = oppScore.toString()

        val statRef = gameRef.child("oppScore")

        statRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val data = dataSnapshot.value
                var newVal = data.toString().toInt()
                newVal++
                statRef.setValue(newVal)
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })

        val posRef = gameRef.child("onOffense")
        posRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                posRef.setValue(onOffense)
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })

        val frag = LiveGameFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.gameLayout, frag).commit()

        rosterRecycler.layoutManager = LinearLayoutManager(this)
        rosterRecycler.adapter = RosterAdapter(rosterList, this, teamName, this)
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
            }
        })
    }

    fun begin(game : GameResponse) {
        maxScore = game.maxScore
        ourScore = game.ourScore
        oppScore = game.oppScore
        startingOnOffense = game.startingOnOffense
        onOffense = game.onOffense
        ourTOsFirstHalf = game.ourTOsFirstHalf
        ourTOsSecondHalf = game.ourTOsSecondHalf
        ourTOsFloater = game.ourTOsFloater
        oppTOsFirstHalf = game.oppTOsFirstHalf
        oppTOsSecondHalf = game.oppTOsSecondHalf
        oppTOsFloater = game.oppTOsFloater
        halftime = maxScore/2

        if(ourScore >= halftime || oppScore >= halftime) {
            secondHalf = true
        }

        startClock(game.startDate, game.softCap, game.hardCap)

        ourTeamText.text = teamName
        opponentTeamText.text = game.opponent
        ourScoreText.text = ourScore.toString()
        opponentScoreText.text = oppScore.toString()

        if(!secondHalf) {
            ourTimeoutsText.text = (ourTOsFirstHalf + ourTOsFloater).toString()
            opponentTimeoutsText.text = (oppTOsFirstHalf + oppTOsFloater).toString()
        }
        else {
            ourTimeoutsText.text = (ourTOsSecondHalf + ourTOsFloater).toString()
            opponentTimeoutsText.text = (oppTOsSecondHalf + oppTOsFloater).toString()
        }
    }

    fun startClock(startDT : String, softCap: Int, hardCap : Int) {
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
                hardCapIsOn = true
            }
        }.start()
    }

    class RosterAdapter(val items : List<Player>, val context: Context, val teamName : String, listener : DataPasser) : RecyclerView.Adapter<RosterAdapter.ViewHolder>() {

        var selectedPlayers : ArrayList<Player> = ArrayList(7)
        var playerNames : ArrayList<String> = ArrayList(7)
        var numSelected = 0
        private val onClickListener : View.OnClickListener

        init {
            onClickListener = View.OnClickListener { v ->
                val item = v.tag as Player

                if(selectedPlayers.contains(item)) {
                    v.setBackgroundColor(Color.WHITE)
                    selectedPlayers.remove(item)
                    playerNames.remove(item.name)
                    numSelected--
                }
                else {
                    v.setBackgroundColor(Color.LTGRAY)
                    numSelected++
                    selectedPlayers.add(item)
                    playerNames.add(item.name)

                    if(numSelected == 7){
                        listener.beginPoint(playerNames)

                        /*for(i in 0..6) {
                            val index = items.indexOf(selectedPlayers[i])
                            selectedPlayers[i]
                            this.
                        }*/

                        numSelected = 0
                        selectedPlayers.clear()
                        playerNames.clear()
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

        fun clearSelections() {

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
