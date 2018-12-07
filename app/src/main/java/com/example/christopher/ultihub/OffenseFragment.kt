package com.example.christopher.ultihub

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

class OffenseFragment : Fragment() {

    lateinit var dataPasser : DataPasser
    lateinit var players : ArrayList<String>
    private var posessionIndex = 0

    companion object {
        fun newInstance(playerList: ArrayList<String>): OffenseFragment {

            val args = Bundle()
            val nameList = ArrayList<String>()
            nameList.add(playerList[0])
            nameList.add(playerList[1])
            nameList.add(playerList[2])
            nameList.add(playerList[3])
            nameList.add(playerList[4])
            nameList.add(playerList[5])
            nameList.add(playerList[6])
            args.putStringArrayList("nameList", nameList)
            val fragment = OffenseFragment()
            fragment.arguments = args

            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val args = arguments!!
        players = args.getStringArrayList("nameList")
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, bundle: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_offense, container, false)

        if(players.size > 0) {
            val p1 = view.findViewById(R.id.p1Text) as TextView
            val p2 = view.findViewById(R.id.p2Text) as TextView
            val p3 = view.findViewById(R.id.p3Text) as TextView
            val p4 = view.findViewById(R.id.p4Text) as TextView
            val p5 = view.findViewById(R.id.p5Text) as TextView
            val p6 = view.findViewById(R.id.p6Text) as TextView
            val p7 = view.findViewById(R.id.p7Text) as TextView
            p1.text = players[0]
            p2.text = players[1]
            p3.text = players[2]
            p4.text = players[3]
            p5.text = players[4]
            p6.text = players[5]
            p7.text = players[6]

            this.dataPasser = activity as DataPasser
            addListeners(view)
        }
        return view
    }

    fun passData(player : String, action : String) {
        dataPasser.passData(player, action)
    }

    fun addListeners(view: View) {

        //### Initialize all buttons ###
        val throwawayBut = view.findViewById(R.id.throwawayButton) as Button
        val stalledBut = view.findViewById(R.id.oppGoalButton) as Button
        val callahanBut = view.findViewById(R.id.opponentCallahanButton) as Button

        val p1CatchBut = view.findViewById(R.id.p1Catch) as Button
        val p2CatchBut = view.findViewById(R.id.p2Catch) as Button
        val p3CatchBut = view.findViewById(R.id.p3Catch) as Button
        val p4CatchBut = view.findViewById(R.id.p4Catch) as Button
        val p5CatchBut = view.findViewById(R.id.p5Catch) as Button
        val p6CatchBut = view.findViewById(R.id.p6Catch) as Button
        val p7CatchBut = view.findViewById(R.id.p7Catch) as Button

        val p1DropBut = view.findViewById(R.id.p1Drop) as Button
        val p2DropBut = view.findViewById(R.id.p2Drop) as Button
        val p3DropBut = view.findViewById(R.id.p3Drop) as Button
        val p4DropBut = view.findViewById(R.id.p4Drop) as Button
        val p5DropBut = view.findViewById(R.id.p5Drop) as Button
        val p6DropBut = view.findViewById(R.id.p6Drop) as Button
        val p7DropBut = view.findViewById(R.id.p7Drop) as Button

        val p1GoalBut = view.findViewById(R.id.p1Goal) as Button
        val p2GoalBut = view.findViewById(R.id.p2Goal) as Button
        val p3GoalBut = view.findViewById(R.id.p3Goal) as Button
        val p4GoalBut = view.findViewById(R.id.p4Goal) as Button
        val p5GoalBut = view.findViewById(R.id.p5Goal) as Button
        val p6GoalBut = view.findViewById(R.id.p6Goal) as Button
        val p7GoalBut = view.findViewById(R.id.p7Goal) as Button


        //### Create button arrayLists ###
        val catchButList = ArrayList<Button>(7)
        catchButList.add(p1CatchBut)
        catchButList.add(p2CatchBut)
        catchButList.add(p3CatchBut)
        catchButList.add(p4CatchBut)
        catchButList.add(p5CatchBut)
        catchButList.add(p6CatchBut)
        catchButList.add(p7CatchBut)

        val dropButList = ArrayList<Button>(7)
        dropButList.add(p1DropBut)
        dropButList.add(p2DropBut)
        dropButList.add(p3DropBut)
        dropButList.add(p4DropBut)
        dropButList.add(p5DropBut)
        dropButList.add(p6DropBut)
        dropButList.add(p7DropBut)

        val goalButList = ArrayList<Button>(7)
        goalButList.add(p1GoalBut)
        goalButList.add(p2GoalBut)
        goalButList.add(p3GoalBut)
        goalButList.add(p4GoalBut)
        goalButList.add(p5GoalBut)
        goalButList.add(p6GoalBut)
        goalButList.add(p7GoalBut)


        //### Create Button listeners ###

        //Special Buttons
        throwawayBut.setOnClickListener {
            passData(players[posessionIndex], "throwaway")
            dataPasser.switchPossession(players)
        }

        stalledBut.setOnClickListener {
            passData(players[posessionIndex], "stalled")
            dataPasser.switchPossession(players)
        }

        callahanBut.setOnClickListener {
            passData(players[posessionIndex], "threw callahan")
            dataPasser.newLineOffense()
        }


        //Catch buttons
        for(catchBut in catchButList)
        {
            catchBut.setOnClickListener {
                val thisIndex = catchButList.indexOf(catchBut)
                passData(players[posessionIndex], "pass")
                passData(players[thisIndex], "catch")
                catchButList[posessionIndex].visibility = View.VISIBLE
                dropButList[posessionIndex].visibility = View.VISIBLE
                goalButList[posessionIndex].visibility = View.VISIBLE
                posessionIndex = thisIndex
                catchButList[thisIndex].visibility = View.INVISIBLE
                dropButList[thisIndex].visibility = View.INVISIBLE
                goalButList[thisIndex].visibility = View.INVISIBLE
            }
        }

        //Drop buttons
        for(dropBut in dropButList)
        {
            dropBut.setOnClickListener {
                val thisIndex = dropButList.indexOf(dropBut)
                passData(players[posessionIndex], "pass")
                passData(players[thisIndex], "drop")
                dataPasser.switchPossession(players)
            }
        }

        //Goal buttons
        for(goalBut in goalButList)
        {
            goalBut.setOnClickListener {
                val thisIndex = goalButList.indexOf(goalBut)
                passData(players[posessionIndex], "pass")
                passData(players[posessionIndex], "assist")
                passData(players[thisIndex], "drop")
                dataPasser.newLineDefense()
            }
        }
    }
}