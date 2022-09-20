package com.example.test2

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test2.databinding.ActivityChatRoomBinding
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_signup.*

class ChatRoomActivity : AppCompatActivity(){

    private lateinit var messageAdapter: ChatRecyclerAdapter
    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var chatDb: DatabaseReference
    private lateinit var messageList: ArrayList<Chat>
    private lateinit var binding: ActivityChatRoomBinding

    var email: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room)


        binding = ActivityChatRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val flag = application as FlagClass
        val roomNum = flag.getRoomNum()
        email = flag.getEmail().toString()

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
                messageAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        )











//

        binding.btnSend.setOnClickListener {
            val message: String = binding.etMessage.text.toString()
            val flag = application as FlagClass
            val email = flag.getEmail()

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

