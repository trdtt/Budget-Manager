package com.example.budgetmanager_kotlin

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatDialogFragment
import java.lang.ClassCastException

class ButtonDialog(b: Button) : AppCompatDialogFragment() {
    private lateinit var label: EditText
    private lateinit var sizeOfText: EditText
    private lateinit var listener: ButtonDialogListener
    private var btn: Button = b

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val view = layoutInflater.inflate(R.layout.activity_dialog, null)

        builder.setView(view)
            .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialogInterface, i -> })
            .setPositiveButton("Okay", DialogInterface.OnClickListener { dialogInterface, i ->
                val labelInput: String = label.text.toString()
                val textSizeInput = sizeOfText.text.toString().toFloatOrNull()
                listener.applyTexts(labelInput, textSizeInput, btn)
            })

        initButton(view)

        return builder.create()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            listener = context as ButtonDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString()
            + "must implement ButtonDialogListener")
        }

    }

    private fun initButton(view: View) {
        label = view.findViewById(R.id.new_lable)
        sizeOfText = view.findViewById(R.id.text_size)

        sizeOfText.setText(btn.textSize.toString())
        label.setText(btn.text)
    }

    interface ButtonDialogListener {
        fun applyTexts(label: String, sizeOfText: Float?, btn: Button)
    }
}