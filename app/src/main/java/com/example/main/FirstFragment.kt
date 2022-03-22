package com.example.main

import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import androidx.core.view.doOnLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.main.databinding.FragmentFirstBinding
import com.example.main.model.MainViewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private val TAG = "FirstFragment"

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.gameView.setOnTouchListener { view, motionEvent ->
            evaluateTouch(view, motionEvent)
        }
    }



    private fun evaluateTouch(view: View, motionEvent: MotionEvent): Boolean {
        val action = motionEvent.action
        val x = motionEvent.x
        val y = motionEvent.y

        if (action == MotionEvent.ACTION_DOWN) {
            Log.i(TAG,"Action DOWN at ($x, $y)")
            binding.gameView.setCirclePos(Point(x.toInt(),y.toInt()))
            binding.gameView.invalidate()
        }

        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}