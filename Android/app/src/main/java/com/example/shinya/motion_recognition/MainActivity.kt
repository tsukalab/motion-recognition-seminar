package com.example.shinya.motion_recognition

import android.os.Bundle

import android.content.Context
import android.support.v4.app.FragmentActivity
import android.view.View
import android.widget.TabHost
import android.widget.TabHost.TabContentFactory

class MainActivity : FragmentActivity(), TabHost.OnTabChangeListener {

    // TabHost
    private var mTabHost: TabHost? = null
    private var mLastTabId: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tabhost)
        addTabFragment()
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