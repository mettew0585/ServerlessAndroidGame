package com.example.test2

import android.app.Application
import android.media.Image

class FlagClass : android.app.Application() {
    private var email: String? = null
    private var roomNum : Int? = null
    private var x : Int?= null
    private var y : Int?= null
    private var skill1 : Boolean?=false
    var brd= Array<Array<Int>>(40,{Array(30,{-1})})

    val images = arrayOf(
        R.drawable.character1,
        R.drawable.character2,
        R.drawable.character3,
        R.drawable.character4,
        R.drawable.character5
    )




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

    fun getX():Int?{
        return x
    }

    fun setX(x : Int?){
        this.x=x
    }

    fun getY():Int?{
        return y
    }

    fun setY(y : Int?){
        this.y=y
    }

    fun setSkill1(y:Boolean?){
        this.skill1=y
    }

    fun getSkill1(): Boolean? {
       return this.skill1
    }
}