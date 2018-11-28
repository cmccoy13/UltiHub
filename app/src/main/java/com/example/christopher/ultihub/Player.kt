package com.example.christopher.ultihub

data class PlayerResponse(
    val name : String = "",
    val number : Int? = null,
    val position : String? = null
)

fun PlayerResponse.mapToPlayer() = Player(name, number, position)

data class Player(
    val name : String = "",
    val number : Int? = 0,
    val position : String? = ""
)