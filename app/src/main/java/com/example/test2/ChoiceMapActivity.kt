package com.example.test2

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.myapplication.MapHandling
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_choice_map.*

class ChoiceMapActivity : AppCompatActivity() {
    var mapString: String = ""
    val colorArr = arrayOf(R.color.red_color, R.color.green_color)
    var loserId: Int = 0
    var winnerId: Int = 0
    var playerNum: Int = 1
    var landNum: Int = 1
    fun landColorTypeCheck(mapString: String, userId: Int, chkIdx: Int): Int {
        if (MapHandling().getMapColorToIdx(mapString, userId) == MapHandling().getMapColorToIdx(
                mapString,
                chkIdx
            )
        ) return 1
        return 0
    }

    fun setMap() {
        val imgBtnArr = arrayOf(
            imgbtn_map1,
            imgbtn_map1,
            imgbtn_map2,
            imgbtn_map3,
            imgbtn_map4,
            imgbtn_map5,
            imgbtn_map6,
            imgbtn_map7,
            imgbtn_map8,
            imgbtn_map9,
            imgbtn_map10,
            imgbtn_map11,
            imgbtn_map12,
            imgbtn_map13,
            imgbtn_map14,
            imgbtn_map15,
            imgbtn_map16,
            imgbtn_map17,
            imgbtn_map18,
            imgbtn_map19,
            imgbtn_map20,
            imgbtn_map21,
            imgbtn_map22,
            imgbtn_map23,
            imgbtn_map24,
            imgbtn_map25
        )
        for (i in 1..landNum) {
            imgBtnArr[i].visibility = View.VISIBLE
        }
    }

    fun showChoiceMap(colorType: Int) {
        val landColorType = Array<Int>(30) { 0 }
        val imgBtnArr = arrayOf(
            imgbtn_map1,
            imgbtn_map1,
            imgbtn_map2,
            imgbtn_map3,
            imgbtn_map4,
            imgbtn_map5,
            imgbtn_map6,
            imgbtn_map7,
            imgbtn_map8,
            imgbtn_map9,
            imgbtn_map10,
            imgbtn_map11,
            imgbtn_map12,
            imgbtn_map13,
            imgbtn_map14,
            imgbtn_map15,
            imgbtn_map16,
            imgbtn_map17,
            imgbtn_map18,
            imgbtn_map19,
            imgbtn_map20,
            imgbtn_map21,
            imgbtn_map22,
            imgbtn_map23,
            imgbtn_map24,
            imgbtn_map25
        )
        if (colorType == 1) {
            for (curIdx in 1..25) {
                landColorType[curIdx] = landColorTypeCheck(mapString, loserId, curIdx)
            }
            for (i in 1..25) {
                imgBtnArr[i].setColorFilter(
                    ContextCompat.getColor(
                        this,
                        colorArr[landColorType[i]]
                    )
                )
                //imgBtnArr[i].setBackgroundColor(Color.parseColor(colorArr[landColorType[i]]))
            }
        }
        if (colorType == 2) {
            for (i in 1..landNum) {
                imgBtnArr[i].setColorFilter(
                    colorArr[1],
                    PorterDuff.Mode.DST
                )
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choice_map)
        val imgBtnArr = arrayOf(
            imgbtn_map1,
            imgbtn_map1,
            imgbtn_map2,
            imgbtn_map3,
            imgbtn_map4,
            imgbtn_map5,
            imgbtn_map6,
            imgbtn_map7,
            imgbtn_map8,
            imgbtn_map9,
            imgbtn_map10,
            imgbtn_map11,
            imgbtn_map12,
            imgbtn_map13,
            imgbtn_map14,
            imgbtn_map15,
            imgbtn_map16,
            imgbtn_map17,
            imgbtn_map18,
            imgbtn_map19,
            imgbtn_map20,
            imgbtn_map21,
            imgbtn_map22,
            imgbtn_map23,
            imgbtn_map24,
            imgbtn_map25
        )
        for (i in 1..25) {
            imgBtnArr[i].visibility = View.INVISIBLE
        }
        val flag = application as FlagClass
        val db = FirebaseDatabase.getInstance().getReference("Rooms")
        val userDb = FirebaseDatabase.getInstance().getReference("Users")
        userDb.child(flag.getEmail().toString()).child("userId").get().addOnSuccessListener {
            val userId = it.value.toString().toInt()


            db.child(flag.getRoomNum().toString()).get().addOnSuccessListener {
                loserId = intent.getIntExtra("loserId", 0)
                winnerId = intent.getIntExtra("winnerId", 0)
                mapString = it.child("mapString").value.toString()
                playerNum = it.child("players").value.toString().toInt()
                landNum = it.child("landNum").value.toString().toInt()
                if (userId != winnerId) {
                    val intent = Intent(this@ChoiceMapActivity, MapActivity::class.java)
                    startActivity(intent)

                    overridePendingTransition(0, 0)
                    finish()
                }
                var colorType = 1
                setMap()
                showChoiceMap(colorType)
                color_change_btn.setOnClickListener {
                    colorType = colorType % 2 + 1
                    if (colorType == 1) {
                        color_change_btn.text = "색 벗기기"
                        showChoiceMap(colorType)
                    }
                    if (colorType == 2) {
                        color_change_btn.text = "색 입히기"
                        showChoiceMap(colorType)
                    }
                }
                for (i in 1..25) {
                    imgBtnArr[i].setOnClickListener(ChoiceMapButtonListener())
                }
            }


        }


    }

    inner class ChoiceMapButtonListener : View.OnClickListener {
        override fun onClick(v: View?) {
            val intent = Intent(this@ChoiceMapActivity, MapActivity::class.java)
            val dlg = AttackChoiceDialog(this@ChoiceMapActivity)
            var imgNum: Int
            imgNum = 0
            dlg.setOnOKClickedListener {
                Toast.makeText(this@ChoiceMapActivity, "선택하였습니다.", Toast.LENGTH_SHORT).show()
                Toast.makeText(this@ChoiceMapActivity,imgNum.toString()+" "+winnerId.toString(),Toast.LENGTH_SHORT).show()
                mapString = MapHandling().UpdateMap(mapString, winnerId, imgNum)
                MapHandling().changeAttackString(mapString, winnerId, imgNum, 0)

                val db=FirebaseDatabase.getInstance().getReference("Rooms")
                val flag = application as FlagClass
                db.child(flag.getRoomNum().toString()).child("mapString")
                .setValue(mapString)
                startActivity(intent)

                overridePendingTransition(0, 0)
                finish()
            }//예 클릭시 실행
            when (v?.id) {
                R.id.imgbtn_map1 -> imgNum = 1
                R.id.imgbtn_map2 -> imgNum = 2
                R.id.imgbtn_map3 -> imgNum = 3
                R.id.imgbtn_map4 -> imgNum = 4
                R.id.imgbtn_map5 -> imgNum = 5
                R.id.imgbtn_map6 -> imgNum = 6
                R.id.imgbtn_map7 -> imgNum = 7
                R.id.imgbtn_map8 -> imgNum = 8
                R.id.imgbtn_map9 -> imgNum = 9
                R.id.imgbtn_map10 -> imgNum = 10
                R.id.imgbtn_map11 -> imgNum = 11
                R.id.imgbtn_map12 -> imgNum = 12
                R.id.imgbtn_map13 -> imgNum = 13
                R.id.imgbtn_map14 -> imgNum = 14
                R.id.imgbtn_map15 -> imgNum = 15
                R.id.imgbtn_map16 -> imgNum = 16
                R.id.imgbtn_map17 -> imgNum = 17
                R.id.imgbtn_map18 -> imgNum = 18
                R.id.imgbtn_map19 -> imgNum = 19
                R.id.imgbtn_map20 -> imgNum = 20
                R.id.imgbtn_map21 -> imgNum = 21
                R.id.imgbtn_map22 -> imgNum = 22
                R.id.imgbtn_map23 -> imgNum = 23
                R.id.imgbtn_map24 -> imgNum = 24
                R.id.imgbtn_map25 -> imgNum = 25
            }
            val landTypeNum = landColorTypeCheck(mapString, loserId, imgNum)
            if (landTypeNum == 1) {
                dlg.start("선택하시겠습니까?")
            } else {
                Toast.makeText(
                    this@ChoiceMapActivity,
                    "상대가 가진 땅이 아닙니다. 다시 선택하십시오",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
