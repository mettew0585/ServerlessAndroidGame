package com.example.test2

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class Fragment1 : Fragment()  {

    private lateinit var roomdb : DatabaseReference
    private lateinit var rrcv : RecyclerView
    private lateinit var plusBtn : Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view=inflater.inflate(R.layout.after_login_frag1,container,false)

        rrcv=view.findViewById(R.id.room_view)
        rrcv.layoutManager=LinearLayoutManager(activity)
        rrcv.setHasFixedSize(true)
        roomdb=FirebaseDatabase.getInstance().getReference("Rooms")

        roomdb.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                if(snapshot.exists()){
                    val roomLis=ArrayList<Room>()
                    for(roomSnapShot in snapshot.children){
                        val room=roomSnapShot.getValue(Room::class.java)

                        if (room != null) {
                            if(room.players!! >0)
                                roomLis.add(room!!)
                        }

                        val k=3
                    }

                    var adapter=RoomRecyclerAdapter(roomLis)
                    rrcv.adapter=adapter
                    adapter.setOnItemClickListener(object : RoomRecyclerAdapter.onItemClickListener{

                        override fun onItemClick(position: Int) {

                            val roomNum=roomLis[position].roomNum
                            val flag= activity?.application as FlagClass
                            flag.setRoomNum(roomNum)
                            val builder: AlertDialog.Builder = android.app.AlertDialog.Builder(activity)
                            val input = EditText(activity)
                            input.setHint("비밀번호")
                            input.inputType =
                                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

                            val dialog=builder.setView(input)
                            .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                                // Here you get get input text from the Edittext
                                var m_Text = input.text.toString()

                                roomdb.child(roomNum.toString()).child("players").get().addOnSuccessListener {
                                    val players=it.value.toString().toInt()

                                    if(players==0){
                                        Toast.makeText(activity,"존재하지 않는 방입니다!",Toast.LENGTH_SHORT).show()
                                    }else{

                                        roomdb.child(roomNum.toString()).child("password").get().addOnSuccessListener {

                                        if(m_Text==it.value.toString()){ //패스워드 일치시

                                            val flag = activity?.application as FlagClass
                                            val email= flag.getEmail().toString()
                                            val roomdb1=FirebaseDatabase.getInstance().getReference("Rooms")

                                            roomdb1.child(roomNum.toString()).child("players").get().addOnSuccessListener {
                                                val p=it.value.toString().toInt()
                                                val roomdb2=FirebaseDatabase.getInstance().getReference("Rooms")
                                                val roomdb3=FirebaseDatabase.getInstance().getReference("Rooms")
                                                val chatDb=FirebaseDatabase.getInstance().getReference("Chat")
                                                val userDb=FirebaseDatabase.getInstance().getReference("Users")

                                                roomdb2.child(roomNum.toString()).child("sum").get().addOnSuccessListener {
                                                    val s=it.value.toString().toInt()
                                                    val playerInfo = PlayerInfo(email,false,"-1",-1,-1,s)

                                                    userDb.child(email).child("userId").setValue(s)
                                                    flag.setUserId(s)
                                                    //유저 데이터베이스 업데이트

                                                    roomdb3.child(roomNum.toString()).child("players").setValue(p+1)
                                                    //룸 데이터베이스 플레이어수 업데이트
                                                    roomdb3.child(roomNum.toString()).child("emails").child(s.toString()).setValue(playerInfo)
                                                    //
                                                    roomdb3.child(roomNum.toString()).child("sum").setValue(s+1)

                                                    userDb.child(email.toString()).child("userName").get().addOnSuccessListener {

                                                        chatDb.child(roomNum.toString()).child("contents").push().setValue(
                                                            Chat("${it.value.toString()}님이 입장하였습니다","관리자","관리자",-1)
                                                        )

                                                    }
                                                }

                                            }

                                            val userdb=FirebaseDatabase.getInstance().getReference("Users")
                                            userdb.child(email.toString()).child("roomNum").setValue(roomNum)


                                            val intent = Intent(activity, ChatRoomActivity::class.java)
                                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                            startActivity(intent)


                                         }else{
                                             Toast.makeText(activity,"비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show()
                                         }



                                        }

                                    }
                                }

                            }).create()

                            dialog.setOnShowListener(DialogInterface.OnShowListener {
                                val button=dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                                button.setTextColor(Color.BLACK)
                            })

                            dialog.show()

                        }
                    })

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })




        return view
    }


}