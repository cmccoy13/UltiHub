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
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.list_item.view.*
import kotlinx.android.synthetic.main.live_game.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class LiveGameActivity : AppCompatActivity(), DataPasser {

    lateinit var teamName : String
    lateinit var tournamentName : String
    lateinit var rosterList : List<Player>
    lateinit var currentLineup : ArrayList<String>
    lateinit var playerRef : DatabaseReference
    lateinit var gameRef : DatabaseReference
    var opponentName = ""
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
    var gameFinished = false

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

        playerRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                dataSnapshot.run{

                    val players = children.mapNotNull {
                        it.getValue(PlayerResponse::class.java)
                    }

                    rosterList = players.map(PlayerResponse::mapToPlayer)

                    if(!gameFinished) {
                        rosterRecycler.layoutManager = LinearLayoutManager(act)
                        rosterRecycler.adapter = RosterAdapter(rosterList, baseContext, teamName, act)
                    }
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
            val frag = PickupFragment.newInstance(players)

            supportFragmentManager.beginTransaction()
                .replace(R.id.gameLayout, frag).commit()
        }
        else {
            val frag = DefenseFragment.newInstance(players)

            supportFragmentManager.beginTransaction()
                .replace(R.id.gameLayout, frag).commit()
        }
    }

    override fun toOffense(pickupIndex : Int, players: ArrayList<String>) {
        val frag = OffenseFragment.newInstance(pickupIndex, players)

        supportFragmentManager.beginTransaction()
            .replace(R.id.gameLayout, frag).commit()
    }

    override fun switchPossession(players : ArrayList<String>) {
        if(onOffense) {
            val frag = DefenseFragment.newInstance(players)

            supportFragmentManager.beginTransaction()
                .replace(R.id.gameLayout, frag).commit()
        }
        else {
            val frag = PickupFragment.newInstance(players)
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

        if(!secondHalf && ourScore == halftime) {
            secondHalf = true
            Toast.makeText(this, "Halftime! $teamName lead $ourScore-$oppScore", Toast.LENGTH_LONG)

            if(!startingOnOffense)
                onOffense = true
        }

        if(ourScore >= maxScore || hardCapIsOn) {
            if ((ourScore >= (oppScore + 2)) || (hardCapIsOn && ourScore > oppScore))
            {
                gameFinished = true
                Toast.makeText(this, "$teamName win!  Final Score: $ourScore-$oppScore", Toast.LENGTH_LONG)
            }
        }

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

        if(gameFinished) {
            val frag = GameFinishedFragment.newInstance(teamName, ourScore, oppScore)
            supportFragmentManager.beginTransaction()
                .replace(R.id.gameLayout, frag).commit()

            val finishedRef = gameRef.child("gameFinished")
            posRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    finishedRef.setValue(true)
                }

                override fun onCancelled(p0: DatabaseError) {
                }
            })

            gameClockText.text = "Game Completed"
        }
        else {
            val frag = LiveGameFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.gameLayout, frag).commit()

            rosterRecycler.layoutManager = LinearLayoutManager(this)
            rosterRecycler.adapter = RosterAdapter(rosterList, this, teamName, this)
        }
    }

    override fun newLineOffense() {
        oppScore++
        onOffense = true
        opponentScoreText.text = oppScore.toString()

        if(!secondHalf && oppScore == halftime) {
            secondHalf = true
            Toast.makeText(this, "Halftime! $opponentName lead $oppScore-$ourScore", Toast.LENGTH_LONG)

            if(startingOnOffense)
                onOffense = false
        }

        if(oppScore >= maxScore || hardCapIsOn) {
            if ((oppScore >= (ourScore + 2)) || (hardCapIsOn && oppScore > ourScore))
            {
                gameFinished = true
                Toast.makeText(this, "$opponentName win!  Final Score: $oppScore-$ourScore", Toast.LENGTH_LONG)
            }
        }

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

        if(gameFinished) {
            val frag = GameFinishedFragment.newInstance(opponentName, oppScore, ourScore)
            supportFragmentManager.beginTransaction()
                .replace(R.id.gameLayout, frag).commit()

            val finishedRef = gameRef.child("gameFinished")
            posRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    finishedRef.setValue(true)
                }

                override fun onCancelled(p0: DatabaseError) {
                }
            })

            gameClockText.text = "Game Completed"

        }
        else {
            val frag = LiveGameFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.gameLayout, frag).commit()

            rosterRecycler.layoutManager = LinearLayoutManager(this)
            rosterRecycler.adapter = RosterAdapter(rosterList, this, teamName, this)
        }
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
            "stall" -> stat = "stalls"
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
        opponentName = game.opponent
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
        gameFinished = game.gameFinished
        halftime = maxScore/2

        if(ourScore >= halftime || oppScore >= halftime) {
            secondHalf = true
        }

        if(gameFinished)
            gameClockText.text = "Game Completed"
        else
            startClock(game.startDate, game.softCap, game.hardCap)

        ourTeamText.text = teamName
        opponentTeamText.text = opponentName
        ourScoreText.text = ourScore.toString()
        opponentScoreText.text = oppScore.toString()

        if(!secondHalf) {
            ourTimeoutsText.text = "Timeouts Remaining: " + (ourTOsFirstHalf + ourTOsFloater).toString()
            opponentTimeoutsText.text = "Timeouts Remaining: " + (oppTOsFirstHalf + oppTOsFloater).toString()
        }
        else {
            ourTimeoutsText.text = "Timeouts Remaining: " + (ourTOsSecondHalf + ourTOsFloater).toString()
            opponentTimeoutsText.text = "Timeouts Remaining: " + (oppTOsSecondHalf + oppTOsFloater).toString()
        }

        ourTOButton.setOnClickListener {
            if(!secondHalf) {
                if (ourTOsFirstHalf > 0) {
                    ourTOsFirstHalf--
                }
                else if (ourTOsFloater > 0) {
                    ourTOsFloater--
                }
                else {
                    Toast.makeText(this, "No timeouts remaining!", Toast.LENGTH_SHORT).show()
                }

                ourTimeoutsText.text = "Timeouts Remaining: " + (ourTOsFirstHalf + ourTOsFloater).toString()
            }
            else {
                if (ourTOsSecondHalf > 0) {
                    ourTOsSecondHalf--
                }
                else if (ourTOsFloater > 0) {
                    ourTOsFloater--
                }
                else {
                    Toast.makeText(this, "No timeouts remaining!", Toast.LENGTH_SHORT).show()
                }

                ourTimeoutsText.text = "Timeouts Remaining: " + (ourTOsSecondHalf + ourTOsFloater).toString()
            }
        }

        oppTOButton.setOnClickListener {
            if(!secondHalf) {
                if (oppTOsFirstHalf > 0) {
                    oppTOsFirstHalf--
                }
                else if (oppTOsFloater > 0) {
                    oppTOsFloater--
                }
                else {
                    Toast.makeText(this, "No timeouts remaining!", Toast.LENGTH_SHORT).show()
                }

                opponentTimeoutsText.text = "Timeouts Remaining: " + (oppTOsFirstHalf + oppTOsFloater).toString()
            }
            else {
                if (oppTOsSecondHalf > 0) {
                    oppTOsSecondHalf--
                }
                else if (oppTOsFloater > 0) {
                    oppTOsFloater--
                }
                else {
                    Toast.makeText(this, "No timeouts remaining!", Toast.LENGTH_SHORT).show()
                }

                opponentTimeoutsText.text = "Timeouts Remaining: " + (oppTOsSecondHalf + oppTOsFloater).toString()
            }
        }

        if(gameFinished) {

            when (ourScore > oppScore) {
                true -> {
                    val frag = GameFinishedFragment.newInstance(teamName, ourScore, oppScore)
                    supportFragmentManager.beginTransaction()
                        .add(R.id.gameLayout, frag).commit()
                }
                false -> {
                    val frag = GameFinishedFragment.newInstance(opponentName, oppScore, ourScore)
                    supportFragmentManager.beginTransaction()
                        .add(R.id.gameLayout, frag).commit()
                }
            }
        }
        else {
            val frag = LiveGameFragment()
            supportFragmentManager.beginTransaction()
                .add(R.id.gameLayout, frag).commit()
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

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item!!.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return false
    }
}
