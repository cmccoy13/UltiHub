package com.example.christopher.ultihub

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.list_item.view.*
import com.google.firebase.database.DataSnapshot


class HomeActivity : AppCompatActivity() {
    companion object {
        val TAG = "Firebase transaction"
    }

    lateinit var teamList : MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //val userID = intent.getStringExtra("id")

        teamList = mutableListOf()

        val database = Utils.database
        val users = database.getReference("users")
        val userRef = users.child(Utils.userID)

        val teamsRef = userRef.child("teams")

        val createTeamButton = findViewById<Button>(R.id.createTeamButton)
        createTeamButton.setOnClickListener{
            startActivity(Intent(this, TeamCreateActivity::class.java))
        }


        teamListRecycler.layoutManager = LinearLayoutManager(this)
        teamListRecycler.adapter = TeamAdapter(teamList, this)

        teamsRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                dataSnapshot.run{

                    val teams = children.mapNotNull {
                        it.child("name").getValue().toString()
                    }

                    teamListRecycler.layoutManager = LinearLayoutManager(baseContext)
                    teamListRecycler.adapter = TeamAdapter(teams, baseContext)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
    }

    class TeamAdapter(val items : List<String>, val context: Context) : RecyclerView.Adapter<TeamAdapter.ViewHolder>() {

        private val onClickListener : View.OnClickListener

        init {
            onClickListener = View.OnClickListener { v ->
                val item = v.tag as String

                val intent = Intent(v.context, TeamDetailActivity::class.java).apply {
                    putExtra("Name", item)
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
            holder.itemView.itemNameText.text = items.get(position)

            with(holder.itemView) {
                tag = item
                setOnClickListener(onClickListener)
            }
        }

        inner class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
            // Holds the TextView that will add each animal to
            val tvTeamName = view.itemNameText
        }
    }
}
