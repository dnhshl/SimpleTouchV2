package com.example.main.model

import android.graphics.Color
import android.graphics.Point
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    val TAG = "MainViewModel"

    // Konstante Daten
    val CIRCLE_RADIUS = 100F
    val CIRCLE_COLOR = Color.WHITE
    val BACKGROUND_COLOR = Color.BLUE
    val MAX_CLICKS = 10

    // LiveData
    private var _circlePos = MutableLiveData<Point>()
    val circlePos: LiveData<Point>
        get() = _circlePos


    init {
        _circlePos.value = Point(0,0)
    }

    // setter Funktionen für die LiveData
    fun setCirclePos(x:Int, y:Int) {
        Log.i(TAG, "setCirclePos $x $y")
        _circlePos.value = Point(x, y)
    }

    // getter Funktionen für die LiveData
    fun getCirclePos(): Point {
        return _circlePos.value!!
    }

    // sonstige variable Daten, nicht als LiveData

    private var _clickCounter = 0
    val clickCounter: Int
       get() = _clickCounter


    fun incClickCounter() {
        _clickCounter++
    }

    fun resetClickCounter() {
        _clickCounter = 0
    }

}