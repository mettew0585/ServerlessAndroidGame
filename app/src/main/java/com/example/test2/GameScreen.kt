package com.example.test2

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log.d
import android.util.TypedValue
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.*
import androidx.core.view.drawToBitmap
import androidx.lifecycle.Transformations.map
import com.example.joystick.JoystickView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_game_screen.*
import kotlinx.android.synthetic.main.activity_game_screen.view.*
import kotlinx.android.synthetic.main.activity_signup.*
import java.util.*

class GameScreen : AppCompatActivity() {


    val dx : Array<Int> =  arrayOf(-1,0,1,0)
    val dy : Array<Int> =  arrayOf(0,1,0,-1)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_screen)


        val paint = Paint()
        paint.setColor(Color.BLACK)
        paint.style=Paint.Style.FILL



        val flag = application as FlagClass
        val screen = findViewById<FrameLayout>(R.id.screen)
        screen.addView(MySurfaceView(this))

        val img_myself=findViewById<ImageView>(R.id.img_myself)
        val img_opponent=findViewById<ImageView>(R.id.img_opponent)
        img_myself.setImageResource(flag.images[0])
        img_opponent.setImageResource(flag.images[2])

        flag.setY(10)
        flag.setX(10)
        flag.brd[10][10]=3

        val skill1 =findViewById<Button>(R.id.skill1)
        skill1.setOnClickListener{
            val cx= flag.getX()?.toInt()
            val cy= flag.getY()?.toInt()

            for(i in -2..2)
                for(j in -2..2)
                {
                    val nx= cx?.plus(i)
                    val ny= cy?.plus(j)

                    if(nx!=null && ny!=null){
                        if(InRange(nx,ny)){
                            flag.brd[nx][ny]=3
                        }
                    }
                }

        }

/*
        val gn = "-1"
        FirebaseDatabase.getInstance().getReference("Games").child(gn).child(flag.getEmail().toString())
            .child("opponent").get().addOnSuccessListener {
                val opponent=it.value.toString()

                FirebaseDatabase.getInstance().getReference("Games").child(gn).child(opponent).child("position")
                    .addValueEventListener(object : ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            TODO("Not yet implemented")
                            val x=snapshot.child("x").value.toString().toInt()
                            val y=snapshot.child("y").value.toString().toInt()

                            flag.brd[x][y]=4
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                    })
            }


         */





        val joystickView =findViewById<JoystickView>(R.id.joystick)


               joystickView.setOnMoveListener({ angle, strength ->



                   if(angle>=45&&angle<135){
                       //up
                       var x= flag.getX()
                       var y= flag.getY()
                       if (y != null && x!=null) {
                           if(x>0) {
                               val prev = flag.brd[x][y]
                               x--
                               flag.setX(x)
                               if(prev == 3 && flag.brd[x][y] == 1){
                                   fun1(x,y)
                               }
                               else if (flag.brd[x][y] != 1) {
                                   flag.brd[x][y] = 3
                                   BFS1()
                               }
                           }
                       }


                   }else if(angle>=135&&angle<225){
                       //left
                       var x=flag.getX()
                       var y=flag.getY()

                       if (x != null && y!=null) {

                           if(y>0 ) {
                               val prev = flag.brd[x][y]
                               y--
                               flag.setY(y)

                               if(prev == 3 && flag.brd[x][y] == 1){
                                   fun1(x,y)
                               }
                               else if (flag.brd[x][y] != 1) {
                                   flag.brd[x][y] = 3
                                   BFS1()
                               }
                           }
                       }

                   }else if(angle>=225&&angle<315) {
                       //down

                       var y=flag.getY()
                       var x=flag.getX()

                       if (y != null&&x!=null) {

                           if(x<39) {
                               val prev = flag.brd[x][y]
                               x++
                               flag.setX(x)
                               if(prev == 3 && flag.brd[x][y] == 1){
                                   fun1(x,y)
                               }
                               else if (flag.brd[x][y] != 1) {
                                   flag.brd[x][y]=3
                                   BFS1()
                               }
                           }

                       }

                   }else if(angle>=315||angle<=405){
                       //right
                       var x=flag.getX()
                       var y=flag.getY()

                       if (x != null&&y!=null) {


                           if(y<29) {
                               val prev = flag.brd[x][y]
                               y++
                               flag.setY(y)

                               if(prev == 3 && flag.brd[x][y] == 1){
                                   fun1(x,y)
                               }
                               else if (flag.brd[x][y] != 1) {
                                   flag.brd[x][y]=3
                                   BFS1()
                               }
                           }

                       }

                   }

               }, 75)

    }



    fun spToPx(sp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            sp,
            context.resources.displayMetrics
        ).toInt()
    }

    fun InRange(x:Int,y:Int):Boolean{
        return (x>=0&&x<40&&y>=0&&y<30)
    }

    fun fun1(x:Int,y:Int){


        val flag = application as FlagClass
        val l = LinkedList<Pair<Int,Int>>()
        val q = LinkedList<Pair<Int,Int>>()
        val vis = Array(40,{BooleanArray(30,{false})})

        l.add(Pair<Int,Int>(x,y))
        q.add(Pair<Int,Int>(x,y))
        vis[x][y]=true
        flag.brd[x][y]=1

        while(!q.isEmpty())
        {
            val cx=q[0].first
            val cy=q[0].second
            q.removeAt(0)

            for(dir in 0..3){
                val nx=cx+dx[dir]
                val ny=cy+dy[dir]

                if(!InRange(nx,ny)||vis[nx][ny]||flag.brd[nx][ny]!=3)
                    continue

                l.add(Pair<Int,Int>(nx,ny))
                q.add(Pair<Int,Int>(nx,ny))
                vis[nx][ny]=true
                flag.brd[nx][ny]=1
            }
        }
    }

    fun BFS1(){

        val vis = Array(40,{BooleanArray(30,{false})})
        val vis1 = Array(40,{BooleanArray(30,{false})})
        val flag = application as FlagClass
        val pnt_myself=findViewById<ProgressBar>(R.id.pnt_myself)
        var count = 0


        for(x in 0..39)
            for(y in 0..29)
            {
                if(flag.brd[x][y]==1)
                    count++

                if(vis[x][y] || flag.brd[x][y]!=-1)
                    continue


                val l = LinkedList<Pair<Int,Int>>()
                val q = LinkedList<Pair<Int,Int>>()
                var b : Boolean = true

                l.add(Pair<Int,Int>(x,y))
                q.add(Pair<Int,Int>(x,y))
                vis[x][y]=true

                if(x==0||x==39||y==0||y==29)
                    b = false


                while(!q.isEmpty()){

                    val cx=q[0].first
                    val cy=q[0].second
                    q.removeAt(0)


                    for(dir in 0..3){
                        val nx=cx+dx[dir]
                        val ny=cy+dy[dir]

                        if(!InRange(nx,ny))
                            continue

                        if(flag.brd[nx][ny]==2||flag.brd[nx][ny]==4)
                            b = false

                        if(!vis[nx][ny] && flag.brd[nx][ny]==-1){

                            if(nx==0||nx==39||ny==0||ny==29)
                                b = false


                            l.add(Pair<Int,Int>(nx,ny))
                            q.add(Pair<Int,Int>(nx,ny))
                            vis[nx][ny]=true
                        }

                    }
                }

                if(b){
                    for( a in l){
                        flag.brd[a.first][a.second]=3
                    }
                    for(x in 0..39)
                        for(y in 0..29)
                            if(flag.brd[x][y]==3)
                                flag.brd[x][y]=1
                }

            }

        pnt_myself.setProgress(count)


    }
    fun BFS2(){

        val vis = Array(40,{BooleanArray(30,{false})})
        val vis1 = Array(40,{BooleanArray(30,{false})})
        val flag = application as FlagClass
        val pnt_opponent=findViewById<ProgressBar>(R.id.pnt_opponent)
        var count = 0


        for(x in 0..39)
            for(y in 0..29)
            {
                if(flag.brd[x][y]==2)
                    count++

                if(vis[x][y] || flag.brd[x][y]!=-1)
                    continue


                val l = LinkedList<Pair<Int,Int>>()
                val q = LinkedList<Pair<Int,Int>>()
                var b : Boolean = true

                l.add(Pair<Int,Int>(x,y))
                q.add(Pair<Int,Int>(x,y))
                vis[x][y]=true

                if(x==0||x==39||y==0||y==29)
                    b = false


                while(!q.isEmpty()){

                    val cx=q[0].first
                    val cy=q[0].second
                    q.removeAt(0)


                    for(dir in 0..3){
                        val nx=cx+dx[dir]
                        val ny=cy+dy[dir]

                        if(!InRange(nx,ny))
                            continue

                        if(flag.brd[nx][ny]==1||flag.brd[nx][ny]==3)
                            b = false

                        if(!vis[nx][ny] && flag.brd[nx][ny]==-1){

                            if(nx==0||nx==39||ny==0||ny==29)
                                b = false


                            l.add(Pair<Int,Int>(nx,ny))
                            q.add(Pair<Int,Int>(nx,ny))
                            vis[nx][ny]=true
                        }

                    }
                }

                if(b){
                    for( a in l){
                        flag.brd[a.first][a.second]=4
                    }
                    for(x in 0..39)
                        for(y in 0..29)
                            if(flag.brd[x][y]==4)
                                flag.brd[x][y]=2
                }

            }

        pnt_opponent.setProgress(count)


    }

}

