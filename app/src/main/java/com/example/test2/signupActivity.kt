package com.example.test2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.example.test2.databinding.ActivityMainBinding
import com.example.test2.databinding.ActivitySignupBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_signup.*
import java.util.regex.Pattern

class signupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var userdb: DatabaseReference
    private lateinit var namedb: DatabaseReference
    private lateinit var havedb: DatabaseReference

    var emailChecked = false
    var usernameChecked = false
    var passwordChecked=false

    val pwPattern1 = "^(?=.*[A-Za-z])(?=.*[0-9])[A-Za-z[0-9]]{8,20}$" // 영문, 숫자 8 ~ 20자 패턴
    val pattern = Pattern.compile(pwPattern1)
    val emailPattern = "^[a-zA-Z0-9]+@[a-zA-Z0-9]+\$"
    val epattern = Pattern.compile(emailPattern)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.emailOverlapCheckBtn.setOnClickListener {

            userdb = FirebaseDatabase.getInstance().getReference("Users")

            val email = binding.email.text.toString()
            val emailRight=epattern.matcher(email)

            if (emailRight.find()) {
                userdb.child(email).get().addOnSuccessListener {

                    if (it.exists()) {
                        emailChecked = false
                        binding.emailResult.setText("중복된 이메일입니다.")
                    } else {
                        emailChecked = true;
                        binding.emailResult.setText("사용가능한 이메일 입니다")
                    }
                }
            } else {
                Toast.makeText(this, "올바른 이메일을 입력해주세요", Toast.LENGTH_SHORT).show()
            }
        }

        binding.password.addTextChangedListener {
            val password=binding.password.text.toString()

            val passwordRight=pattern.matcher(password)

            if(passwordRight.find()){
                binding.passwordResult.setText("사용 가능한 비밀번호 입니다")
                passwordChecked=true
            }else{
                binding.passwordResult.setText("사용 불가능한 비밀번호 입니다. ( 영문, 숫자 8~20자 )")
                passwordChecked=false
            }
        }

        binding.usernameOverlapCheckBtn.setOnClickListener{
            namedb=FirebaseDatabase.getInstance().getReference("Names")

            val username=binding.userName.text.toString()

            if(username.isNotEmpty()) {
                namedb.child(username).get().addOnSuccessListener {
                    if (it.exists()) {
                        usernameChecked = false
                        binding.usernameResult.setText("중복된 닉네임 입니다")
                    } else {
                        usernameChecked = true
                        binding.usernameResult.setText("사용가능한 닉네임입니다.")
                    }
                }
            }else{
                Toast.makeText(this,"닉네임을 입력해주세요",Toast.LENGTH_SHORT).show()
            }
        }

        binding.submitBtn.setOnClickListener{
            if(emailChecked&&usernameChecked&&passwordChecked){
                val email=binding.email.text.toString()
                val password=binding.password.text.toString()
                val userName=binding.userName.text.toString()

                userdb=FirebaseDatabase.getInstance().getReference("Users")
                namedb=FirebaseDatabase.getInstance().getReference("Names")
                havedb=FirebaseDatabase.getInstance().getReference("Have")

                val User=User(email,password,userName,-1,1,0,1,0)
                val Have=Have(true,false,false,false,false)

                userdb.child(email).setValue(User)
                namedb.child(userName).setValue(userName)
                havedb.child(email).setValue(Have)

                Toast.makeText(this,"회원가입완료!",Toast.LENGTH_SHORT).show()
                finish()
            }else{
                Toast.makeText(this,"이메일, 비밀번호, 닉네임을 똑바로 입력해주세요",Toast.LENGTH_SHORT).show()
            }
        }
    }
}




