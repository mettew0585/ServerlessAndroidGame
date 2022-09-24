package com.example.test2

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import com.example.joystick.JoystickView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Game : AppCompatActivity() {

        val roomNum=1

        val width=50
        val height=50

        var x=10
        var y=10


        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_game)



            var arrImg : ArrayList<ArrayList<ImageView>> = ArrayList<ArrayList<ImageView>>()
            val skillBtn = findViewById<Button>(R.id.skillbtn)





            for (i in 0..height ) {
                var arr : ArrayList<ImageView> = ArrayList()
                for (j in 0..width ) {
                    var str1 : String
                    var str2 : String
                    if(i>=0&&i<10)
                        str1="0$i"
                    else
                        str1="$i"
                    if(j>=0&&j<10)
                        str2="0$j"
                    else
                        str2="$j"

                    val str="img${str1}${str2}"
                    val resID = resources.getIdentifier(str, "id", packageName)
                    arr.add(findViewById(resID))
                }
                arrImg.add(arr)

            }




            val db= FirebaseDatabase.getInstance().getReference("Position")

            val joystickView = findViewById<JoystickView>(R.id.joystick)

            db.child("1").child("2").addValueEventListener(object: ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {


                    val sx=snapshot.child("x").value.toString().toInt()
                    val sy=snapshot.child("y").value.toString().toInt()


                    arrImg[sx][sy].setBackgroundColor(Color.parseColor("#FFBB86FC"))
                }

                override fun onCancelled(error: DatabaseError) {
                }


            }
            )

            joystickView.setOnMoveListener({ angle, strength ->

                db.child("1").child("1").child("x").setValue(x)
                db.child("1").child("1").child("y").setValue(y)

                if(angle>=45&&angle<135){
                    //up
                    if(x!=0){
                        x--
                        arrImg[x][y].setBackgroundColor(Color.parseColor("#FF0000"))
                    }

                }else if(angle>=135&&angle<225){
                    //left
                    if(y!=0){
                        y--
                        arrImg[x][y].setBackgroundColor(Color.parseColor("#FF0000"))
                    }
                }else if(angle>=225&&angle<315) {
                    //down
                    if (x != height-1) {
                        x++
                        arrImg[x][y].setBackgroundColor(Color.parseColor("#FF0000"))
                    }
                }else if(angle>=315||angle<=405){
                    //right
                    if(y!=width-1){
                        val str : String="img${x}${y}"
                        val img = this.resources.getIdentifier(str,"id",packageName)
                        y++
                        arrImg[x][y].setBackgroundColor(Color.parseColor("#FF0000"))
                    }

                }

            }, 50)

        }
}