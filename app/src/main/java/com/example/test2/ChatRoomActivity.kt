package com.example.test2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test2.databinding.ActivityChatRoomBinding
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_signup.*

class ChatRoomActivity : AppCompatActivity() {

    private lateinit var adapter: ChatRecyclerAdapter
    private lateinit var chatRecyclerView : RecyclerView
    private lateinit var chatArrayList : ArrayList<Chat>
    private lateinit var chatDb : DatabaseReference
    private lateinit var binding : ActivityChatRoomBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room)

        val flag = application as FlagClass
        val roomNum=flag.getRoomNum()
        val layoutManager=LinearLayoutManager(this)
        chatDb=FirebaseDatabase.getInstance().getReference("Chat")



        chatDb.child(roomNum.toString()).child("contents").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if(snapshot.exists()){

                    val chatList =ArrayList<Chat>()
                    for( chatSnapshot in snapshot.children){
                        val chat=chatSnapshot.getValue(Chat::class.java)

                        if(chat!=null){
                            chatList.add(chat)
                        }
                    }

                    chatRecyclerView=findViewById(R.id.chat_recycler)
                    chatRecyclerView.setHasFixedSize(true)
                    adapter= ChatRecyclerAdapter(chatList)
                    chatRecyclerView.adapter=adapter

                    var adapter=ChatRecyclerAdapter(chatList)

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        )
        /*
        binding.btnSend.setOnClickListener{
            val message : String = binding.etMessage.text.toString()
            val flag = application as FlagClass
            val email = flag.getEmail()

            if(message.isNotEmpty()){

                val userDb=FirebaseDatabase.getInstance().getReference("Users")

                userDb.child(email.toString()).get().addOnSuccessListener {

                    if(it.exists()){
                        val userName = it.child("password").value.toString()
                        val charNum= it.child("character").value.toString().toInt()

                        val chat = Chat(message,userName,email,charNum)

                        chatDb=FirebaseDatabase.getInstance().getReference("Chat")
                        chatDb.child(roomNum.toString()).get().addOnSuccessListener { snapshot->
                            if(snapshot.exists()){
                                val chatCount = snapshot.child("chatCount").value.toString().toInt()

                                val chatDb1=FirebaseDatabase.getInstance().getReference("Chat")

                                chatDb1.child("chatCount").setValue(chatCount+1)
                                chatDb.child(roomNum.toString()).child("contents").child((chatCount+1).toString())
                                    .setValue(chat)
                            }
                        }
                    }
                }
            }
        }
*/
    }

}