package com.example.christopher.ultihub

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

class DefenseFragment : Fragment() {

    lateinit var dataPasser : DataPasser
    lateinit var players : ArrayList<String>

    companion object {
        fun newInstance(playerList: ArrayList<String>): DefenseFragment {

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
            val fragment = DefenseFragment()
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_defense, container, false)

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
        val oppGoalBut = view.findViewById(R.id.oppGoalButton) as Button
        val oppThrowawayBut = view.findViewById(R.id.oppThrowayButton) as Button

        val p1DBut = view.findViewById(R.id.p1D) as Button
        val p2DBut = view.findViewById(R.id.p2D) as Button
        val p3DBut = view.findViewById(R.id.p3D) as Button
        val p4DBut = view.findViewById(R.id.p4D) as Button
        val p5DBut = view.findViewById(R.id.p5D) as Button
        val p6DBut = view.findViewById(R.id.p6D) as Button
        val p7DBut = view.findViewById(R.id.p7D) as Button

        val p1StallBut = view.findViewById(R.id.p1stall) as Button
        val p2StallBut = view.findViewById(R.id.p2stall) as Button
        val p3StallBut = view.findViewById(R.id.p3stall) as Button
        val p4StallBut = view.findViewById(R.id.p4stall) as Button
        val p5StallBut = view.findViewById(R.id.p5stall) as Button
        val p6StallBut = view.findViewById(R.id.p6stall) as Button
        val p7StallBut = view.findViewById(R.id.p7stall) as Button

        val p1CallahanBut = view.findViewById(R.id.p1callahan) as Button
        val p2CallahanBut = view.findViewById(R.id.p2callahan) as Button
        val p3CallahanBut = view.findViewById(R.id.p3callahan) as Button
        val p4CallahanBut = view.findViewById(R.id.p4callahan) as Button
        val p5CallahanBut = view.findViewById(R.id.p5callahan) as Button
        val p6CallahanBut = view.findViewById(R.id.p6callahan) as Button
        val p7CallahanBut = view.findViewById(R.id.p7callahan) as Button


        //### Create button arrayLists ###
        val dButList = ArrayList<Button>(7)
        dButList.add(p1DBut)
        dButList.add(p2DBut)
        dButList.add(p3DBut)
        dButList.add(p4DBut)
        dButList.add(p5DBut)
        dButList.add(p6DBut)
        dButList.add(p7DBut)

        val stallButList = ArrayList<Button>(7)
        stallButList.add(p1StallBut)
        stallButList.add(p2StallBut)
        stallButList.add(p3StallBut)
        stallButList.add(p4StallBut)
        stallButList.add(p5StallBut)
        stallButList.add(p6StallBut)
        stallButList.add(p7StallBut)

        val callahanButList = ArrayList<Button>(7)
        callahanButList.add(p1CallahanBut)
        callahanButList.add(p2CallahanBut)
        callahanButList.add(p3CallahanBut)
        callahanButList.add(p4CallahanBut)
        callahanButList.add(p5CallahanBut)
        callahanButList.add(p6CallahanBut)
        callahanButList.add(p7CallahanBut)


        //### Create Button listeners ###

        //Special Buttons
        oppGoalBut.setOnClickListener {
            dataPasser.newLineOffense()
        }

        oppThrowawayBut.setOnClickListener {
            dataPasser.switchPossession(players)
        }


        //D buttons
        for(dBut in dButList)
        {
            dBut.setOnClickListener {
                val thisIndex = dButList.indexOf(dBut)
                passData(players[thisIndex], "D")
                dataPasser.switchPossession(players)
            }
        }

        //Drop buttons
        for(stallBut in stallButList)
        {
            stallBut.setOnClickListener {
                val thisIndex = stallButList.indexOf(stallBut)
                passData(players[thisIndex], "stall")
                dataPasser.switchPossession(players)
            }
        }

        //Goal buttons
        for(callahanBut in callahanButList)
        {
            callahanBut.setOnClickListener {
                val thisIndex = callahanButList.indexOf(callahanBut)
                passData(players[thisIndex], "callahan")
                dataPasser.newLineDefense()
            }
        }
    }
}