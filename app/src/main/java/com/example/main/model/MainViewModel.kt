package com.example.main.model

import android.graphics.Color
import android.graphics.Point
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
    val MAX_TOP_SCORES = 5

    // LiveData
    private var _circle = MutableLiveData<Point>(Point(0,0))
    val circle: LiveData<Point>
      get() = _circle

    fun setCircle(c:Point) {
        _circle.value = c
    }
    fun getCircle(): Point {
        return _circle.value!!
    }


    // Variable Daten
    var clickCounter = 0
    var startTime = 0L
    var elapsedTime = 0L

    var nickname = "anonymous"
    var highscoreList = ArrayList<HighScoreListItem>()

    inner class HighScoreListItem(val nickname: String, val time: Long) {}
}

