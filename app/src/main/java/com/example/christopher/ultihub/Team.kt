package com.example.christopher.ultihub

import java.util.*
import kotlin.collections.HashMap

data class TeamResponse(
        val name : String = "",
        val tournaments : HashMap<String, Objects> = HashMap<String, Objects>(),
        val players : HashMap<String, Objects> = HashMap<String, Objects>()
)

fun TeamResponse.mapToTeam() = Team(name, tournaments, players)

data class Team(
    val name : String,
    val tournaments : HashMap<String, Objects>,
    val players : HashMap<String, Objects>
    )