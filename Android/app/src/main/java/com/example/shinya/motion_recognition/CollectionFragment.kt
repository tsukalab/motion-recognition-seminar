package com.example.shinya.motion_recognition

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.InputType
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.content.Intent
import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.support.v4.content.LocalBroadcastManager
import android.widget.Toast
import java.io.*


class CollectionFragment : Fragment() {

    private lateinit var motionLabelEditText: EditText
    private lateinit var inputMethodManager: InputMethodManager
    private var isRecording = false
    private lateinit var broadcast: LocalBroadcastManager

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_collection, container, false)
        setupView(v)
        setupEditText(v)
        broadcast = LocalBroadcastManager.getInstance(context!!)
        return v
    }

    override fun onResume() {
        super.onResume()
        broadcast.registerReceiver(mMessageReceiver, IntentFilter("volume"))
    }

    override fun onPause() {
        super.onPause()
        broadcast.unregisterReceiver(mMessageReceiver)
    }

    private fun saveSensorData(data:String, label: String){
        try {
            val dir = activity?.applicationContext?.getExternalFilesDir(null)
            val filename = (dir?.absolutePath + "/") + label + getCurrentTime() + ".csv"
            val f = File(filename)
            val fw = FileWriter(f, true)
            fw.write(data)
            fw.flush()
            fw.close()
            Toast.makeText(context , "保存しました", Toast.LENGTH_LONG).show()
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
    }

    private fun setupView(v: View) {
        v.isFocusableInTouchMode = true
        v.setOnKeyListener { _, keyCode, event ->
            if ((keyCode == KeyEvent.KEYCODE_VOLUME_UP || keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)
                    && event.action == KeyEvent.ACTION_UP) {

                if(isRecording){
                    (activity as MainActivity).stopRecordSensor()
                    val sensorData = (activity as MainActivity).getSensorData()
                    saveSensorData(sensorData, motionLabelEditText.text.toString())
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
        motionLabelEditText.inputType = InputType.TYPE_CLASS_TEXT
        motionLabelEditText.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                val inputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)
                true
            }
            false
        }
    }

    private fun getCurrentTime(): String{
        val tsLong = System.currentTimeMillis() / 1000
        return tsLong.toString()
    }

    private val mMessageReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if(isRecording){
                (activity as MainActivity).stopRecordSensor()
                val sensorData = (activity as MainActivity).getSensorData()
                saveSensorData(sensorData, motionLabelEditText.text.toString())
                isRecording = false
            }else {
                (activity as MainActivity).startRecordSensor()
                isRecording = true
            }
        }
    }
}