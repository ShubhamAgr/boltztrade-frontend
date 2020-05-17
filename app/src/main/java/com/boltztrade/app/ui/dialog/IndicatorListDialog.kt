package com.boltztrade.app.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.boltztrade.app.R
import com.boltztrade.app.callbacks.IndicatorListDialogCallback

class IndicatorListDialog(val message:String,val indicatorListDialogCallback: IndicatorListDialogCallback): DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(message)
                .setItems(R.array.indiactor_list
                ) { dialog, which ->
                    indicatorListDialogCallback.indicatorName(resources.getStringArray(R.array.indiactor_list)[which])
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}