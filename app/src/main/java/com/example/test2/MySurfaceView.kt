package com.example.test2

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.util.TypedValue
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_game_screen.view.*


class MySurfaceView(context : Context?) :
    SurfaceView(context),
    SurfaceHolder.Callback,
    Runnable {

    //private val img : ImageView = R.drawable.character1
    private var surfaceHolder: SurfaceHolder
    private var paint: Paint
    private lateinit var thread: Thread
    private var threadRunning = false
    private lateinit var canvas: Canvas
    private var k = -1
    private lateinit var tile1: Bitmap
    private var unit: Float


    init {

        surfaceHolder = holder
        surfaceHolder.addCallback(this)

        holder.setFormat(PixelFormat.TRANSLUCENT)
        setZOrderOnTop(true)


        paint = Paint()
        paint.setColor(Color.BLUE)
        paint.style = Paint.Style.FILL


        k = context?.let { spToPx(1f, it) }!!
        unit = (k * 10).toFloat()

    }


    override fun surfaceCreated(p0: SurfaceHolder) {
        Toast.makeText(context, "game start", Toast.LENGTH_SHORT).show()


/*
        val background = BitmapFactory.decodeResource(context.resources,R.drawable.character1)
        val scale = background.height as Float / height.toFloat()
        val newWidth = Math.round(background.width / scale).toInt()
        val newHeight = Math.round(background.height / scale).toInt()
        val scaled = Bitmap.createScaledBitmap(background, newWidth, newHeight, true)
*/


        // Create the child thread when SurfaceView is created.
        thread = Thread(this)
        // Start to run the child thread.
        thread!!.start()
        // Set thread running flag to true.
        threadRunning = true

    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {

    }

    override fun surfaceDestroyed(p0: SurfaceHolder) {
        threadRunning = false
    }


    private fun draw() {

        /*
        val flag = context.applicationContext as FlagClass
        val cx = flag.getX()
        val cy = flag.getY()
        val x : Float? = cx?.toFloat()
        val y : Float? = cy?.toFloat()

         */

        val flag = context.applicationContext as FlagClass

        for (x in 0..39)
            for (y in 0..29) {
                if (flag.brd[x][y] == 1)
                    canvas.drawRect(y*unit, x * unit, (y + 1) * unit, (x + 1) * unit, paint)
            }

    }


    override fun run() {
        while (threadRunning) {
            try {
                canvas = surfaceHolder.lockCanvas()
                synchronized(surfaceHolder) {
                    draw()
                }

            } finally {
                surfaceHolder!!.unlockCanvasAndPost(canvas)
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
}