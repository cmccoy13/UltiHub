package com.example.christopher.ultihub

data class TeamResponse(
    val name : String = "",
    val tournaments : ArrayList<Tournament> = ArrayList<Tournament>(),
    val players : ArrayList<Player> = ArrayList<Player>()
)

fun TeamResponse.mapToTeam() = Team(name, tournaments, players)

data class Team(
    val name : String,
    val tournaments : ArrayList<Tournament>,
    val players : ArrayList<Player>
    )