package com.example.test2

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.myapplication.ATCCheckDialog
import com.example.myapplication.MapHandling
import com.example.test2.databinding.ActivityMapBinding
import com.google.firebase.database.FirebaseDatabase
import java.security.SecureRandom


class MapActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMapBinding

    val colorArr = arrayOf(
        R.color.color1,
        R.color.color1,
        R.color.color2,
        R.color.color3,
        R.color.color4,
        R.color.color5,
        R.color.color6,
        R.color.color7,
        R.color.color8,
        R.color.color9,
        R.color.color10,
        R.color.color11,
        R.color.color12,
        R.color.color13,
        R.color.color14,
        R.color.color15,
        R.color.color16,
        R.color.color17,
        R.color.color18,
        R.color.color19,
        R.color.color20,
        R.color.color21,
        R.color.color22,
        R.color.color23,
        R.color.color24,
        R.color.color25
    )

    val SPLITLEN: Int = 36
    val EMAILLEN: Int = 20
    var userId: Int = 1
    var landNum: Int = 25
    var playerNum: Int = 5
    var MAX_LANDNUM = 25
    var mapString =
                "01/01/01@naver.com##################" +
                "02/02/2@naver.com###################" +
                "03/03/03@naver.com##################" +
                "04/04/4@naver.com###################" +
                "05/05/05@naver.com##################" +
                "06/06/06@naver.com##################" +
                "07/07/07@naver.com##################" +
                "08/08/08@naver.com##################" +
                "09/09/09@naver.com##################" +
                "10/10/10@naver.com##################" +
                "11/11/11@naver.com##################" +
                "12/12/12@naver.com##################" +
                "13/13/13@naver.com##################" +
                "14/14/14@naver.com##################" +
                "15/15/15@naver.com##################" +
                "16/16/16@naver.com##################" +
                "17/17/17@naver.com##################" +
                "18/18/18@naver.com##################" +
                "19/19/19@naver.com##################" +
                "20/20/20@naver.com##################" +
                "21/21/21@naver.com##################" +
                "22/22/22@naver.com##################" +
                "23/23/23@naver.com##################" +
                "24/24/24@naver.com##################" +
                "25/25/25@naver.com##################"

    fun setMap(landNum: Int, imgBtnArr: Array<ImageButton>) {


        for (i in 1..landNum) {
            imgBtnArr[i].visibility = View.VISIBLE
        }

    }

    fun showMap(mapString: String, colorType: Int, imgBtnArr: Array<ImageButton>) {


        if (colorType == 1) {
            for (i in 1..landNum) {
                imgBtnArr[i].setColorFilter(
                    ContextCompat.getColor(
                        this,
                        colorArr[MapHandling().getMapColorToIdx(mapString, i)]
                    )
                )
            }
        }

        if (colorType == 2) {
            for (i in 1..landNum) {
                imgBtnArr[i].setColorFilter(
                    colorArr[MapHandling().getMapColorToIdx(mapString, i)],
                    PorterDuff.Mode.DST
                )
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(intent.hasExtra("ATCCheckStart")) {

            val intent = Intent(this, MainActivity::class.java)
            val dlg = ATCCheckDialog(this)
            dlg.setOnOKClickedListener {
                Toast.makeText(this, "전투를 진행합니다", Toast.LENGTH_SHORT).show()
                //on going true로 변경
                val p1=intent.getIntExtra("player1",0)
                val p2=intent.getIntExtra("player2",0)
                startActivity(intent)
                overridePendingTransition(0, 0)
                finish()
            }
            dlg.start("전투를 진행하시겠습니까")
        }
            val flag = application as FlagClass

        val imgBtnArr = arrayOf(
            binding.imgbtnMap1,
            binding.imgbtnMap1,
            binding.imgbtnMap2,
            binding.imgbtnMap3,
            binding.imgbtnMap4,
            binding.imgbtnMap5,
            binding.imgbtnMap6,
            binding.imgbtnMap7,
            binding.imgbtnMap8,
            binding.imgbtnMap9,
            binding.imgbtnMap10,
            binding.imgbtnMap11,
            binding.imgbtnMap12,
            binding.imgbtnMap13,
            binding.imgbtnMap14,
            binding.imgbtnMap15,
            binding.imgbtnMap16,
            binding.imgbtnMap17,
            binding.imgbtnMap18,
            binding.imgbtnMap19,
            binding.imgbtnMap20,
            binding.imgbtnMap21,
            binding.imgbtnMap22,
            binding.imgbtnMap23,
            binding.imgbtnMap24,
            binding.imgbtnMap25
        )


        val color_change_btn: Button = binding.colorChangeBtn
        val attack_btn_intent: Button = binding.attackBtnIntent


        var colorType = 1

        for (i in 1..25) {
            imgBtnArr[i].visibility = View.INVISIBLE
        }

//
        val db = FirebaseDatabase.getInstance().getReference("Rooms")


        db.child(flag.getRoomNum().toString()).get().addOnSuccessListener {

            landNum = it.child("landNum").value.toString().toInt()
            playerNum = it.child("players").value.toString().toInt()
            mapString = it.child("mapString").value.toString()

            if (intent.hasExtra("start")) {

                val tmpCnt = Array<Int>(30) { 0 }
                for (i in 1..26) {
                    tmpCnt[i] = 0
                }
                val secureRandom = SecureRandom()
                var tmpIdx: Int
                for (i in playerNum + 1..landNum) {
                    while (true) {
                        tmpIdx = secureRandom.nextInt(playerNum) + 1
                        if (tmpCnt[tmpIdx] < (landNum / playerNum) - 1) {
                            mapString = MapHandling().UpdateMap(mapString, tmpIdx, i)
                            tmpCnt[tmpIdx]++
                            break
                        }
                    }
                }


                db.child(flag.getRoomNum().toString()).child("mapString")
                    .setValue(mapString)
                    .addOnSuccessListener {
                        db.child(flag.getRoomNum().toString()).child("gameStarted")
                            .setValue(true)
                    }


            }

            setMap(landNum, imgBtnArr)
            showMap(mapString, colorType, imgBtnArr)


            color_change_btn.setOnClickListener {


                colorType = colorType % 2 + 1
                if (colorType == 1) {
                    color_change_btn.text = "색 벗기기"
                    showMap(mapString, colorType, imgBtnArr)
                }
                if (colorType == 2) {
                    color_change_btn.text = "색 입히기"
                    showMap(mapString, colorType, imgBtnArr)
                }
            }
            ///

            attack_btn_intent.setOnClickListener {


                if (MapHandling().getLandCount(mapString, userId) == 0) {
                    Toast.makeText(this, "영역이 존재하지 않아 공격 할 수 없습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    val intent = Intent(this, AtcMapActivity::class.java)

                    intent.putExtra("userId", userId)
                    intent.putExtra("mapString", mapString)
                    intent.putExtra("playerNum", playerNum)
                    intent.putExtra("landNum", landNum)

                    startActivity(intent)
                    overridePendingTransition(0, 0)
                    finish()
                }
            }

            //
        }


    }


}