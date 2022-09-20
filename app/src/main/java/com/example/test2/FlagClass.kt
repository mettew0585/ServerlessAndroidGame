package com.example.test2

import android.app.Application
import android.media.Image

class FlagClass : android.app.Application() {
    private var email: String? = null
    private var roomNum : Int? = null
    private val imgArray : Array<Int> = arrayOf( R.drawable.character1,
        R.drawable.character2,
        R.drawable.character3,
        R.drawable.character4,
        R.drawable.character5
        )


    fun getEmail(): String? {
        return email
    }

    fun setEmail(id: String?) {
        this.email = id
    }


    fun getRoomNum(): Int? {
        return roomNum
    }

    fun setRoomNum(roomNum : Int?) {
        this.roomNum=roomNum
    }

}