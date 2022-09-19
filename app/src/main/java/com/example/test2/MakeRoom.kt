package com.example.test2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.test2.databinding.ActivityMakeRoomBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_signup.*

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


                    val email=intent.getStringExtra("email")
                    val room = Room(num.toInt(),1,title,password,email)

                    fdb.getReference("Rooms").child(num).setValue(room)
                    fdb.getReference("Rooms").child(num).child("emails").child(flag.getEmail().toString()).
                    setValue(true)


                    fdb.getReference("roomCount").setValue(num.toInt()+1)

                    val intent = Intent(this,ChatRoomActivity::class.java)
                    startActivity(intent)
                    finish()
                }

            }
        }

    }
}

