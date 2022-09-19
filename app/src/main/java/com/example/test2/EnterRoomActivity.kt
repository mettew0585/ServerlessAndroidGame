package com.example.test2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase

class EnterRoomActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter_room2)

        val roomNumFlag = RoomNumFlag()
        val roomNum=roomNumFlag.getRoomNum()

        Toast.makeText(this,roomNum.toString(),Toast.LENGTH_SHORT).show()




    }
}