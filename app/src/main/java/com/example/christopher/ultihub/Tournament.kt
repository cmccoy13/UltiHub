package com.example.christopher.ultihub

data class TournamentResponse(
    val name : String = "",
    val location : String = ""
)

fun TournamentResponse.mapToTournament() = Tournament(name, location)

data class Tournament(
    val name : String = "",
    val location : String = ""
)