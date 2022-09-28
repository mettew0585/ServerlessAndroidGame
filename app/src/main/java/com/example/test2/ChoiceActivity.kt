package com.example.test2

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import com.example.test2.databinding.ActivityChoiceBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_signup.*


class ChoiceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChoiceBinding
    private lateinit var db: DatabaseReference
    private lateinit var userdb: DatabaseReference
    private lateinit var userdb2: DatabaseReference
    private lateinit var havedb: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choice)

        val data: Array<Boolean> = arrayOf(false, false, false, false, false, false)

        binding = ActivityChoiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val email = intent.getStringExtra("email")
        db = FirebaseDatabase.getInstance().getReference("Have")
        userdb = FirebaseDatabase.getInstance().getReference("Users")

        var cur: Int = -1
        userdb.child(email.toString()).child("character").get().addOnSuccessListener {
            cur = it.value.toString().toInt()
        }




        db.child(email.toString()).child("have1").get().addOnSuccessListener {
            if (it.value.toString().toBoolean()) {
                binding.char1.setBackgroundResource(R.drawable.unable_use)
                binding.tvHave1.setText("보유중!")
            }
            if (cur == 1) {
                binding.tvHave1.setText("사용중!")
            }
        }
        db.child(email.toString()).child("have2").get().addOnSuccessListener {
            if (it.value.toString().toBoolean()) {
                binding.char2.setBackgroundResource(R.drawable.unable_use)
                binding.tvHave2.setText("보유중!")
            }

            if (cur == 2) {
                binding.tvHave2.setText("사용중!")
            }
        }
        db.child(email.toString()).child("have3").get().addOnSuccessListener {
            if (it.value.toString().toBoolean()) {
                binding.char3.setBackgroundResource(R.drawable.unable_use)
                binding.tvHave3.setText("보유중!")
            }

            if (cur == 3) {
                binding.tvHave3.setText("사용중!")
            }
        }
        db.child(email.toString()).child("have4").get().addOnSuccessListener {
            if (it.value.toString().toBoolean()) {
                binding.char4.setBackgroundResource(R.drawable.unable_use)
                binding.tvHave4.setText("보유중!")
            }

            if (cur == 4) {
                binding.tvHave4.setText("사용중!")
            }
        }
        db.child(email.toString()).child("have5").get().addOnSuccessListener {
            if (it.value.toString().toBoolean()) {
                binding.char5.setBackgroundResource(R.drawable.unable_use)
                binding.tvHave5.setText("보유중!")
            }

            if (cur == 5) {
                binding.tvHave5.setText("사용중!")
            }
        }



        binding.imageView1.setOnClickListener {
            db.child(email.toString()).child("have1").get().addOnSuccessListener {
                if (it.value.toString().toBoolean()) {
                    //보유중인 상황 . 교체
                    if (cur == 1) {
                        Toast.makeText(this, "현재 사용중인 캐릭터 입니다!", Toast.LENGTH_SHORT).show()
                    } else {
                        val builder = AlertDialog.Builder(this)
                        builder.setMessage("현재 캐릭터를 '돌쇠'로 바꾸시겠습니까?")
                            .setPositiveButton(
                                "확인",
                                DialogInterface.OnClickListener { dialog, id ->
                                    userdb.child(email.toString()).child("character").setValue(1)
                                    restart()
                                }
                            )
                        builder.show()
                    }
                }
            }
        }



        binding.imageView2.setOnClickListener {
            db.child(email.toString()).child("have2").get().addOnSuccessListener {
                if (it.value.toString().toBoolean()) {
                    //교체?
                    if (cur == 2) {
                        Toast.makeText(this, "현재 사용중인 캐릭터 입니다!", Toast.LENGTH_SHORT).show()
                    } else {
                        val builder = AlertDialog.Builder(this)
                        builder.setMessage("현재 캐릭터를 '석쇠'로 바꾸시겠습니까?")
                            .setPositiveButton(
                                "확인",
                                DialogInterface.OnClickListener { dialog, id ->
                                    userdb.child(email.toString()).child("character").setValue(2)
                                    restart()
                                }
                            )
                        builder.show()
                    }
                } else {
                    val builder = AlertDialog.Builder(this)
                    builder.setMessage("'석쇠'를 구매하시겠습니까").setPositiveButton(
                        "확인",
                        DialogInterface.OnClickListener { dialog, id ->
                            userdb.child(email.toString()).child("coin").get()
                                .addOnSuccessListener {
                                    val coin = it.value.toString().toLong()

                                    if (coin >= 500) {
                                        db.child(email.toString()).child("have2").setValue(true)
                                        userdb.child(email.toString()).child("coin")
                                            .setValue(coin - 500)
                                        restart()
                                    } else {
                                        Toast.makeText(this, "코인이 부족합니다!", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                }
                        }
                    )

                    builder.show()
                }

            }
        }


        binding.imageView3.setOnClickListener {
            db.child(email.toString()).child("have3").get().addOnSuccessListener {
                if (it.value.toString().toBoolean()) {
                    //교체?
                    if (cur == 3) {
                        Toast.makeText(this, "현재 사용중인 캐릭터 입니다!", Toast.LENGTH_SHORT).show()
                    } else {
                        val builder = AlertDialog.Builder(this)
                        builder.setMessage("현재 캐릭터를 '영희'로 바꾸시겠습니까?")
                            .setPositiveButton(
                                "확인",
                                DialogInterface.OnClickListener { dialog, id ->
                                    userdb.child(email.toString()).child("character").setValue(3)
                                    restart()
                                }
                            )
                        builder.show()
                    }
                } else {
                    val builder = AlertDialog.Builder(this)
                    builder.setMessage("'영희'를 구매하시겠습니까").setPositiveButton(
                        "확인",
                        DialogInterface.OnClickListener { dialog, id ->
                            userdb.child(email.toString()).child("coin").get()
                                .addOnSuccessListener {
                                    val coin = it.value.toString().toLong()
                                    if (coin >= 1000000000000) {
                                        db.child(email.toString()).child("have3").setValue(true)
                                        userdb.child(email.toString()).child("coin")
                                            .setValue(coin - 1000)
                                        restart()
                                    } else {
                                        Toast.makeText(this, "코인이 부족합니다!", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                }
                        }
                    )

                    builder.show()
                }

            }
        }


        binding.imageView4.setOnClickListener {
            db.child(email.toString()).child("have4").get().addOnSuccessListener {
                if (it.value.toString().toBoolean()) {
                    //교체?
                    if (cur == 4) {
                        Toast.makeText(this, "현재 사용중인 캐릭터 입니다!", Toast.LENGTH_SHORT).show()
                    } else {
                        val builder = AlertDialog.Builder(this)
                        builder.setMessage("현재 캐릭터를 '암살자'로 바꾸시겠습니까?")
                            .setPositiveButton(
                                "확인",
                                DialogInterface.OnClickListener { dialog, id ->
                                    userdb.child(email.toString()).child("character").setValue(4)
                                    restart()
                                }
                            )
                        builder.show()
                    }
                } else {

                    val builder = AlertDialog.Builder(this)
                    builder.setMessage("'암살자'를 구매하시겠습니까").setPositiveButton(
                        "확인",
                        DialogInterface.OnClickListener { dialog, id ->
                            userdb.child(email.toString()).child("coin").get()
                                .addOnSuccessListener {
                                    val coin = it.value.toString().toLong()
                                    if (coin >= 1500) {
                                        db.child(email.toString()).child("have4").setValue(true)
                                        userdb.child(email.toString()).child("coin")
                                            .setValue(coin - 1500)
                                        restart()
                                    } else {
                                        Toast.makeText(this, "코인이 부족합니다!", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                }
                        }
                    )

                    builder.show()
                }
            }
        }



        binding.imageView5.setOnClickListener {
            db.child(email.toString()).child("have5").get().addOnSuccessListener {
                if (it.value.toString().toBoolean()) {
                    //교체?
                    if (cur == 5) {
                        Toast.makeText(this, "현재 사용중인 캐릭터 입니다!", Toast.LENGTH_SHORT).show()
                    } else {
                        val builder = AlertDialog.Builder(this)
                        builder.setMessage("현재 캐릭터를 '하쿠미짱'으로 바꾸시겠습니까?")
                            .setPositiveButton(
                                "확인",
                                DialogInterface.OnClickListener { dialog, id ->
                                    userdb.child(email.toString()).child("character").setValue(5)
                                    restart()
                                }
                            )
                        builder.show()
                    }
                } else {
                    val builder = AlertDialog.Builder(this)
                    builder.setMessage("'하쿠미짱'을 구매하시겠습니까").setPositiveButton(
                        "확인",
                        DialogInterface.OnClickListener { dialog, id ->
                            userdb.child(email.toString()).child("coin").get()
                                .addOnSuccessListener {
                                    val coin = it.value.toString().toLong()
                                    if (coin >= 300) {
                                        db.child(email.toString()).child("have5").setValue(true)
                                        userdb.child(email.toString()).child("coin")
                                            .setValue(coin - 300)
                                        restart()
                                    } else {
                                        Toast.makeText(this, "코인이 부족합니다!", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                }
                        }
                    )

                    builder.show()
                }
            }
        }

//



    }




    //


    private fun restart(){
        val intent = intent
        finish()
        startActivity(intent)
    }
}

