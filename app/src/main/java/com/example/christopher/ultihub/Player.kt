package com.example.christopher.ultihub

import android.os.Parcel
import android.os.Parcelable

data class PlayerResponse(
    val name : String = "",
    val number : String? = null,
    val position : String? = null,
    val captain : Boolean = false,
    val Ds : Int = 0,
    val assists : Int = 0,
    val callahans : Int = 0,
    val callahansThrown : Int = 0,
    val catches : Int = 0,
    val drops : Int = 0,
    val goals : Int = 0,
    val passes : Int = 0,
    val stalled : Int = 0,
    val stalls : Int = 0,
    val throwaways : Int = 0
)

fun PlayerResponse.mapToPlayer() = Player(name, number, position, captain, Ds, assists, callahans, callahansThrown,
                                            catches, drops, goals, passes, stalled, stalls, throwaways)

data class Player(
    val name : String = "",
    val number : String? = null,
    val position : String? = null,
    val captain : Boolean = false,
    val Ds : Int = 0,
    val assists : Int = 0,
    val callahans : Int = 0,
    val callahansThrown : Int = 0,
    val catches : Int = 0,
    val drops : Int = 0,
    val goals : Int = 0,
    val passes : Int = 0,
    val stalled : Int = 0,
    val stalls : Int = 0,
    val throwaways : Int = 0
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