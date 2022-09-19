package com.example.test2

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.TypedValue
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.test2.databinding.ActivityAfterLoginBinding
import com.example.test2.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_after_login.*
import kotlinx.android.synthetic.main.activity_signup.*

class afterLoginActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var roomdb: DatabaseReference
    private lateinit var binding: ActivityAfterLoginBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_after_login)



        binding = ActivityAfterLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragment2 = Fragment2()
        val fragment1 = Fragment1()
        val fragment3 = Fragment3()

        setFragment(fragment1)




        binding.frag1Btn.setOnClickListener {
            setFragment(fragment1)
            binding.frag1Btn.setTextColor(Color.BLACK)
            binding.frag2Btn.setTextColor(Color.parseColor("#868B8E"))
            binding.frag3Btn.setTextColor(Color.parseColor("#868B8E"))

            binding.frag1Btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)
            binding.frag2Btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10F)
            binding.frag3Btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10F)


            binding.plusBtn.visibility = View.VISIBLE
        }


        binding.frag2Btn.setOnClickListener {

            binding.frag2Btn.setTextColor(Color.BLACK)
            binding.frag1Btn.setTextColor(Color.parseColor("#868B8E"))
            binding.frag3Btn.setTextColor(Color.parseColor("#868B8E"))

            binding.frag2Btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)
            binding.frag1Btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10F)
            binding.frag3Btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10F)

            val email = intent.getStringExtra("email")
            binding.plusBtn.visibility = View.INVISIBLE
            setDataAtFragment(fragment2, email.toString())
        }

        binding.frag3Btn.setOnClickListener {

            binding.frag3Btn.setTextColor(Color.BLACK)
            binding.frag1Btn.setTextColor(Color.parseColor("#868B8E"))
            binding.frag2Btn.setTextColor(Color.parseColor("#868B8E"))


            binding.frag3Btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)
            binding.frag1Btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10F)
            binding.frag2Btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10F)

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

