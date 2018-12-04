package com.example.christopher.ultihub

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.list_item.view.*
import com.google.firebase.database.DataSnapshot
import kotlinx.android.synthetic.main.tournament_detail.*


class TournamentDetailActivity : AppCompatActivity() {

    lateinit var gameList : MutableList<Game>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tournament_detail)

        val teamName = intent.getStringExtra("teamName")
        val tournamentName = intent.getStringExtra("tournamentName")

        gameList = mutableListOf()

        val database = Utils.database
        val team = database.getReference("users").child(Utils.userID).child("teams").child(teamName)
        val tournament = team.child("tournaments").child(tournamentName)
        val gamesRef = tournament.child("games")

        gameListRecycler.layoutManager = LinearLayoutManager(this)
        gameListRecycler.adapter = GameAdapter(gameList, this, teamName, tournamentName)

        newGameButton.setOnClickListener {
            val intent = Intent(this, GameCreateActivity::class.java).apply{
                putExtra("teamName", teamName)
                putExtra("tournamentName", tournamentName)
            }
            startActivity(intent)
        }

        gamesRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                dataSnapshot.run{

                    val games = children.mapNotNull {
                        it.getValue(GameResponse::class.java)
                    }

                    val gameList = games.map(GameResponse::mapToGame)

                    gameListRecycler.layoutManager = LinearLayoutManager(baseContext)
                    gameListRecycler.adapter = GameAdapter(gameList, baseContext, teamName, tournamentName)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(HomeActivity.TAG, "Failed to read value.", error.toException())
            }
        })
    }

    class GameAdapter(val items : List<Game>, val context: Context, val teamName : String, val tournamentName : String) : RecyclerView.Adapter<GameAdapter.ViewHolder>() {

        private val onClickListener : View.OnClickListener

        init {
            onClickListener = View.OnClickListener { v ->
                val item = v.tag as Game

                val intent = Intent(v.context, LiveGameActivity::class.java).apply {
                    putExtra("teamName", teamName)
                    putExtra("tournamentName", tournamentName)
                    putExtra("opponent", item.opponent)
                }
                v.context.startActivity(intent)
            }
        }

        override fun getItemCount(): Int {
            return items.size
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item, parent, false))
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = items[position]
            holder.itemView.itemNameText.text = items.get(position).title

            with(holder.itemView) {
                tag = item
                setOnClickListener(onClickListener)
            }
        }

        inner class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
            // Holds the TextView that will add each animal to
            val tvGameName = view.itemNameText
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
