package com.example.christopher.ultihub

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import kotlinx.android.synthetic.main.fragment_offense.*

class OffenseFragment : Fragment(), GameFragment {

    lateinit var dataPasser : DataPasser
    var fullLine = false
    lateinit var players : ArrayList<Player>
    private var posessionIndex = 0

    companion object {
        fun newInstance(): OffenseFragment {
            return OffenseFragment()
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        val dataPasser = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_offense, container, false)

        /*val throwawayBut = view.findViewById(R.id.throwawayButton) as Button

        throwawayBut.setOnClickListener {
            passData(players.get(posessionIndex).name, "throwaway")
        }*/

        return view
    }

    override fun updatePlayers(playerList : ArrayList<Player>) {
        players = playerList

        p1Text.text = players[0].name
        p2Text.text = players[1].name
        p3Text.text = players[2].name
        p4Text.text = players[3].name
        p5Text.text = players[4].name
        p6Text.text = players[5].name
        p7Text.text = players[6].name

        fullLine = true
    }

    fun passData(player : String, action : String) {
        dataPasser.passData(player, action)
    }
}