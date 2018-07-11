package com.example.shinya.motion_recognition

import android.content.Context
import android.os.Bundle
import android.os.Debug
import android.support.v4.app.Fragment
import android.text.InputType
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText




class CollectionFragment : Fragment() {

    private lateinit var motionLabelEditText: EditText
    private lateinit var inputMethodManager: InputMethodManager
    private var isRecording = false

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_collection, container, false)
        setupView(v)
        setupEditText(v)
        return v
    }

    private fun setupView(v: View) {
        v.isFocusableInTouchMode = true
        v.setOnKeyListener { _, keyCode, event ->
            if ((keyCode == KeyEvent.KEYCODE_VOLUME_UP || keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)
                    && event.action == KeyEvent.ACTION_UP) {

                if(isRecording){
                    val label = motionLabelEditText?.text
                    (activity as MainActivity).stopRecordSensor()
                    val sensorData = (activity as MainActivity).getSensorData()
                    Log.d("sensor", sensorData)
                    isRecording = false
                }else {
                    (activity as MainActivity).startRecordSensor()
                    isRecording = true
                }
                true
            } else false
        }
    }

    private fun setupEditText(v: View) {
        inputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        motionLabelEditText = v.findViewById(R.id.motion_label) as EditText
        motionLabelEditText?.inputType = InputType.TYPE_CLASS_TEXT
        motionLabelEditText?.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                val inputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)
                true
            }
            false
        }
    }
}