package com.example.test2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.test2.databinding.ActivityEnterRoomBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class EnterRoomActivity : AppCompatActivity() {

    private lateinit var binding : ActivityEnterRoomBinding

    private lateinit var db : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter_room)

        val flag=application as FlagClass
        val roomNum=flag.getRoomNum()

        binding= ActivityEnterRoomBinding.inflate(layoutInflater)


        db=FirebaseDatabase.getInstance().getReference("Rooms")
        db.child(roomNum.toString()).child("title").get().addOnSuccessListener {
           Toast.makeText(this,it.value.toString(),Toast.LENGTH_SHORT).show()

            binding.tvRoomTitle.text=it.value.toString()

        }

    }
}