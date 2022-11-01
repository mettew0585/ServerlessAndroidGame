package com.example.test2

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.TypedValue
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.example.joystick.JoystickView
import com.example.test2.databinding.ActivityGameScreenBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_game_screen.*
import java.util.*

class GameScreen : AppCompatActivity() {

    private lateinit var binding : ActivityGameScreenBinding

    var opp_num : Int = -1
    val dx: Array<Int> = arrayOf(-1, 0, 1, 0)
    val dy: Array<Int> = arrayOf(0, 1, 0, -1)
    var pre_x: Int = 0
    var pre_y: Int = 0

    fun init(){
        var brd =  Array<Array<Int>>(40,{Array(30,{-1})})
        FirebaseDatabase.getInstance().getReference("Games").child("9631").child("mapString").setValue(brd)
    }

    fun numToPos(num: Int): Pair<Int, Int> {
        return Pair<Int, Int>(num / 30, num % 30)
    }

    fun mapToString(): String {

        val flag = application as FlagClass
        var str: String = ""

        for (x in 0..39)
            for (y in 0..29)
                str += flag.brd[x][y]

        return str
    }

    fun StringToMap( mapString  : String ) {

        val flag = application as FlagClass

        val str = mapString.chunked(30)

        for(x in 0..39)
            for(y in 0..29) {

                var tmp : Int = 5

                if(str[x][y]=='1')
                    tmp = 2
                else if(str[x][y]=='2')
                    tmp = 1
                else if(str[x][y]=='3')
                    tmp=4
                else if(str[x][y]=='4')
                    tmp=3

                flag.brd[x][y] = tmp
            }


        val k=3
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_screen)

        binding = ActivityGameScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name_myself= binding.nameMyself
        val name_opp = binding.nameOpponent



        val paint = Paint()
        paint.setColor(Color.BLACK)
        paint.style = Paint.Style.FILL


        val flag = application as FlagClass
        val screen = findViewById<FrameLayout>(R.id.screen)
        screen.addView(MySurfaceView(this))

        val img_myself = findViewById<ImageView>(R.id.img_myself)
        val img_opponent = findViewById<ImageView>(R.id.img_opponent)
        img_myself.setImageResource(flag.images[0])
        img_opponent.setImageResource(flag.images[2])


        val userDB=FirebaseDatabase.getInstance().getReference("Users")
        val userDB2=FirebaseDatabase.getInstance().getReference("Users")
        val roomDB=FirebaseDatabase.getInstance().getReference("Rooms")
        val roomDB2=FirebaseDatabase.getInstance().getReference("Rooms")
        val gameDB=FirebaseDatabase.getInstance().getReference("Games")


        userDB.child(flag.getEmail().toString()).get().addOnSuccessListener {

            flag.setUserId(it.child("userId").value.toString().toInt())
            name_myself.setText(it.child("userName").value.toString())



            roomDB.child(flag.getRoomNum().toString()).child("emails").child(it.child("userId").value.toString()).child("opponent")
                .get().addOnSuccessListener {
                    opp_num= it.value.toString().toInt()

                    roomDB2.child(flag.getRoomNum().toString()).child("emails").child(opp_num.toString()).child("email")
                        .get().addOnSuccessListener {
                            val opp_email = it.value.toString()

                            userDB2.child(opp_email).child("userName").get().addOnSuccessListener {
                                name_opp.setText(it.value.toString())
                            }
                        }


                    flag.setX(20)
                    flag.setY(20)
                    //방장이 아닐 때는 그냥 DB에서 받아서 보여주기만 하면됨

                    if(flag.getEmail()=="aa@a"){

                        val key = flag.getRoomNum().toString() + opp_num.toString()+ flag.getUserId().toString()
                        val userDB=FirebaseDatabase.getInstance().getReference("Users")
                        val roomDB=FirebaseDatabase.getInstance().getReference("Rooms")
                        val gameDB=FirebaseDatabase.getInstance().getReference("Games")


                        //좌표값 업데이트 해주기
                        //map 받아서 업데이트 보여주기

                        gameDB.child(key).child("mapString").addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {

                                val str : String = snapshot.value.toString()

                                StringToMap(str)

                            }

                            override fun onCancelled(error: DatabaseError) {
                            }
                        })

                        val joystickView = findViewById<JoystickView>(R.id.joystick)

                        joystickView.setOnMoveListener({ angle, strength ->


                            if (angle >= 45 && angle < 135) {
                                //up
                                var x = flag.getX()
                                var y = flag.getY()
                                if (y != null && x != null) {
                                    if (x > 0) {
                                        x--
                                        flag.setX(x)
                                    }
                                }


                            } else if (angle >= 135 && angle < 225) {
                                //left
                                var x = flag.getX()
                                var y = flag.getY()

                                if (x != null && y != null) {

                                    if (y > 0) {
                                        y--
                                        flag.setY(y)
                                    }
                                }

                            } else if (angle >= 225 && angle < 315) {
                                //down

                                var y = flag.getY()
                                var x = flag.getX()

                                if (y != null && x != null) {

                                    if (x < 39) {
                                        x++
                                        flag.setX(x)
                                    }

                                }

                            } else if (angle >= 315 || angle <= 405) {
                                //right
                                var x = flag.getX()
                                var y = flag.getY()

                                if (x != null && y != null) {


                                    if (y < 29) {
                                        y++
                                        flag.setY(y)
                                    }

                                }

                            }

                            roomDB.child(flag.getRoomNum().toString()).child("emails").child(flag.getUserId().toString())
                                .child("x").setValue(flag.getX())
                            roomDB.child(flag.getRoomNum().toString()).child("emails").child(flag.getUserId().toString())
                                .child("y").setValue(flag.getY())

                        }, 75)


                    }
                    //


                    //방장일 때 실행되는 코드
                    else {
                        flag.setY(10)
                        flag.setX(10)
                        flag.brd[10][10] = 3

                        val skill1 = findViewById<Button>(R.id.skill1)
                        skill1.setOnClickListener {
                            val cx = flag.getX()?.toInt()
                            val cy = flag.getY()?.toInt()

                            for (i in -2..2)
                                for (j in -2..2) {
                                    val nx = cx?.plus(i)
                                    val ny = cy?.plus(j)

                                    if (nx != null && ny != null) {
                                        if (InRange(nx, ny)) {
                                            flag.brd[nx][ny] = 3
                                        }
                                    }
                                }

                        }


                        val roomDB = FirebaseDatabase.getInstance().getReference("Rooms")
                            .child(flag.getRoomNum().toString())
                        val roomDB2 = FirebaseDatabase.getInstance().getReference("Rooms")
                            .child(flag.getRoomNum().toString())
                        val userDB = FirebaseDatabase.getInstance().getReference("Users")

                        userDB.child(flag.getEmail().toString()).child("userId").get().addOnSuccessListener {
                            roomDB.child("emails").child(it.value.toString()).child("opponent").get()
                                .addOnSuccessListener {
                                    roomDB2.child("emails").child(it.value.toString())
                                        .addValueEventListener(object : ValueEventListener {
                                            override fun onDataChange(snapshot: DataSnapshot) {


                                                val x = snapshot.child("x").value.toString().toInt()
                                                val y = snapshot.child("y").value.toString().toInt()

                                                if (pre_x != x || pre_y != y) {
                                                    flag.setOx(x)
                                                    flag.setOy(y)
                                                }

                                                flag.brd[x][y] = 4


                                                val prev = flag.brd[pre_x][pre_y]

                                                if (prev == 4 && flag.brd[x][y] == 2) {
                                                    fun2(x, y)
                                                }

                                                pre_x = x;
                                                pre_y = y;

                                                BFS2()

                                            }

                                            override fun onCancelled(error: DatabaseError) {
                                            }
                                        })

                                }
                        }


                        val joystickView = findViewById<JoystickView>(R.id.joystick)


                        joystickView.setOnMoveListener({ angle, strength ->


                            if (angle >= 45 && angle < 135) {
                                //up
                                var x = flag.getX()
                                var y = flag.getY()
                                if (y != null && x != null) {
                                    if (x > 0) {
                                        val prev = flag.brd[x][y]
                                        x--
                                        flag.setX(x)
                                        if (prev == 3 && flag.brd[x][y] == 1) {
                                            fun1(x, y)
                                        } else if (flag.brd[x][y] != 1) {
                                            flag.brd[x][y] = 3
                                            BFS1()
                                        }
                                    }
                                }


                            } else if (angle >= 135 && angle < 225) {
                                //left
                                var x = flag.getX()
                                var y = flag.getY()

                                if (x != null && y != null) {

                                    if (y > 0) {
                                        val prev = flag.brd[x][y]
                                        y--
                                        flag.setY(y)

                                        if (prev == 3 && flag.brd[x][y] == 1) {
                                            fun1(x, y)
                                        } else if (flag.brd[x][y] != 1) {
                                            flag.brd[x][y] = 3
                                            BFS1()
                                        }
                                    }
                                }

                            } else if (angle >= 225 && angle < 315) {
                                //down

                                var y = flag.getY()
                                var x = flag.getX()

                                if (y != null && x != null) {

                                    if (x < 39) {
                                        val prev = flag.brd[x][y]
                                        x++
                                        flag.setX(x)
                                        if (prev == 3 && flag.brd[x][y] == 1) {
                                            fun1(x, y)
                                        } else if (flag.brd[x][y] != 1) {
                                            flag.brd[x][y] = 3
                                            BFS1()
                                        }
                                    }

                                }

                            } else if (angle >= 315 || angle <= 405) {
                                //right
                                var x = flag.getX()
                                var y = flag.getY()

                                if (x != null && y != null) {


                                    if (y < 29) {
                                        val prev = flag.brd[x][y]
                                        y++
                                        flag.setY(y)

                                        //3에서 1로 들어갈때 실행되는 함수
                                        //무슨 함수냐? 3을 1로 바꿔주는 함수!
                                        //굳이 bfs가 필요할까?
                                        if (prev == 3 && flag.brd[x][y] == 1) {
                                            fun1(x, y)
                                        }//3이 아직 확정안된거고 1은 아예 확정된거임
                                        else if (flag.brd[x][y] != 1) {
                                            flag.brd[x][y] = 3
                                            BFS1()
                                        }
                                    }

                                }

                            }

                        }, 75)
                    }
                }



        }


    }


    fun spToPx(sp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            sp,
            context.resources.displayMetrics
        ).toInt()
    }

    fun InRange(x: Int, y: Int): Boolean {
        return (x >= 0 && x < 40 && y >= 0 && y < 30)
    }

    fun fun1(x: Int, y: Int) {


        val flag = application as FlagClass
        val key = flag.getRoomNum().toString() + flag.getUserId().toString() + opp_num.toString()
        val l = LinkedList<Pair<Int, Int>>()
        val q = LinkedList<Pair<Int, Int>>()
        val vis = Array(40, { BooleanArray(30, { false }) })

        l.add(Pair<Int, Int>(x, y))
        q.add(Pair<Int, Int>(x, y))
        vis[x][y] = true
        flag.brd[x][y] = 1

        //bfs?

        while (!q.isEmpty()) {
            val cx = q[0].first
            val cy = q[0].second
            q.removeAt(0)

            for (dir in 0..3) {
                val nx = cx + dx[dir]
                val ny = cy + dy[dir]

                if (!InRange(nx, ny) || vis[nx][ny] || flag.brd[nx][ny] != 3)
                    continue

                l.add(Pair<Int, Int>(nx, ny))
                q.add(Pair<Int, Int>(nx, ny))
                vis[nx][ny] = true
                flag.brd[nx][ny] = 1
            }
        }
        //
        val gameDB =
            FirebaseDatabase.getInstance().getReference("Games").child(key).child("mapString")

        gameDB.setValue(mapToString())

        //
    }

    fun BFS1() {

        val flag = application as FlagClass
        val key = flag.getRoomNum().toString() + flag.getUserId().toString() + opp_num.toString()
        val vis = Array(40, { BooleanArray(30, { false }) })
        val vis1 = Array(40, { BooleanArray(30, { false }) })
        val pnt_myself = findViewById<ProgressBar>(R.id.pnt_myself)
        var count = 0


        for (x in 0..39)
            for (y in 0..29) {

                if (flag.brd[x][y] == 1)
                    count++

                if(vis[x][y] || flag.brd[x][y] == 1 || flag.brd[x][y] == 3 || flag.brd[x][y]==2 ||flag.brd[x][y]==4)
                    continue


                val l = LinkedList<Pair<Int, Int>>()
                val q = LinkedList<Pair<Int, Int>>()
                var b: Boolean = true

                l.add(Pair<Int, Int>(x, y))
                q.add(Pair<Int, Int>(x, y))
                vis[x][y] = true

                if (x == 0 || x == 39 || y == 0 || y == 29)
                    b = false


                while (!q.isEmpty()) {

                    val cx = q[0].first
                    val cy = q[0].second
                    q.removeAt(0)


                    for (dir in 0..3) {
                        val nx = cx + dx[dir]
                        val ny = cy + dy[dir]

                        if (!InRange(nx, ny))
                            continue


                        if (!vis[nx][ny] && flag.brd[nx][ny] == 5) {

                            if (nx == 0 || nx == 39 || ny == 0 || ny == 29)
                                b = false


                            l.add(Pair<Int, Int>(nx, ny))
                            q.add(Pair<Int, Int>(nx, ny))
                            vis[nx][ny] = true
                        }

                    }
                }

                if (b) {
                    for (a in l) {
                        flag.brd[a.first][a.second] = 3
                    }
                    for (x in 0..39)
                        for (y in 0..29)
                            if (flag.brd[x][y] == 3)
                                flag.brd[x][y] = 1
                }

            }

        //
        val gameDB =
            FirebaseDatabase.getInstance().getReference("Games").child(key).child("mapString")

        gameDB.setValue(mapToString()).addOnSuccessListener {

        }.addOnFailureListener {

        }


        //


        pnt_myself.setProgress(count)
    }

    fun fun2(x: Int, y: Int) {


        val flag = application as FlagClass
        val key = flag.getRoomNum().toString() + opp_num.toString() + flag.getUserId().toString()
        val l = LinkedList<Pair<Int, Int>>()
        val q = LinkedList<Pair<Int, Int>>()
        val vis = Array(40, { BooleanArray(30, { false }) })

        l.add(Pair<Int, Int>(x, y))
        q.add(Pair<Int, Int>(x, y))
        vis[x][y] = true
        flag.brd[x][y] = 2

        while (!q.isEmpty()) {
            val cx = q[0].first
            val cy = q[0].second
            q.removeAt(0)

            for (dir in 0..3) {
                val nx = cx + dx[dir]
                val ny = cy + dy[dir]

                if (!InRange(nx, ny) || vis[nx][ny] || flag.brd[nx][ny] != 4)
                    continue

                l.add(Pair<Int, Int>(nx, ny))
                q.add(Pair<Int, Int>(nx, ny))
                vis[nx][ny] = true
                flag.brd[nx][ny] = 2
            }
        }

    }


    fun BFS2() {



        val flag = application as FlagClass
        val key = flag.getRoomNum().toString() + opp_num.toString()+ flag.getUserId().toString()
        val vis = Array(40, { BooleanArray(30, { false }) })
        val vis1 = Array(40, { BooleanArray(30, { false }) })
        val pnt_myself = findViewById<ProgressBar>(R.id.pnt_opponent)
        var count = 0


        for (x in 0..39)
            for (y in 0..29) {

                if (flag.brd[x][y] == 2)
                    count++

                if(vis[x][y] || flag.brd[x][y] == 1 || flag.brd[x][y] == 3 || flag.brd[x][y]==2 ||flag.brd[x][y]==4)
                    continue


                val l = LinkedList<Pair<Int, Int>>()
                val q = LinkedList<Pair<Int, Int>>()
                var b: Boolean = true

                l.add(Pair<Int, Int>(x, y))
                q.add(Pair<Int, Int>(x, y))
                vis[x][y] = true

                if (x == 0 || x == 39 || y == 0 || y == 29)
                    b = false


                while (!q.isEmpty()) {

                    val cx = q[0].first
                    val cy = q[0].second
                    q.removeAt(0)


                    for (dir in 0..3) {
                        val nx = cx + dx[dir]
                        val ny = cy + dy[dir]

                        if (!InRange(nx, ny))
                            continue


                        if (!vis[nx][ny] && flag.brd[nx][ny] == 5) {

                            if (nx == 0 || nx == 39 || ny == 0 || ny == 29)
                                b = false


                            l.add(Pair<Int, Int>(nx, ny))
                            q.add(Pair<Int, Int>(nx, ny))
                            vis[nx][ny] = true
                        }

                    }
                }

                if (b) {
                    for (a in l) {
                        flag.brd[a.first][a.second] = 4
                    }
                    for (x in 0..39)
                        for (y in 0..29)
                            if (flag.brd[x][y] == 4)
                                flag.brd[x][y] = 2
                }

            }
        pnt_opponent.setProgress(count)


    }

}

