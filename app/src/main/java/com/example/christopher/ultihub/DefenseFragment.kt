package com.example.christopher.ultihub

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_defense.*
import kotlinx.android.synthetic.main.fragment_offense.*

class DefenseFragment : Fragment(), GameFragment {

    companion object {
        fun newInstance(): DefenseFragment {
            return DefenseFragment()
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_defense, container, false)
    }

    override fun updatePlayers(players : ArrayList<Player>) {
        val p1 = players[0]
        val p2 = players[1]
        val p3 = players[2]
        val p4 = players[3]
        val p5 = players[4]
        val p6 = players[5]
        val p7 = players[6]
    }

    fun passData (data: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}