package com.example.main

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.main.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private val TAG = "FirstFragment"

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    private lateinit var gestureDetector: GestureDetector

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gestureDetector = GestureDetector(context, SimpleTouchGestureListener())
        binding.imageView.setOnTouchListener { view, motionEvent ->
            //evaluateTouch(view, motionEvent)
            gestureDetector.onTouchEvent(motionEvent)
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

        when (action){
            MotionEvent.ACTION_DOWN -> Log.i(TAG,"Action DOWN at ($x, $y)")
            MotionEvent.ACTION_UP -> Log.i(TAG,"Action UP at ($x, $y)")
            MotionEvent.ACTION_MOVE -> Log.i(TAG,"Action MOVE at ($x, $y)")
        }
        return true
    }

    //eigenen GestureListener implementieren
    inner class SimpleTouchGestureListener() : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent): Boolean {
            Log.i(TAG, "onDown ${e.toString()}")
            return true
        }

        override fun onFling(e1: MotionEvent, e2: MotionEvent,
                             velocityX: Float, velocityY: Float): Boolean {
            Log.i(TAG, "onFling ${e1.toString()} ${e2.toString()}")
            return true
        }

        override fun onLongPress(e: MotionEvent) {
            Log.i(TAG, "onLongPress ${e.toString()}")
        }

        override fun onShowPress(e: MotionEvent) {
            Log.i(TAG, "onShowPress: ${e.toString()}")
        }

        override fun onSingleTapUp(e: MotionEvent): Boolean {
            Log.i(TAG, "onSingleTapUp: ${e.toString()}")
            return true
        }

        override fun onDoubleTap(e: MotionEvent): Boolean {
            Log.i(TAG, "onDoubleTap: ${e.toString()}")
            return true
        }

        override fun onDoubleTapEvent(e: MotionEvent): Boolean {
            Log.i(TAG, "onDoubleTapEvent: ${e.toString()}")
            return true
        }

        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            Log.i(TAG, "onSingleTapConfirmed: ${e.toString()}")
            return true
        }
    }
}