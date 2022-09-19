package com.example.test2

import android.app.Application

class FlagClass : android.app.Application() {
    private var email: String? = null
    private var roomNum : Int? = null

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