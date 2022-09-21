package com.example.test2

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.test2.databinding.ActivityAfterLoginBinding
import com.google.firebase.FirebaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class afterLoginActivity : AppCompatActivity() {
    private lateinit var db: DatabaseReference
    private lateinit var roomdb: DatabaseReference
    private lateinit var binding: ActivityAfterLoginBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_after_login)


        val flag= application as FlagClass
        val email=flag.getEmail()

        db=FirebaseDatabase.getInstance().getReference("Users")
        db.child(email.toString()).child("userName").get().addOnSuccessListener {
            Toast.makeText(this,"'${it.value.toString()}'님 환영합니다!",Toast.LENGTH_SHORT).show()
        }




        binding = ActivityAfterLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragment2 = Fragment2()
        val fragment1 = Fragment1()
        val fragment3 = Fragment3()

        setFragment(fragment1)




        binding.frag1Btn.setOnClickListener {
            setFragment(fragment1)

            binding.frag1Btn.backgroundTintList= ColorStateList.valueOf(Color.parseColor("#05445E"))
            binding.frag2Btn.backgroundTintList= ColorStateList.valueOf(Color.parseColor("#75E6DA"))
            binding.frag3Btn.backgroundTintList= ColorStateList.valueOf(Color.parseColor("#75E6DA"))

            binding.frag1Btn.layoutParams=(LinearLayout.LayoutParams(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,50F,resources.displayMetrics).toInt()
                ,TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,50F,resources.displayMetrics).toInt()))
            binding.frag2Btn.layoutParams=(LinearLayout.LayoutParams(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,30F,resources.displayMetrics).toInt()
                ,TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,30F,resources.displayMetrics).toInt()))
            binding.frag3Btn.layoutParams=(LinearLayout.LayoutParams(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,30F,resources.displayMetrics).toInt(),
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,30F,resources.displayMetrics).toInt()))


            binding.plusBtn.visibility = View.VISIBLE
        }


        binding.frag2Btn.setOnClickListener {

            binding.frag2Btn.backgroundTintList= ColorStateList.valueOf(Color.parseColor("#05445E"))
            binding.frag1Btn.backgroundTintList= ColorStateList.valueOf(Color.parseColor("#75E6DA"))
            binding.frag3Btn.backgroundTintList= ColorStateList.valueOf(Color.parseColor("#75E6DA"))


            binding.frag2Btn.layoutParams=(LinearLayout.LayoutParams(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,50F,resources.displayMetrics).toInt()
                ,TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,50F,resources.displayMetrics).toInt()))
            binding.frag1Btn.layoutParams=(LinearLayout.LayoutParams(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,30F,resources.displayMetrics).toInt()
                ,TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,30F,resources.displayMetrics).toInt()))
            binding.frag3Btn.layoutParams=(LinearLayout.LayoutParams(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,30F,resources.displayMetrics).toInt(),
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,30F,resources.displayMetrics).toInt()))



            val email = intent.getStringExtra("email")
            binding.plusBtn.visibility = View.INVISIBLE
            setDataAtFragment(fragment2, email.toString())
        }

        binding.frag3Btn.setOnClickListener {

            binding.frag3Btn.backgroundTintList= ColorStateList.valueOf(Color.parseColor("#05445E"))
            binding.frag2Btn.backgroundTintList= ColorStateList.valueOf(Color.parseColor("#75E6DA"))
            binding.frag1Btn.backgroundTintList= ColorStateList.valueOf(Color.parseColor("#75E6DA"))

            binding.frag3Btn.layoutParams=(LinearLayout.LayoutParams(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,50F,resources.displayMetrics).toInt()
                ,TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,50F,resources.displayMetrics).toInt()))
            binding.frag1Btn.layoutParams=(LinearLayout.LayoutParams(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,30F,resources.displayMetrics).toInt()
                ,TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,30F,resources.displayMetrics).toInt()))
            binding.frag2Btn.layoutParams=(LinearLayout.LayoutParams(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,30F,resources.displayMetrics).toInt(),
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,30F,resources.displayMetrics).toInt()))


            setFragment(fragment3)
        }



        binding.plusBtn.setOnClickListener {

            val intent = Intent(this, MakeRoom::class.java)
            startActivity(intent)
        }


    }

    private fun setDataAtFragment(fragment: Fragment, email: String) {

        val bundle = Bundle()
        bundle.putString("email", email)

        fragment.arguments = bundle
        setFragment(fragment)
    }

    private fun setFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.main_frame, fragment)
        transaction.commit()
    }

    fun refreshFragment(context: Context) {
        context?.let {
            val fragmentManager = (context as? AppCompatActivity)?.supportFragmentManager
            fragmentManager?.let {
                val currentFragment = fragmentManager.findFragmentById(R.id.main_frame)
                currentFragment?.let{
                    val fragmentTransaction=fragmentManager.beginTransaction()
                    fragmentTransaction.detach(it)
                    fragmentTransaction.attach(it)
                    fragmentTransaction.commit()
                }
            }
        }
    }
}

