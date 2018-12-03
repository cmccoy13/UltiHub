package com.example.christopher.ultihub

data class GameResponse(
    val title : String = "",
    val opponent : String = "",
    val startDate : String = "",
    val maxScore : Int = -1,
    val softCap : Int = -1,
    val hardCap : Int = -1,
    val ourTOsFirstHalf : Int = 1,
    val ourTOsSecondHalf : Int = 1,
    val ourTOsFloater : Int = 1,
    val oppTOsFirstHalf : Int = 1,
    val oppTOsSecondHalf : Int = 1,
    val oppTOsFloater : Int = 1,
    val onOffense : Boolean = true,
    val ourScore : Int = 0,
    val oppScore : Int = 0,
    val gameFinished : Boolean = false
)

fun GameResponse.mapToGame() = Game(title, opponent, startDate, maxScore, softCap, hardCap,
    ourTOsFirstHalf, ourTOsSecondHalf, ourTOsFloater, oppTOsFirstHalf, oppTOsSecondHalf, oppTOsFloater,
    onOffense, ourScore, oppScore, gameFinished)

data class Game(
    val title : String,
    val opponent : String,
    val startDate : String,
    val maxScore : Int,
    val softCap : Int,
    val hardCap : Int,
    val ourTOsFirstHalf : Int,
    val ourTOsSecondHalf : Int,
    val ourTOsFloater : Int,
    val oppTOsFirstHalf : Int,
    val oppTOsSecondHalf : Int,
    val oppTOsFloater : Int,
    val onOffense : Boolean,
    val ourScore : Int,
    val oppScore : Int,
    val gameFinished: Boolean
)