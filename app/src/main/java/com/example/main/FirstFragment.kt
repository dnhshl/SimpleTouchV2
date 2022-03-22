package com.example.main

import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.*
import android.widget.ImageView
import androidx.core.view.doOnLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.main.databinding.FragmentFirstBinding
import com.example.main.model.MainViewModel
import java.util.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private val TAG = "FirstFragment"

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    private val vm: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialisierung des GameViews
        binding.gameView.apply {
            setOnTouchListener { view, motionEvent -> evaluateTouch(view, motionEvent) }
            setBackgroundColor(vm.BACKGROUND_COLOR)
            setCircleRadius(vm.CIRCLE_RADIUS)
            setCircleColor(vm.CIRCLE_COLOR)

            // zu diesem Zeitpunkt ist die Dimension des GameViews (Höhe und Breite)
            // noch nicht bekannt. Wir brauchen diese Info aber, um den Kreis initial
            // in der Mitte des Bildschirms darzustellen.
            // die Funktion doOnLayout trägt dafür sorge, dass die Positionsberechnung
            // erst dann ausgeführt wird, wenn Höhe und Breite bekannt sind
            doOnLayout { view ->
                vm.setCircle(Point(view.measuredWidth/2, view.measuredHeight/2))
            }
        }

        // Observer auf die Circle LiveData
        vm.circle.observe(viewLifecycleOwner) { circle ->
            // Circle Position im GameView aktualisieren
            binding.gameView.setCirclePos(circle)
            // GameView neu darstellen
            binding.gameView.invalidate()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Funktion, die bei Touch Events aufgerufen wird
    private fun evaluateTouch(view: View, motionEvent: MotionEvent): Boolean {
        val action = motionEvent.action
        val x = motionEvent.x
        val y = motionEvent.y

        // Abbruch, wenn kein ACTION_DOWN Event
        if (action != MotionEvent.ACTION_DOWN) return false

        // Abbruch, wenn Klick nicht innerhalb des Kreises
        if (!clickInCircle(x, y)) return false

        // ab hier Code, wenn Klick innerhalb des Kreises

        // do at game start
        if (vm.clickCounter == 0) {
            // starte Timer
            vm.startTime = SystemClock.elapsedRealtime()
            // blende TextView aus
            binding.textView.visibility = View.INVISIBLE
        }

        // bei jedem erfolgreichen Klick
        // clickCounter hochzählen
        vm.clickCounter++
        // neue zufällige Circleposition
        vm.setCircle(newCirclePos())

        // do at game end
        if (vm.clickCounter == vm.MAX_CLICKS) {
            // stoppe Timer, berechne abgelaufene Zeit
            vm.elapsedTime = SystemClock.elapsedRealtime() - vm.startTime
            // Display Game Results
            binding.textView.visibility = View.VISIBLE
            binding.textView.text = getString(R.string.game_over).format(vm.elapsedTime.toString())
            updateHighscoreList()
            // reset für nächstes Spiel
            vm.clickCounter = 0
            val x = binding.gameView.width/2
            val y = binding.gameView.height/2
            vm.setCircle(Point(x,y))
        }
        return true
    }

    // Hilfsfunktionen

    // true, wenn (x,y) innerhalb des circles
    private fun clickInCircle(x: Float, y: Float): Boolean {
        val c = vm.getCircle()
        val dx = x - c.x
        val dy = y - c.y
        var r = vm.CIRCLE_RADIUS
        if (dx*dx + dy*dy < r*r) return true
        return false
    }

    // zufällige neue Postion des Kreises
    private fun newCirclePos(): Point {
        val w = binding.gameView.width
        val h = binding.gameView.height
        val r = vm.CIRCLE_RADIUS.toInt()
        val rand = Random()
        // rand.nextInt(ZAHL) liefert Ihnen eine Zufallszahl zwischen 0 und ZAHL
        val dx = w - 2*r
        val dy = h - 2*r

        return Point((r + rand.nextInt(dx)), r + rand.nextInt(dy))
    }

    // Eintrag in der Highscoreliste
    private fun updateHighscoreList() {
        // ergänze in der Highscore Liste
        vm.highscoreList.add(vm.HighScoreListItem(vm.nickname, vm.elapsedTime))
        // sortiere die Liste nach der besten Zeit
        vm.highscoreList.sortBy {listentry -> listentry.time}
        // wenn die Liste länger ist, als vorgesehen, dann entferne den
        // schlechtesten Eintrag
        if (vm.highscoreList.size > vm.MAX_TOP_SCORES) vm.highscoreList.removeLast()
        // fürs debuggen Ausgabe der Liste
        vm.highscoreList.forEach {
            Log.i(TAG, "${it.nickname} ${it.time}")
        }
    }
}