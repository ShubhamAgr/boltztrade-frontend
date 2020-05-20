package com.boltztrade.app.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.boltztrade.app.R
import com.boltztrade.app.callbacks.DeployIntervalListDialogCallback

class DeployIntervalDialog(val message:String,val deployIntervalListDialogCallback: DeployIntervalListDialogCallback): DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(message)
                .setItems(
                    R.array.deploy_interval
                ) { dialog, which ->
                    deployIntervalListDialogCallback.interval(resources.getStringArray(R.array.deploy_interval)[which])
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}