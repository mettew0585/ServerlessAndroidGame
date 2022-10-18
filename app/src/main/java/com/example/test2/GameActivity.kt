package com.example.test2

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import java.security.SecureRandom

class GameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        val intent = Intent(this, ChoiceMapActivity::class.java)
        val flag = application as FlagClass
        var opUserId: Int
        var userId: Int
        var mapString: String
        val db = FirebaseDatabase.getInstance().getReference("Rooms")
        val userDb = FirebaseDatabase.getInstance().getReference("Users")
        userDb.child(flag.getEmail().toString()).get().addOnSuccessListener {
            userId = it.child("userId").value.toString().toInt()

            db.child(flag.getRoomNum().toString()).get().addOnSuccessListener {
                opUserId =
                    it.child("emails").child(userId.toString()).child("opponent").value.toString()
                        .toInt()
                mapString = it.child("mapString").value.toString()
                val secureRandom = SecureRandom()
                val ran: Int = secureRandom.nextInt(10)

                intent.putExtra("mapString", mapString)
                var win: Int = userId
                var lose: Int = opUserId
                if (ran < 5) {
                    win = opUserId
                    lose = userId
                }
                intent.putExtra("winnerId", win)
                intent.putExtra("loserId", lose)
                //db.child("ttt").child("winnerId").setValue(win)
                //db.child("ttt").child("loserId").setValue(lose)
                startActivity(intent)
                finish()
            }
        }
    }
}