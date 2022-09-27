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
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_game_screen.*
import kotlinx.android.synthetic.main.activity_game_screen.view.*
import java.util.*

class GameScreen : AppCompatActivity() {
    /*
    private val images = arrayOf(
        R.drawable.character1,
        R.drawable.character2,
        R.drawable.character3,
        R.drawable.character4,
        R.drawable.character5
    )
*/

    val dx : Array<Int> = arrayOf(-1,0,1,0)
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


        val tile1 : Bitmap =BitmapFactory.decodeResource(resources,R.drawable.tile1)

        val k=spToPx(1.0F,this)



        flag.setY(10)
        flag.setX(10)
        flag.brd[10][10]=1




        val joystickView =findViewById<JoystickView>(R.id.joystick)


               joystickView.setOnMoveListener({ angle, strength ->



                   if(angle>=45&&angle<135){
                       //up
                       var x= flag.getX()
                       var y= flag.getY()
                       if (y != null && x!=null) {
                           if(x>0) {
                               x--
                               flag.setX(x)
                               flag.brd[x][y]=1
                           }
                       }


                   }else if(angle>=135&&angle<225){
                       //left
                       var x=flag.getX()
                       var y=flag.getY()

                       if (x != null && y!=null) {
                           if(y>0 ) {
                               y--
                               flag.setY(y)
                               flag.brd[x][y]=1
                           }
                       }

                   }else if(angle>=225&&angle<315) {
                       //down

                       var y=flag.getY()
                       var x=flag.getX()

                       if (y != null&&x!=null) {
                           if(x<39) {
                               x++
                               flag.setX(x)
                               flag.brd[x][y]=1
                           }
                       }

                   }else if(angle>=315||angle<=405){
                       //right
                       var x=flag.getX()
                       var y=flag.getY()

                       if (x != null&&y!=null) {
                           if(y<29) {
                               y++
                               flag.setY(y)
                               flag.brd[x][y]=1
                           }
                       }

                   }

                   BFS()

               }, 50)

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

    fun BFS(){

        val vis = Array(40,{BooleanArray(30,{false})})
        val flag = application as FlagClass

        for(x in 0..39)
            for(y in 0..29)
            {
                if(vis[x][y]||flag.brd[x][y]==1)
                    continue

                val l = LinkedList<Pair<Int,Int>>()
                val q = LinkedList<Pair<Int,Int>>()
                var b : Boolean = true

                l.add(Pair<Int,Int>(x,y))
                q.add(Pair<Int,Int>(x,y))
                vis[x][y]=true
                if(x==0||x==39||y==0||y==29)
                    b=false

                while(!q.isEmpty()){
                    val cx=q[0].first
                    val cy=q[0].second
                    q.removeAt(0)


                    for(dir in 0..3){
                        val nx=cx+dx[dir]
                        val ny=cy+dy[dir]

                        if(InRange(nx,ny) && !vis[nx][ny] && flag.brd[nx][ny]==-1){

                            if(nx==0||nx==39||ny==0||ny==29){
                                b=false
                            }

                            l.add(Pair<Int,Int>(nx,ny))
                            q.add(Pair<Int,Int>(nx,ny))
                            vis[nx][ny]=true
                        }


                    }
                }

                if(b){
                    for( a in l){
                        flag.brd[a.first][a.second]=1
                    }
                }

            }
    }

}

