package com.example.test2

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class GameScreen : AppCompatActivity() {
    private val images = arrayOf(R.drawable.character1,R.drawable.character2
        ,R.drawable.character3,R.drawable.character4,R.drawable.character5)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_screen)

        val bitmap = Bitmap.createBitmap(30,40,Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)

    }
}