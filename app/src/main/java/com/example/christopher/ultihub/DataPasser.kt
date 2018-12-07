package com.example.christopher.ultihub

interface DataPasser {
    fun passData(player : String, action : String)
    fun beginPoint(value : ArrayList<String>)
    fun switchPossession(players : ArrayList<String>)
    fun toOffense(pickupIndex : Int, players : ArrayList<String>)
    fun newLineDefense()
    fun newLineOffense()
}