package com.example.christopher.ultihub

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class PickupFragment : Fragment() {
    lateinit var dataPasser : DataPasser
    lateinit var players : ArrayList<String>

    companion object {
        fun newInstance(playerList: ArrayList<String>): PickupFragment {

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
            val fragment = PickupFragment()
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
        val view = inflater?.inflate(R.layout.fragment_pickup, container, false)

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
        val p1 = view.findViewById(R.id.p1Text) as TextView
        val p2 = view.findViewById(R.id.p2Text) as TextView
        val p3 = view.findViewById(R.id.p3Text) as TextView
        val p4 = view.findViewById(R.id.p4Text) as TextView
        val p5 = view.findViewById(R.id.p5Text) as TextView
        val p6 = view.findViewById(R.id.p6Text) as TextView
        val p7 = view.findViewById(R.id.p7Text) as TextView

        val nameList = ArrayList<TextView>(7)
        nameList.add(p1)
        nameList.add(p2)
        nameList.add(p3)
        nameList.add(p4)
        nameList.add(p5)
        nameList.add(p6)
        nameList.add(p7)

        for(name in nameList)
        {
            name.setOnClickListener {
                val thisIndex = nameList.indexOf(name)
                dataPasser.toOffense(thisIndex, players)
            }
        }
    }
}