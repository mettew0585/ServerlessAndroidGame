package com.example.test2

import android.app.Application

class RoomNumFlag : android.app.Application() {
    private var roomNum: Int? = null

    fun getRoomNum(): Int? {
        return roomNum
    }

    fun setRoomNum(roomNum: Int?) {
        this.roomNum = roomNum
    }
}