package com.example.shinya.motion_recognition

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    val dtw = DTW()

    private lateinit var distanceText: TextView

    val array1 = floatArrayOf(0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f)
    val array2 = floatArrayOf(0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        distanceText = findViewById(R.id.result)

        val dtwDistance = dtw.compute(array1, array2).distance.toString()
        distanceText.text = dtwDistance
    }
}
