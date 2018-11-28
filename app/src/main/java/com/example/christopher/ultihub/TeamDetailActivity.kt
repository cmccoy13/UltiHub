package com.example.christopher.ultihub

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.team_detail.*
import kotlinx.android.synthetic.main.list_item.view.*

class TeamDetailActivity : AppCompatActivity() {

    lateinit var tournamentList : MutableList<Tournament>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.team_detail)

        tournamentList = mutableListOf()

        val database = Utils.database
        val name = intent.getStringExtra("Name")
        val teamList = database.getReference(Utils.userID).child("teams")
        val team = teamList.child(name)

        val tournamentRef = team.child("tournaments")

        tournamentListRecycler.layoutManager = LinearLayoutManager(this)
        tournamentListRecycler.adapter = TournamentAdapter(tournamentList, this)

        teamName.text = team.key

        deleteTeamButton.setOnClickListener{
            team.removeValue()
            NavUtils.navigateUpTo(this, Intent(this, HomeActivity::class.java))
        }

        addTournamentButton.setOnClickListener{
            startActivity(Intent(this, TournamentCreateActivity::class.java))
        }

        addPlayerButton.setOnClickListener{
            startActivity(Intent(this, PlayerCreateActivity::class.java))
        }

        teamStatsButton.setOnClickListener{
            startActivity(Intent(this, StatsActivity::class.java))
        }

        tournamentRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                dataSnapshot.run{

                    val tournaments = children.mapNotNull {
                        it.getValue(TournamentResponse::class.java)
                    }

                    val tournamentList = tournaments.map(TournamentResponse::mapToTournament)

                    tournamentListRecycler.layoutManager = LinearLayoutManager(baseContext)
                    tournamentListRecycler.adapter = TournamentAdapter(tournamentList, baseContext)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(HomeActivity.TAG, "Failed to read value.", error.toException())
            }
        })
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = TournamentAdapter(tournamentList, this)
    }

    class TournamentAdapter(val items : List<Tournament>, val context: Context) : RecyclerView.Adapter<TournamentAdapter.ViewHolder>() {

        private val onClickListener : View.OnClickListener

        init {
            onClickListener = View.OnClickListener { v ->
                val item = v.tag as Tournament

                val intent = Intent(v.context, GameCreateActivity::class.java).apply {
                    putExtra("Name", item.name)
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
            //holder.view.tvTeamName.text = items.get(position).name
            val item = items[position]
            holder.itemView.itemNameText.text = items.get(position).name

            with(holder.itemView) {
                tag = item
                setOnClickListener(onClickListener)
            }
        }

        inner class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
            // Holds the TextView that will add each animal to
            val tvTournamentName = view.itemNameText
        }
    }
}