package com.example.main

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class GameView(context : Context?, attrs : AttributeSet?) : View(context, attrs){

    private var circleRadius = 20F
    private var point= Point(0, 0)

    // unser "Zeichenstift"
    private val paint = Paint()

    init {
        // Touch enablen
        isFocusable = true
        isFocusableInTouchMode = true

        // Zeichenstift konfigurieren
        paint.color = Color.YELLOW
    }

    fun setCircleRadius(radius: Float) {
        circleRadius = radius
    }

    fun setCircleColor(color: Int) {
        paint.color = color
    }

    fun setPoint(x: Int, y: Int) {
        point.x = x
        point.y = y
    }

    override fun onDraw(canvas: Canvas) {
        val height = canvas.height
        val width = canvas.width

        // Zeichne Kreis am Punkt Point
        canvas.drawCircle(point.x.toFloat(), point.y.toFloat(), circleRadius, paint)
    }
}
