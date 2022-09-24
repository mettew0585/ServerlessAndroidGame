package com.example.test2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_signup.*

class Fragment2 : Fragment() {

    private val exp_level : Array<Int> = arrayOf(1000,2000,3000,4000,5000)
    private val images = arrayOf(R.drawable.character1,R.drawable.character2
    ,R.drawable.character3,R.drawable.character4,R.drawable.character5)

    private lateinit var database : DatabaseReference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view=inflater.inflate(R.layout.after_login_frag2,container,false)
        //val tv_email=view.findViewById<TextView>(R.id.tv_email)
        val tv_username=view.findViewById<TextView>(R.id.tv_username)
        val tv_level=view.findViewById<TextView>(R.id.tv_level)
        val pgBar=view.findViewById<ProgressBar>(R.id.progressBar)
        val tv_exp=view.findViewById<TextView>(R.id.tv_exp)
        val characterView= view.findViewById<ImageView>(R.id.character)
        val changeBtn=view.findViewById<Button>(R.id.change_btn)
        val tv_coin=view.findViewById<TextView>(R.id.tv_coin)
        val btnLogout=view.findViewById<Button>(R.id.btn_logout)

        val flag = activity?.application as FlagClass
        val email=flag.getEmail()


        database=FirebaseDatabase.getInstance().getReference("Users")


        database.child(email.toString()).get().addOnSuccessListener {
            tv_username.setText(it.child("userName").value.toString())
            tv_level.setText("LV.${it.child("level").value.toString()}")
            pgBar.max=exp_level[it.child("level").value.toString().toInt()]
            pgBar.progress=it.child("exp").value.toString().toInt()
            tv_exp.text="${it.child("exp").value.toString()}   /    ${exp_level[it.child("level").value.toString().toInt()]}"
            tv_coin.text=it.child("coin").value.toString()
            val imgNum=it.child("character").value.toString().toInt()
            characterView.setImageResource(images[imgNum-1])

        }


        changeBtn.setOnClickListener{
            activity?.let{
                val intent=Intent(context,ChoiceActivity::class.java)
                intent.putExtra("email",email)
                startActivity(intent)
            }
        }

        btnLogout.setOnClickListener{

            val intent = Intent(activity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }




        return view
    }

}