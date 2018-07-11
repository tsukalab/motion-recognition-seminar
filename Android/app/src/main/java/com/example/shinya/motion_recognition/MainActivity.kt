package com.example.shinya.motion_recognition

import android.os.Bundle

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Handler
import android.support.v4.app.FragmentActivity
import android.view.View
import android.widget.TabHost
import android.widget.TabHost.TabContentFactory

class MainActivity : FragmentActivity(), TabHost.OnTabChangeListener, SensorEventListener {

    // TabHost
    private lateinit var mTabHost: TabHost
    private var mLastTabId = ""
    private lateinit var sensorManager: SensorManager
    private var sensorDataString = ""
    private var acceleration = Acceleration()
    private val handler = Handler()
    private lateinit var runnable:Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tabhost)
        addTabFragment()
        setupSensorManager()
    }

    fun startRecordSensor(){
        handler.post(runnable)
    }

    fun stopRecordSensor(){
        handler.removeCallbacks(runnable)
    }

    fun getSensorData(): String{
        return sensorDataString
    }

    private fun setupSensorManager() {
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val accel = sensorManager?.getDefaultSensor(
                Sensor.TYPE_ACCELEROMETER)

        sensorManager?.registerListener(this, accel, SensorManager.SENSOR_DELAY_FASTEST)

        runnable = object : Runnable {
            override fun run() {
                sensorDataString += acceleration.x.toString() + "," +
                        acceleration.y.toString() + "," +
                        acceleration.z.toString() + "\n"
                handler.postDelayed(this, 100)
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}

    override fun onSensorChanged(event: SensorEvent) {
        when (event.sensor.type) {
            Sensor.TYPE_ACCELEROMETER -> {
                acceleration.x = event.values[0]
                acceleration.y = event.values[1]
                acceleration.z = event.values[2]
            }
        }
    }

    override fun onTabChanged(tabId: String) {
        if (mLastTabId !== tabId) {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            if ("collection" === tabId) {
                fragmentTransaction
                        .replace(R.id.realtabcontent, CollectionFragment())
            } else if ("recognition" === tabId) {
                fragmentTransaction
                        .replace(R.id.realtabcontent, RecognitionFragment())
            }
            mLastTabId = tabId
            fragmentTransaction.commit()
        }
    }

    private fun addTabFragment(){
        mTabHost = findViewById<View>(android.R.id.tabhost) as TabHost
        mTabHost!!.setup()

        /* Tab1 設定 */
        val collectionTab = mTabHost!!.newTabSpec("collection")
        collectionTab.setIndicator("収集")
        collectionTab.setContent(DummyTabFactory(this))
        mTabHost!!.addTab(collectionTab)

        // Tab2 設定
        val recognitionTab = mTabHost!!.newTabSpec("recognition")
        recognitionTab.setIndicator("認識")
        recognitionTab.setContent(DummyTabFactory(this))
        mTabHost!!.addTab(recognitionTab)

        // タブ変更時イベントハンドラ
        mTabHost!!.setOnTabChangedListener(this)

        // 初期タブ選択
        onTabChanged("collection")
    }

    /*
     * android:id/tabcontent のダミーコンテンツ
     */
    private class DummyTabFactory internal constructor(/* Context */
            private val mContext: Context) : TabContentFactory {

        override fun createTabContent(tag: String): View {
            val v = View(mContext)
            return View(mContext)
        }
    }
}