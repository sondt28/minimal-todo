package com.son.minimaltodo.ui.reminder

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.son.minimaltodo.R

class RemoveTaskDialogFragment : DialogFragment() {

    private lateinit var actionTask: ActionTask

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val parentFragment = parentFragment
        if (parentFragment is ActionTask) {
            actionTask = parentFragment
        } else {
            throw IllegalStateException("Parent fragment must implement ActionTask interface")
        }

        val dialogBuilder = AlertDialog.Builder(activity)
        dialogBuilder.setMessage(R.string.removeTask)

        dialogBuilder.setPositiveButton(R.string.remove) { _, _ ->
            actionTask.remove(true)
            dismiss()
        }

        dialogBuilder.setNegativeButton(R.string.cancel) { _, _ ->
            dismiss()
        }

        return dialogBuilder.create()
    }
}

interface ActionTask {
    fun remove(isRemove: Boolean)
}