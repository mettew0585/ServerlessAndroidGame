package com.example.test2

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.android.synthetic.main.after_login_frag1.*

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

                    }

                    var adapter=RoomRecyclerAdapter(roomLis)
                    rrcv.adapter=adapter
                    adapter.setOnItemClickListener(object : RoomRecyclerAdapter.onItemClickListener{

                        override fun onItemClick(position: Int) {

                            val roomNum=roomLis[position].roomNum
                            Toast.makeText(activity,roomNum.toString(),Toast.LENGTH_SHORT).show()

                            val roomNumFlag = activity.application

                            val intent=Intent(activity,EnterRoomActivity::class.java)
                            startActivity(intent)
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