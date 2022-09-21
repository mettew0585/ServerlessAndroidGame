package com.example.test2

import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test2.databinding.ActivityChatRoomBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_signup.*

class ChatRoomActivity : AppCompatActivity(){


    private lateinit var messageAdapter: ChatRecyclerAdapter
    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var chatDb: DatabaseReference
    private lateinit var roomDb:DatabaseReference
    private lateinit var userDb:DatabaseReference
    private lateinit var messageList: ArrayList<Chat>
    private lateinit var binding: ActivityChatRoomBinding

    var email: String? = null

    var time3: Long = 0
    override fun onBackPressed() {
        val time1 = System.currentTimeMillis()
        val time2 = time1 - time3
        if (time2 in 0..2000) {

            val flag = application as FlagClass
            val roomNum=flag.getRoomNum()
            //user처리
            userDb=FirebaseDatabase.getInstance().getReference("Users")
            userDb.child(email.toString()).child("roomNum").setValue(-1)
            //room처리
            roomDb=FirebaseDatabase.getInstance().getReference("Rooms")
            roomDb.child(roomNum.toString()).child("players").get().addOnSuccessListener {
                FirebaseDatabase.getInstance().getReference("Rooms").child(roomNum.toString()).child("players")
                    .setValue(it.value.toString().toInt()-1)
            }
            //room처리
            roomDb.child(roomNum.toString()).child("emails").child(email.toString()).removeValue()
            ///퇴장 메시지
            chatDb=FirebaseDatabase.getInstance().getReference("Chat")


            userDb.child(email.toString()).child("userName").get().addOnSuccessListener {
                chatDb.child(roomNum.toString()).push().setValue(Chat("${it.value.toString()}님이 퇴장하였습니다",
                    "관리자",
                    "관리자",
                    -1
                ))
            }

            val intent=Intent(this,afterLoginActivity::class.java)
            startActivity(intent)
            finish()

            finish()
        }
        else {
            time3 = time1
            Toast.makeText(applicationContext, "한번 더 누르면 방을 나갑니다.",Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room)



        binding = ActivityChatRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val flag = application as FlagClass
        val roomNum = flag.getRoomNum()
        email = flag.getEmail().toString()

        roomDb=FirebaseDatabase.getInstance().getReference("Rooms")
        roomDb.child(roomNum.toString()).child("title").get().addOnSuccessListener {
            binding.tvRoomTitle.setText(it.value.toString())
        }


        chatDb = FirebaseDatabase.getInstance().getReference("Chat")
        chatRecyclerView=findViewById(R.id.chat_recycler)

        messageList=ArrayList()
        messageAdapter= ChatRecyclerAdapter(email.toString(),this@ChatRoomActivity,messageList)

        chatRecyclerView.layoutManager=LinearLayoutManager(this)
        chatRecyclerView.adapter=messageAdapter
//


        chatDb.child(roomNum.toString()).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {


                    messageList.clear()
                    for( chatSnapshot in snapshot.children) {
                        val chat = chatSnapshot.getValue(Chat::class.java)
                        messageList.add(chat!!)
                    }
                chatRecyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1)

                messageAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        )


        //

        binding.btnExit.setOnClickListener{
            //user처리
            userDb=FirebaseDatabase.getInstance().getReference("Users")
            userDb.child(email.toString()).child("roomNum").setValue(-1)
            //room처리
            roomDb=FirebaseDatabase.getInstance().getReference("Rooms")
            roomDb.child(roomNum.toString()).child("players").get().addOnSuccessListener {
                FirebaseDatabase.getInstance().getReference("Rooms").child(roomNum.toString()).child("players")
                    .setValue(it.value.toString().toInt()-1)
            }
            //room처리
            roomDb.child(roomNum.toString()).child("emails").child(email.toString()).removeValue()
            ///퇴장 메시지
            chatDb=FirebaseDatabase.getInstance().getReference("Chat")


            userDb.child(email.toString()).child("userName").get().addOnSuccessListener {
                chatDb.child(roomNum.toString()).push().setValue(Chat("${it.value.toString()}님이 퇴장하였습니다",
                    "관리자",
                    "관리자",
                    -1
                ))
            }

            val intent=Intent(this,afterLoginActivity::class.java)
            startActivity(intent)
            finish()
        }










//

        binding.btnSend.setOnClickListener {
            val message: String = binding.etMessage.text.toString()
            val flag = application as FlagClass
            val email = flag.getEmail()
            binding.etMessage.setText("")


            if (message.isNotEmpty()) {

                val userDb = FirebaseDatabase.getInstance().getReference("Users")

                userDb.child(email.toString()).get().addOnSuccessListener {

                    val userName = it.child("userName").value.toString()
                    val charNum = it.child("character").value.toString().toInt()

                    val chat = Chat(message, userName, email, charNum)

                    chatDb = FirebaseDatabase.getInstance().getReference("Chat")

                    chatDb.child(roomNum.toString()).push().setValue(chat)

                }
            }
        }






    }

}

