package com.example.test2

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test2.databinding.ActivityChatRoomBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_chat_room.*
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.coroutines.NonCancellable.cancel
import kotlin.concurrent.timer

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
    var master : String? = null

    override fun onBackPressed() {
        val time1 = System.currentTimeMillis()
        val time2 = time1 - time3

        val flag = application as FlagClass
        val roomNum=flag.getRoomNum()
        val curEmail=flag.getEmail()


        if (time2 in 0..2000) {


            if(master!=curEmail){


                //방장이 아닐 시

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
                    chatDb.child(roomNum.toString()).child("contents").push().setValue(Chat("${it.value.toString()}님이 퇴장하였습니다",
                        "관리자",
                        "관리자",
                        -1
                    ))
                }


                val intent = Intent(this, afterLoginActivity::class.java)
                startActivity(intent)
                finish()
            }else{

                //방장일시

                ActivityCompat.finishAffinity(this)
                System.exit(0)
            }


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
        val curEmail = flag.getEmail()

        email = flag.getEmail().toString()

        roomDb=FirebaseDatabase.getInstance().getReference("Rooms")
        roomDb.child(roomNum.toString()).get().addOnSuccessListener {
            binding.tvRoomTitle.setText(it.child("title").value.toString())
            master=it.child("master").value.toString()

        }


        chatDb = FirebaseDatabase.getInstance().getReference("Chat")
        chatRecyclerView=findViewById(R.id.chat_recycler)

        messageList=ArrayList()
        messageAdapter= ChatRecyclerAdapter(email.toString(),this@ChatRoomActivity,messageList)

        chatRecyclerView.layoutManager=LinearLayoutManager(this)
        chatRecyclerView.adapter=messageAdapter

        //나가지는 거 구현
        chatDb.child(roomNum.toString()).child("onGoing").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.value==false){
                    Toast.makeText(this@ChatRoomActivity,"방이 종료되었습니다",Toast.LENGTH_SHORT).show()
                    val userDb=FirebaseDatabase.getInstance().getReference("Users")
                    val roomDb=FirebaseDatabase.getInstance().getReference("Rooms")

                    userDb.child(email.toString()).child("roomNum").setValue(-1)
                    roomDb.child(roomNum.toString()).child("players").setValue(0)

                    val intent=Intent(this@ChatRoomActivity,afterLoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }
        )
//


        chatDb.child(roomNum.toString()).child("contents").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {


                    messageList.clear()
                    for( chatSnapshot in snapshot.children) {
                        val chat = chatSnapshot.getValue(Chat::class.java)
                        messageList.add(chat!!)
                    }
                chatRecyclerView.scrollToPosition(messageAdapter.getItemCount() - 1)

                messageAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        )

        /*
        val rotateCw : Animation = AnimationUtils.loadAnimation(this,R.anim.rotate_cw)
        val rotateCcw : Animation = AnimationUtils.loadAnimation(this,R.anim.rotate_ccw)
        rotateCcw.fillAfter=true
        rotateCcw.isFillEnabled=true
        rotateCw.fillAfter=true
        rotateCw.isFillEnabled=true
        binding.btnSend.startAnimation(rotateCw)

        */


        binding.etMessage.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val i=1
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(p0.toString()!=""){
                    binding.btnSend.backgroundTintList= ColorStateList.valueOf(Color.parseColor("#189AB4"))
                    //binding.btnSend.startAnimation(rotateCcw)
                }else{

                    binding.btnSend.backgroundTintList= ColorStateList.valueOf(Color.parseColor("#868B8E"))
                    //binding.btnSend.startAnimation(rotateCw)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                val i=1
            }


        }

        )



        binding.btnSend.setOnClickListener {
            val message: String = binding.etMessage.text.toString()
            val flag = application as FlagClass
            val email = flag.getEmail()
            binding.etMessage.setText("")


            if (message.isNotEmpty()) {

                if (email == master && message=="게임시작") {

                    chatDb=FirebaseDatabase.getInstance().getReference("Chat")
                    var  second : Int=6

                    timer(period=1000 , initialDelay = 1000){

                        if(second==1)
                            cancel()

                        second--
                        chatDb.child(roomNum.toString()).child("contents").push().setValue(Chat("게임 시작 전 : ${second}","관리자",
                        "관리자",-1))

                    }

                        chatDb.child(roomNum.toString()).child("contents").push().setValue(Chat("게임을 시작합니다!","관리자",
                            "관리자",-1))






                }else if(email == master && message=="게임종료"){

                    chatDb=FirebaseDatabase.getInstance().getReference("Chat")

                    chatDb.child(roomNum.toString()).child("contents").push().setValue(Chat("게임을 종료합니다!","관리자",
                        "관리자",-1))

                    chatDb.child(roomNum.toString()).child("onGoing").setValue(false)


                } else {
                    val userDb = FirebaseDatabase.getInstance().getReference("Users")

                    userDb.child(email.toString()).get().addOnSuccessListener {

                        val userName = it.child("userName").value.toString()
                        val charNum = it.child("character").value.toString().toInt()

                        val chat = Chat(message, userName, email, charNum)

                        chatDb = FirebaseDatabase.getInstance().getReference("Chat")

                        chatDb.child(roomNum.toString()).child("contents").push().setValue(chat)


                    }
                }
            }
        }






    }

}

