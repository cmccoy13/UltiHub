package com.example.christopher.ultihub

import android.os.Parcel
import android.os.Parcelable

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
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(number)
        parcel.writeString(position)
        parcel.writeByte(if (captain) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Player> {
        override fun createFromParcel(parcel: Parcel): Player {
            return Player(parcel)
        }

        override fun newArray(size: Int): Array<Player?> {
            return arrayOfNulls(size)
        }
    }
}