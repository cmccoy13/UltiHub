package com.example.christopher.ultihub

data class PlayerResponse(
    val name : String = "",
    val number : String? = null,
    val position : String? = null,
    val captain : Boolean = false
)

fun PlayerResponse.mapToPlayer() = Player(name, number, position, captain)

data class Player(
    val name : String = "",
    val number : String? = "",
    val position : String? = "",
    val captain : Boolean = false
)