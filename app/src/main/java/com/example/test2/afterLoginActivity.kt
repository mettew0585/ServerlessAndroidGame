package com.example.test2

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.ScaleAnimation
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.test2.databinding.ActivityAfterLoginBinding
import com.google.firebase.FirebaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_password.*


class afterLoginActivity : AppCompatActivity() {
    private lateinit var db: DatabaseReference
    private lateinit var roomdb: DatabaseReference
    private lateinit var binding: ActivityAfterLoginBinding
    var clicked = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_after_login)



        val flag= application as FlagClass
        val email=flag.getEmail()

        val scaleUp = ScaleAnimation(1f,1.5f,1f,1.5f,
            Animation.RELATIVE_TO_SELF, 0f, // Pivot point of X scaling
            Animation.RELATIVE_TO_SELF, 1f)
        scaleUp.duration=300
        scaleUp.fillAfter=true

        val scaleDown = ScaleAnimation(1.5f,1f,1.5f,1f,
            Animation.RELATIVE_TO_SELF, 0f, // Pivot point of X scaling
            Animation.RELATIVE_TO_SELF, 1f)
        scaleDown.duration=300
        scaleDown.fillAfter=true


        db=FirebaseDatabase.getInstance().getReference("Users")
        db.child(email.toString()).child("userName").get().addOnSuccessListener {
            Toast.makeText(this,"'${it.value.toString()}'님 환영합니다!",Toast.LENGTH_SHORT).show()



            binding.frag1Btn.backgroundTintList= ColorStateList.valueOf(Color.parseColor("#75E6DA"))
            binding.frag1Btn.startAnimation(scaleUp)

        }




        binding = ActivityAfterLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragment2 = Fragment2()
        val fragment1 = Fragment1()
        val fragment3 = Fragment3()

        setFragment(fragment1)

        binding.frag1Btn.setOnClickListener{
            setFragment(fragment1)


            binding.frag1Btn.backgroundTintList= ColorStateList.valueOf(Color.parseColor("#75E6DA"))
            binding.frag2Btn.backgroundTintList= ColorStateList.valueOf(Color.parseColor("#05445E"))
            binding.frag3Btn.backgroundTintList= ColorStateList.valueOf(Color.parseColor("#05445E"))

            if(clicked==2)
                binding.frag2Btn.startAnimation(scaleDown)
            else if(clicked==3)
                binding.frag3Btn.startAnimation(scaleDown)


            clicked=1
            binding.frag1Btn.startAnimation(scaleUp)


            binding.plusBtn.visibility = View.VISIBLE
        }
        binding.frag2Btn.setOnClickListener{

            binding.frag2Btn.backgroundTintList= ColorStateList.valueOf(Color.parseColor("#75E6DA"))
            binding.frag3Btn.backgroundTintList= ColorStateList.valueOf(Color.parseColor("#05445E"))
            binding.frag1Btn.backgroundTintList= ColorStateList.valueOf(Color.parseColor("#05445E"))


            if(clicked==1)
                binding.frag1Btn.startAnimation(scaleDown)
            else if(clicked==3)
                binding.frag3Btn.startAnimation(scaleDown)


            clicked=2
            binding.frag2Btn.startAnimation(scaleUp)

            binding.plusBtn.visibility = View.INVISIBLE
            setDataAtFragment(fragment2, email.toString())
        }
        binding.frag3Btn.setOnClickListener{

            binding.frag3Btn.backgroundTintList= ColorStateList.valueOf(Color.parseColor("#75E6DA"))
            binding.frag2Btn.backgroundTintList= ColorStateList.valueOf(Color.parseColor("#05445E"))
            binding.frag1Btn.backgroundTintList= ColorStateList.valueOf(Color.parseColor("#05445E"))


            if(clicked==1)
                binding.frag1Btn.startAnimation(scaleDown)
            else if(clicked==2)
                binding.frag2Btn.startAnimation(scaleDown)

            clicked=3
            binding.frag3Btn.startAnimation(scaleUp)

            binding.plusBtn.visibility = View.INVISIBLE
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

