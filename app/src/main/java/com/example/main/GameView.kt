package com.example.main

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
class GameView(context : Context?, attrs : AttributeSet?) : View(context, attrs){

    private var circleRadius = 50F
    private var circlePos = Point(100, 100)

    // unser "Zeichenstift"
    private val paint = Paint()

    init {
        // Touch für den View enablen
        isFocusable = true
        isFocusableInTouchMode = true

        // Zeichenstift konfigurieren
        paint.color = Color.YELLOW
    }

    // setter Funktion für den Kreisradius
    fun setCircleRadius(radius: Float) {
        circleRadius = radius
    }

    // setter Funktion für die Farbe des Kreises
    fun setCircleColor(color: Int) {
        paint.color = color
    }

    // Anmerkung: Für den Hintergrund benötigen wir keine spezielle setter Funktion. Das ist eine Eigenschaft,
    // die von der Oberklasse View bereits bereitgestellt wird

    // setter Funktion für die Kreisposition
    fun setCirclePos(x: Int, y: Int) {
        circlePos.x = x
        circlePos.y = y
    }


    // Funktion, um unsere Grafik zu zeichnen
    override fun onDraw(canvas: Canvas) {
        val height = canvas.height
        val width = canvas.width

        // Zeichne Kreis am Punkt Point
        canvas.drawCircle(circlePos.x.toFloat(), circlePos.y.toFloat(), circleRadius, paint)
    }
}