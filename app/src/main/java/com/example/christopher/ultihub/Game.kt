package com.example.christopher.ultihub

data class GameResponse(
    val name : String = "",
    val opponent : String = "",
    val maxScore : String = "",
    val startTime : String = "",
    val softCap : String = "",
    val hardCap : String = "",
    val timeoutsPerHalf : String = "",
    val timeoutsFloater : String = ""
)

fun GameResponse.mapToGame() = Game(name, opponent, maxScore, startTime, softCap, hardCap, timeoutsPerHalf, timeoutsFloater)

data class Game(
    val name : String = "",
    val opponent : String = "",
    val maxScore : String = "",
    val startTime : String = "",
    val softCap : String = "",
    val hardCap : String = "",
    val timeoutsPerHalf : String = "",
    val timeoutsFloater : String = ""
)