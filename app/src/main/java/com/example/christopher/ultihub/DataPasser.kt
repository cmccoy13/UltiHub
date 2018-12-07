package com.example.christopher.ultihub

interface DataPasser {
    fun passData(player : String, action : String)
    fun switchPossession(players : ArrayList<String>)
    fun newLineDefense()
    fun newLineOffense()
}