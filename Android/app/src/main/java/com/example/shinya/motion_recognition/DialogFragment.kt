package com.example.shinya.motion_recognition

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment


class ResultDialogFragment : DialogFragment() {

    val title = "Result"
    var msg = "msg"
    val okText = "OK"
    /** ok押下時の挙動 */
    val onOkClickListener = DialogInterface.OnClickListener { _, _ -> }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the Builder class for convenient dialog construction
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(title)
                .setMessage(msg)
                .setPositiveButton(okText, onOkClickListener)
        // Create the AlertDialog object and return it
        return builder.create()
    }

    override fun onPause() {
        super.onPause()
        // onPause でダイアログを閉じる場合
        dismiss()
    }
}