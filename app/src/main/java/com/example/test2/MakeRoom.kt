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
                val fdb2=FirebaseDatabase.getInstance()

                fdb.getReference("roomCount").get().addOnSuccessListener {
                    val num=it.value.toString()
                    val flag=application as FlagClass
                    val email=flag.getEmail().toString()
                    flag.setRoomNum(num.toInt())
                    val room = Room(num.toInt(),1,title,password,email,2,false,-1)
                    val playerInfo = PlayerInfo(email,false,"-1",-1,-1,1)

                    fdb.getReference("Users").child(email).child("roomNum").setValue(num)
                    fdb.getReference("Users").child(email).child("userId").setValue(1)
                    //user데이터베이스 업데이트
                    fdb.getReference("Rooms").child(num).setValue(room)
                    //rooms데이터베이스 업데이트
                    fdb.getReference("Rooms").child(num).child("emails").child("1").setValue(playerInfo)
                    //플레이어마다 고유번호 저장
                    fdb.getReference("roomCount").setValue(num.toInt()+1)
                    //룸카운트 늘리기


                    val chatDb=FirebaseDatabase.getInstance().getReference("Chat")
                    val userDb=FirebaseDatabase.getInstance().getReference("Users")
                    userDb.child(email.toString()).child("userName").get().addOnSuccessListener {

                        chatDb.child(num.toString()).child("contents").push().setValue(Chat("방이 열렸습니다! \n방장 :${it.value.toString()}","관리자","관리자",-1))
                        chatDb.child(num.toString()).child("onGoing").setValue(true)
                    }


                    val intent = Intent(this, ChatRoomActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }

            }
        }

    }
}

