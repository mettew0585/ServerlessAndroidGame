package com.example.test2

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.test2.databinding.ActivityMainBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var database : DatabaseReference




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.signupBtn.setOnClickListener{
            val intent=Intent(this,signupActivity::class.java)

            startActivity(intent)

        }

        binding.loginBtn.setOnClickListener {

            val email = binding.emailText.text.toString()
            val password = binding.passwordText.text.toString()


            database = FirebaseDatabase.getInstance().getReference("Users")

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this,"enter email and password",Toast.LENGTH_SHORT).show()
            } else {
                database.child(email).get().addOnSuccessListener {
                    if (it.exists()) {
                        val db_password = it.child("passWord").value
                        val db_roomNum = it.child("roomNum").value.toString().toInt()
                        if (db_password == password) {
                            Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()


                            val flag = application as FlagClass
                            flag.setEmail(email)
                            flag.setRoomNum(db_roomNum)

                            //접속된 방 없을 경우
                            if(db_roomNum==-1) {
                                val intent=Intent(this,afterLoginActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            //접속된 방 잇을경우
                            else {
                                val intent = Intent(this,ChatRoomActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        } else {
                            Toast.makeText(this, "비밀번호가 맞지 않습니다", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "등록되지 않은 email", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}