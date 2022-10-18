package com.example.test2

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test2.databinding.ActivityChatRoomBinding
import com.google.firebase.database.*

class ChatRoomActivity : AppCompatActivity() {


    private lateinit var messageAdapter: ChatRecyclerAdapter
    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var chatDb: DatabaseReference
    private lateinit var roomDb: DatabaseReference
    private lateinit var userDb: DatabaseReference
    private lateinit var messageList: ArrayList<Chat>
    private lateinit var binding: ActivityChatRoomBinding
    var email: String? = null
    var time3: Long = 0
    var master: String? = null
    var mapString =
        "01/01/01@naver.com##################" +
                "02/02/2@naver.com###################" +
                "03/03/03@naver.com##################" +
                "04/04/4@naver.com###################" +
                "05/05/05@naver.com##################" +
                "06/06/06@naver.com##################" +
                "07/07/07@naver.com##################" +
                "08/08/08@naver.com##################" +
                "09/09/09@naver.com##################" +
                "10/10/10@naver.com##################" +
                "11/11/11@naver.com##################" +
                "12/12/12@naver.com##################" +
                "13/13/13@naver.com##################" +
                "14/14/14@naver.com##################" +
                "15/15/15@naver.com##################" +
                "16/16/16@naver.com##################" +
                "17/17/17@naver.com##################" +
                "18/18/18@naver.com##################" +
                "19/19/19@naver.com##################" +
                "20/20/20@naver.com##################" +
                "21/21/21@naver.com##################" +
                "22/22/22@naver.com##################" +
                "23/23/23@naver.com##################" +
                "24/24/24@naver.com##################" +
                "25/25/25@naver.com##################"

    override fun onBackPressed() {
        val time1 = System.currentTimeMillis()
        val time2 = time1 - time3

        val flag = application as FlagClass
        val roomNum = flag.getRoomNum()
        val curEmail = flag.getEmail()


        if (time2 in 0..2000) {


            if (master != curEmail) {


                //방장이 아닐 시

                //user처리
                userDb = FirebaseDatabase.getInstance().getReference("Users")
                userDb.child(email.toString()).child("roomNum").setValue(-1)
                //room처리
                roomDb = FirebaseDatabase.getInstance().getReference("Rooms")
                roomDb.child(roomNum.toString()).child("players").get().addOnSuccessListener {
                    FirebaseDatabase.getInstance().getReference("Rooms").child(roomNum.toString())
                        .child("players")
                        .setValue(it.value.toString().toInt() - 1)
                }
                //room처리
                roomDb.child(roomNum.toString()).child("emails").child(email.toString())
                    .removeValue()
                ///퇴장 메시지
                chatDb = FirebaseDatabase.getInstance().getReference("Chat")


                userDb.child(email.toString()).child("userName").get().addOnSuccessListener {
                    chatDb.child(roomNum.toString()).child("contents").push().setValue(
                        Chat(
                            "${it.value.toString()}님이 퇴장하였습니다",
                            "관리자",
                            "관리자",
                            -1
                        )
                    )
                }


                val intent = Intent(this, afterLoginActivity::class.java)
                startActivity(intent)
                finish()


            } else {

                //방장일시

                ActivityCompat.finishAffinity(this)
                System.exit(0)
            }


        } else {
            time3 = time1
            Toast.makeText(applicationContext, "한번 더 누르면 방을 나갑니다.", Toast.LENGTH_SHORT).show()
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

        roomDb = FirebaseDatabase.getInstance().getReference("Rooms")

        roomDb.child(roomNum.toString())
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.child("gameStarted").value == true) {
                        if(snapshot.child("master").value != email) {
                            val intent = Intent(this@ChatRoomActivity, MapActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            }

            )


        roomDb.child(roomNum.toString()).get().addOnSuccessListener {

            binding.tvRoomTitle.setText(it.child("title").value.toString())
            master = it.child("master").value.toString()
        }


        chatDb = FirebaseDatabase.getInstance().getReference("Chat")
        chatRecyclerView = findViewById(R.id.chat_recycler)

        messageList = ArrayList()
        messageAdapter = ChatRecyclerAdapter(email.toString(), this@ChatRoomActivity, messageList)

        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter = messageAdapter

        //나가지는 거 구현
        chatDb.child(roomNum.toString()).child("onGoing")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    if (snapshot.value == false) {

                        Toast.makeText(this@ChatRoomActivity, "방이 종료되었습니다", Toast.LENGTH_SHORT)
                            .show()

                        val userDb = FirebaseDatabase.getInstance().getReference("Users")
                        val roomDb = FirebaseDatabase.getInstance().getReference("Rooms")

                        userDb.child(email.toString()).child("roomNum").setValue(-1)
                        roomDb.child(roomNum.toString()).child("players").setValue(0)

                        val intent = Intent(this@ChatRoomActivity, afterLoginActivity::class.java)
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


        chatDb.child(roomNum.toString()).child("contents")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {


                    messageList.clear()
                    for (chatSnapshot in snapshot.children) {
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


        binding.etMessage.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val i = 1
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString() != "") {
                    binding.btnSend.backgroundTintList =
                        ColorStateList.valueOf(Color.parseColor("#189AB4"))
                    //binding.btnSend.startAnimation(rotateCcw)
                } else {

                    binding.btnSend.backgroundTintList =
                        ColorStateList.valueOf(Color.parseColor("#868B8E"))
                    //binding.btnSend.startAnimation(rotateCw)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                val i = 1
            }


        }

        )



        binding.btnSend.setOnClickListener {
            val message: String = binding.etMessage.text.toString()
            val flag = application as FlagClass
            val email = flag.getEmail()
            binding.etMessage.setText("")


            if (message.isNotEmpty()) {

                if (email == master && message == "게임시작") {

                    chatDb = FirebaseDatabase.getInstance().getReference("Chat")
                    roomDb = FirebaseDatabase.getInstance().getReference("Rooms")

                    var second: Int = 6
                    val dlg = LandNumChoiceDialog(this@ChatRoomActivity)

                    val intent = Intent(this, MapActivity::class.java)

                    dlg.setOnOKClickedListener { content ->


                                intent.putExtra("start", "")

                                roomDb.child(flag.getRoomNum().toString()).child("mapString").setValue(mapString)
                                startActivity(intent)
                                finish()

                                overridePendingTransition(0, 0)


                    }

                    if (roomNum != null) {
                        dlg.start(roomNum)
                    }

                } else if (email == master && message == "게임종료") {

                    chatDb = FirebaseDatabase.getInstance().getReference("Chat")

                    chatDb.child(roomNum.toString()).child("contents").push()
                        .setValue(Chat("게임을 종료합니다!", "관리자", "관리자", -1))

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


/*
timer(period = 1000, initialDelay = 1000) {

    if (second == 1)
        cancel()

    second--

    chatDb.child(roomNum.toString()).child("contents").push().setValue(
        Chat(
            "게임 시작 전 : ${second}", "관리자",
            "관리자", -1
        )
    )

    if (second == 0) {
            chatDb.child(roomNum.toString()).child("contents").push()
                .setValue(
                    Chat(
                        "게임을 시작합니다!", "관리자",
                        "관리자", -1
                    )
                ).addOnSuccessListener {
                    roomDb.child(roomNum.toString()).child("gameStarted").setValue(true).addOnSuccessListener {
                        intent.putExtra("start", "")
                        startActivity(intent)
                        finish()
                    }
                }
    }
}
*/
