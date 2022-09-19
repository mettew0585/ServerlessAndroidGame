package com.example.test2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.test2.databinding.ActivityPasswordBinding

class PasswordActivity : AppCompatActivity() {
    private lateinit var binding : ActivityPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password)

        binding = ActivityPasswordBinding.inflate(layoutInflater)

        val data1=intent.getStringExtra("room")


        Toast.makeText(this,data1.toString(),Toast.LENGTH_SHORT).show()
        binding.submitbtn.setOnClickListener{
            val pw=binding.etPw.text.toString()

        }
    }
}