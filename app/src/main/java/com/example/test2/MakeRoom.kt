package com.example.test2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.test2.databinding.ActivityMakeRoomBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MakeRoom : AppCompatActivity() {

    private lateinit var db: DatabaseReference
    private lateinit var binding:ActivityMakeRoomBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_make_room)

        val flag = application as FlagClass
        val makeBtn=findViewById<Button>(R.id.makeBtn)

        makeBtn.setOnClickListener{

            val title=findViewById<TextView>(R.id.tv_title).text.toString()
            val password=findViewById<TextView>(R.id.tv_password).text.toString()

            if(title.isEmpty()||password.isEmpty()){
                Toast.makeText(this,"방 제목을 입력해 주세요",Toast.LENGTH_SHORT).show()
            }else{

                val fdb=FirebaseDatabase.getInstance()

                fdb.getReference("roomCount").get().addOnSuccessListener {
                    val num=it.value.toString()
                    val flag=application as FlagClass
                    val email=flag.getEmail()
                    flag.setRoomNum(num.toInt())

                    val room = Room(num.toInt(),1,title,password,email)

                    fdb.getReference("Users").child(email.toString()).child("roomNum").setValue(num)
                    fdb.getReference("Rooms").child(num).setValue(room)
                    fdb.getReference("Rooms").child(num).child("emails").child(email.toString())
                        .setValue(email.toString())

                    fdb.getReference("roomCount").setValue(num.toInt()+1)
                    //
                    val chatDb=FirebaseDatabase.getInstance().getReference("Chat")
                    chatDb.child(num.toString()).push().setValue(Chat("방이 열렸습니다","관리자","관리자",-1))


                    //


                    val intent = Intent(this, ChatRoomActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }

            }
        }

    }
}

