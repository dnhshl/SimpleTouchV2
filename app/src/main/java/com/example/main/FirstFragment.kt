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

    private val viewModel: MainViewModel by viewModels()

    private var startTime: Long = 0
    private var elapsedTime: Long = 0



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

         binding.gameView.apply {
            setOnTouchListener { view, motionEvent -> evaluateTouch(view, motionEvent) }
            setBackgroundColor(viewModel.BACKGROUND_COLOR)
            setCircleRadius(viewModel.CIRCLE_RADIUS)
            setCircleColor(viewModel.CIRCLE_COLOR)

            doOnLayout { view ->
                viewModel.setCirclePos(view.measuredWidth/2, view.measuredHeight/2)
            }
        }

        viewModel.circlePos.observe(viewLifecycleOwner) { circlePos ->
            binding.gameView.setCirclePos(circlePos.x, circlePos.y)
            binding.gameView.invalidate()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun evaluateTouch(view: View, motionEvent: MotionEvent): Boolean {
        val action = motionEvent.action
        val x = motionEvent.x
        val y = motionEvent.y


        if (action != MotionEvent.ACTION_DOWN) return false

        // Wenn Klick außerhalb vom Kreis, mache nichts
        if (!clickInCircle(x, y)) return false

        // ab hier Code, wenn Klick innerhalb des Kreises

        // do at game start
        if (viewModel.clickCounter == 0) {
            // starte Timer
            startTime = SystemClock.elapsedRealtime()
            binding.textView.visibility = View.INVISIBLE
        }
        viewModel.incClickCounter()
        val newCirclePos = newCirclePos()
        viewModel.setCirclePos(newCirclePos.x, newCirclePos.y)
        // do at game end
        if (viewModel.clickCounter == viewModel.MAX_CLICKS) {
            // stoppe Timer
            elapsedTime = SystemClock.elapsedRealtime() - startTime
            viewModel.resetClickCounter()
            val x = binding.gameView.width/2
            val y = binding.gameView.height/2
            viewModel.setCirclePos(x,y)
            // Display Game Results
            binding.textView.visibility = View.VISIBLE
            binding.textView.text = getString(R.string.game_over).format(elapsedTime.toString())
        }
        return true
    }

    private fun clickInCircle(x: Float, y: Float): Boolean {
        val c = viewModel.getCirclePos()
        val dx = x - c.x
        val dy = y - c.y
        var r = viewModel.CIRCLE_RADIUS
        Log.i(TAG, "${c.toString()} ${dx*dx + dy*dy}, ${r*r}" )
        if (dx*dx + dy*dy < r*r) return true
        return false
    }

    private fun newCirclePos(): Point {
        val w = binding.gameView.width
        val h = binding.gameView.height
        val r = viewModel.CIRCLE_RADIUS.toInt()
        val dx = w - 2*r
        val dy = h - 2*r
        val rand = Random()
        return Point((r + rand.nextInt(dx)), r + rand.nextInt(dy))
    }
}