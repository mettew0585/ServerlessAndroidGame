
package com.example.test2

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.myapplication.MapHandling
import com.example.test2.databinding.ActivityAtcMapBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_atc_map.*

class AtcMapActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAtcMapBinding

    var imgNum:Int = -1
    var CHANNEL_ID = "channel_id"
    var CHANNEL_NAME = "channel_name"
    val colorArr=arrayOf(R.color.red_color,R.color.green_color,R.color.orange_color)

    var attackMapString:String="0000000000000000000000000"
    var userId=1
    var mapString:String=""
    var playerNum :Int=1
    var landNum:Int=1

    fun landColorTypeCheck(mapString:String, attackMapString: String,userId: Int,chkIdx:Int) : Int{
        val attackIm=attackMapString.chunked(1)
        val tmp= Integer.parseInt(attackIm[chkIdx-1])

        if(MapHandling().getMapColorToIdx(mapString,userId)==MapHandling().getMapColorToIdx(mapString,chkIdx))
            return 2
        if(tmp==1)
            return 0
        return 1
    }
    fun setMap(imgBtnArr: Array<ImageButton>){

        for(i in 1 .. landNum){
            imgBtnArr[i].visibility=View.VISIBLE
        }
    }
    fun showAtcMap(mapString:String, attackMapString : String, userId:Int, colorType:Int , imgBtnArr : Array<ImageButton>){
        val landColorType= Array<Int>(30){0}

        if(colorType==1) {
            for(curIdx in 1..25) {
                landColorType[curIdx]=landColorTypeCheck(mapString,attackMapString,userId,curIdx)
            }
            for(i in 1..25){
                imgBtnArr[i].setColorFilter(ContextCompat.getColor(this,colorArr[landColorType[i]]))

            }
        }
        if(colorType==2) {
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
        setContentView(R.layout.activity_atc_map)


        binding = ActivityAtcMapBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val flag = application as FlagClass

        val userDB=FirebaseDatabase.getInstance().getReference("Users")
        userDB.child(flag.getEmail().toString()).child("userId").get().addOnSuccessListener {
            flag.setUserId(it.value.toString().toInt())
        }


        /*

        val roomDB=FirebaseDatabase.getInstance().getReference("Rooms")
        roomDB.child(flag.getRoomNum().toString()).child("emails").child(flag.getUserId().toString()).child("message")
            .child("sent").get().addOnSuccessListener {

                Toast.makeText(this@AtcMapActivity,it.value.toString(),Toast.LENGTH_SHORT).show()
            }

        val roomDB=FirebaseDatabase.getInstance().getReference("Rooms")
        roomDB.child(flag.getRoomNum().toString()).child("emails").child(flag.getUserId().toString()).child("message")
            .child("sent").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    Toast.makeText(this@AtcMapActivity,"hi",Toast.LENGTH_SHORT).show()

                   /* if(snapshot.value.toString().toBoolean()==true){
                        Toast.makeText(this@AtcMapActivity,"hi",Toast.LENGTH_SHORT).show()
                    }

                    */
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })

         */

        //


        val color_change_btn=binding.colorChangeBtn
        val imgBtnArr = arrayOf(binding.imgbtnMap1,binding.imgbtnMap1,binding.imgbtnMap2,binding.imgbtnMap3,binding.imgbtnMap4,binding.imgbtnMap5,binding.imgbtnMap6,binding.imgbtnMap7,binding.imgbtnMap8,binding.imgbtnMap9,binding.imgbtnMap10,
            binding.imgbtnMap11,binding.imgbtnMap12,binding.imgbtnMap13,binding.imgbtnMap14,binding.imgbtnMap15,binding.imgbtnMap16,binding.imgbtnMap17,binding.imgbtnMap18,binding.imgbtnMap19,binding.imgbtnMap20,
            binding.imgbtnMap21,binding.imgbtnMap22,binding.imgbtnMap23,binding.imgbtnMap24,binding.imgbtnMap25)
        var colorType=1

        for(i in 1 .. 25){
            imgBtnArr[i].visibility=View.INVISIBLE
        }
        for(i in 1..25){
            imgBtnArr[i].setOnClickListener(AttackMapButtonListener())
        }

        val userDb=FirebaseDatabase.getInstance().getReference("Users")
        val db=FirebaseDatabase.getInstance().getReference("Rooms")




        userDb.child(flag.getEmail().toString()).child("userId").get().addOnSuccessListener {

            userId = it.value.toString().toInt()



            db.child(flag.getRoomNum().toString()).get().addOnSuccessListener {


                mapString = it.child("mapString").value.toString()
                playerNum = it.child("players").value.toString().toInt()
                landNum = it.child("landNum").value.toString().toInt()
                attackMapString=MapHandling().getAttackString(mapString);
                setMap(imgBtnArr)
                showAtcMap(mapString, attackMapString, userId, colorType,imgBtnArr)
                if(imgNum!=-1) {
                    sendNotification(MapHandling().getMapColorToIdx(mapString, imgNum))
                }
                color_change_btn.setOnClickListener {
                    colorType=colorType%2+1
                    if(colorType==1){
                        color_change_btn.text="색 벗기기"
                        showAtcMap(mapString,attackMapString,userId,colorType,imgBtnArr)
                    }
                    if(colorType==2){
                        color_change_btn.text="색 입히기"
                        showAtcMap(mapString,attackMapString,userId,colorType,imgBtnArr)
                    }
                }

                back_btn_intent.setOnClickListener {

                    val intent= Intent(this, MapActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0,0)
                    finish()
                }


            }




        }



    }

    inner class AttackMapButtonListener : View.OnClickListener{
        override fun onClick(v:View?) {
            val intent= Intent(this@AtcMapActivity, GameActivity::class.java)
            val bIntent= Intent(this@AtcMapActivity, MapActivity::class.java)
            val dlg = AttackChoiceDialog(this@AtcMapActivity)
            val flag = application as FlagClass

            imgNum=0
            val db=FirebaseDatabase.getInstance().getReference("Rooms")
            val roomDB=FirebaseDatabase.getInstance().getReference("Rooms")
            dlg.setOnOKClickedListener {
                db.child(flag.getRoomNum().toString()).get().addOnSuccessListener {

                    mapString = it.child("mapString").value.toString()
                    val oppNum=MapHandling().getMapColorToIdx(mapString,imgNum)

                    //sent true로 변경
                    roomDB.child(flag.getRoomNum().toString()).child("emails")
                        .child(oppNum.toString()).child("message").child("sent").setValue(true)
                    //

                    Toast.makeText(this@AtcMapActivity, "공격하였습니다", Toast.LENGTH_SHORT).show()

                    db.child(flag.getRoomNum().toString()).child("emails").child(userId.toString()).child("opponent").setValue(imgNum)
                    MapHandling().changeAttackString(mapString,userId,imgNum,1)
                    startActivity(intent)
                    overridePendingTransition(0,0)
                    finish()
                }
            }//예 클릭시 실행
            when(v?.id){
                R.id.imgbtn_map1->imgNum=1
                R.id.imgbtn_map2->imgNum=2
                R.id.imgbtn_map3->imgNum=3
                R.id.imgbtn_map4->imgNum=4
                R.id.imgbtn_map5->imgNum=5
                R.id.imgbtn_map6->imgNum=6
                R.id.imgbtn_map7->imgNum=7
                R.id.imgbtn_map8->imgNum=8
                R.id.imgbtn_map9->imgNum=9
                R.id.imgbtn_map10->imgNum=10
                R.id.imgbtn_map11->imgNum=11
                R.id.imgbtn_map12->imgNum=12
                R.id.imgbtn_map13->imgNum=13
                R.id.imgbtn_map14->imgNum=14
                R.id.imgbtn_map15->imgNum=15
                R.id.imgbtn_map16->imgNum=16
                R.id.imgbtn_map17->imgNum=17
                R.id.imgbtn_map18->imgNum=18
                R.id.imgbtn_map19->imgNum=19
                R.id.imgbtn_map20->imgNum=20
                R.id.imgbtn_map21->imgNum=21
                R.id.imgbtn_map22->imgNum=22
                R.id.imgbtn_map23->imgNum=23
                R.id.imgbtn_map24->imgNum=24
                R.id.imgbtn_map25->imgNum=25
            }
            val landTypeNum=landColorTypeCheck(mapString,attackMapString,userId,imgNum)
            if(landTypeNum==0){
                Toast.makeText(this@AtcMapActivity,"현재 전투 중 입니다.",Toast.LENGTH_SHORT).show()
                startActivity(bIntent)
                overridePendingTransition(0,0)
                finish()
            }else if(landTypeNum==1){
                dlg.start("공격하시겠습니까?")
            }
            else{
                Toast.makeText(this@AtcMapActivity,"본인 땅은 공격할 수 없습니다.",Toast.LENGTH_SHORT).show()
                startActivity(bIntent)
                overridePendingTransition(0,0)
                finish()
            }
        }
    }
    private fun sendNotification(opNum:Int) {
        //val title = binding.edtTitle.text.toString()
        //val message = binding.edtMessage.text.toString()

        val flag = application as FlagClass
        val notificationManager = getSystemService(NOTIFICATION_SERVICE)
                as NotificationManager
        val notificationID = flag.getRoomNum()?.toInt()

        Toast.makeText(this,"${notificationID}",Toast.LENGTH_SHORT).show()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        CHANNEL_ID=CHANNEL_ID+flag.getRoomNum().toString()
        CHANNEL_NAME=CHANNEL_NAME+flag.getRoomNum().toString()

        val Wintent= Intent(this, MapActivity::class.java)
        Wintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) // 액티비티 중복 생성 방지
        Wintent.putExtra("ATCCheckStart","")
        Wintent.putExtra("player1",opNum)
        Wintent.putExtra("player2",userId)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Wintent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_MUTABLE
        )
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("공격당함")
            .setContentText("조현우")
            .setSmallIcon(R.drawable.planet1)
            .setAutoCancel(true)
            //.setContentIntent(pendingIntent)
            .build()

        val roomDB = FirebaseDatabase.getInstance().getReference("Rooms")

        if (notificationID != null) {

            roomDB.child(flag.getRoomNum().toString()).child("emails").child(flag.getUserId().toString()).child("message")
                .child("sent").addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val k=3
                        if(snapshot.value.toString().toBoolean()==true){
                            Toast.makeText(this@AtcMapActivity,"hi",Toast.LENGTH_SHORT).show()
                            //notificationManager.notify(notificationID, notification)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })


        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH).apply {
            description = "Channel Description"
            enableLights(true)
            lightColor = R.color.color1
        }
        notificationManager.createNotificationChannel(channel)
    }
}