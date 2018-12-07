package com.example.christopher.ultihub

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_finished.*

class GameFinishedFragment : Fragment() {

    private var team = ""
    private var winScore = 0
    private var loseScore = 0

    companion object {
        fun newInstance(winningName : String, winScore : Int, loseScore: Int): GameFinishedFragment {
            val args = Bundle()
            val winningTeam = winningName
            val score1 = winScore
            val score2 = loseScore

            args.putString("team", winningTeam)
            args.putInt("winScore", score1)
            args.putInt("loseScore", score2)
            val fragment = GameFinishedFragment()
            fragment.arguments = args

            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val args = arguments!!
        team = args.getString("team")
        winScore = args.getInt("winScore")
        loseScore = args.getInt("loseScore")
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, bundle: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_finished, container, false)

        val gameSummaryText = view.findViewById(R.id.gameSummaryText) as TextView
        gameSummaryText.text = "$team win! Final score: $winScore - $loseScore"

        return view
    }
}