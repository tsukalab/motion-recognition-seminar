package com.example.shinya.motion_recognition

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.LocalBroadcastManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import java.io.File
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class RecognitionFragment : Fragment() {
    private lateinit var broadcast: LocalBroadcastManager
    private var isRecording = false
    private val dtw = DTW()
    private lateinit var trainFiles: Array<File>
    private val csvReader = CsvReader()
    private lateinit var resultText: TextView

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_recognition, container, false)

        broadcast = LocalBroadcastManager.getInstance(context!!)

        getTrainFiles()

        return inflater.inflate(R.layout.fragment_recognition, container, false)
    }

    override fun onResume() {
        super.onResume()
        broadcast.registerReceiver(mMessageReceiver, IntentFilter("volume"))
    }

    override fun onPause() {
        super.onPause()
        broadcast.unregisterReceiver(mMessageReceiver)
    }

    private fun getTrainFiles(){
        val path = activity?.applicationContext?.getExternalFilesDir(null).toString()
        trainFiles = File(path).listFiles()
    }

    private fun parseQueryData(dataStr: String): MotionData{
        var data = MotionData()
        var rowStrings = dataStr.split("\n")
        for (row in rowStrings) {
            val rowData = row.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            if (rowData.count() < 3) break

            data.x.add(rowData[0].toFloat())
            data.y.add(rowData[1].toFloat())
            data.z.add(rowData[2].toFloat())
        }

        return data
    }

    private fun recognition(){
        val queryData = parseQueryData((activity as MainActivity).getSensorData())
        val distances = HashMap<String, Double>()
        trainFiles.forEach { file ->
            var distance = 0.0
            val trainData = csvReader.parse(file)
            distance += dtw.compute(queryData.x.toFloatArray(), trainData.x.toFloatArray()).distance
            distance += dtw.compute(queryData.y.toFloatArray(), trainData.y.toFloatArray()).distance
            distance += dtw.compute(queryData.z.toFloatArray(), trainData.z.toFloatArray()).distance
            distances[file.toString()] = distance
        }
        //TODO("クラスタリングしよう")
        val result = clustering(distances)
        (activity as MainActivity).showResultDialog(result.key)
    }

    private fun clustering(distances: HashMap<String, Double>): Map.Entry<String, Double>{
        val distancesAscMap = distances.toList()
                .sortedBy { (key, value) -> value }
                .toMap()
        var distancesAscArrayList = ArrayList<Map.Entry<String, Double>>()

        for (distance in distancesAscMap) {
            distancesAscArrayList.add(distance)
        }
        return distancesAscArrayList[0]
    }

    private val mMessageReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if(isRecording){
                (activity as MainActivity).stopRecordSensor()
                recognition()
                isRecording = false
            }else {
                (activity as MainActivity).startRecordSensor()
                isRecording = true
            }
        }
    }
}