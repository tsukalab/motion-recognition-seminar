package com.example.shinya.motion_recognition

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup



class CollectionFragment : Fragment() {
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_collection, container, false)
        v.isFocusableInTouchMode = true
        v.setOnKeyListener { _, keyCode, event ->
            if ((keyCode == KeyEvent.KEYCODE_VOLUME_UP  || keyCode == KeyEvent.KEYCODE_VOLUME_DOWN )
                    && event.action == KeyEvent.ACTION_UP) {
                // 何らかの処理
                true
            } else false
        }
        return v
    }
}