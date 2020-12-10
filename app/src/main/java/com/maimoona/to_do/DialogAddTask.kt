package com.maimoona.to_do

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import java.util.*

class DialogAddTask : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val view=activity?.layoutInflater?.inflate(R.layout.add_task_dialog,null)

        val titleEditText = view?.findViewById(R.id.edit_title) as EditText
        val detailsEditText = view?.findViewById(R.id.edit_details) as EditText
        val dateButton = view?.findViewById(R.id.btn_date) as Button

        dateButton.setOnClickListener {
            Toast.makeText(context,"ddddd",Toast.LENGTH_LONG).show()
        }
        return AlertDialog.Builder(requireContext(),R.style.ThemeOverlay_AppCompat_Dialog_Alert)
            .setView(view)
            .setPositiveButton("Add"){ dialog,_ ->
                val std=Task(
                    UUID.randomUUID(),titleEditText.text.toString().toInt(),
                    detailsEditText.text.toString())
                    //passCheckBox.isChecked)

                /*targetFragment?.let { fragment ->
                    (fragment as Callbacks).onStudentAdded(std)
                }*/
            }
            .setNegativeButton("Cancel"){dialog,_ ->
                dialog.cancel()
            }.create()

    }

/*    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        //content = arguments!!.getString("content")

        val style = DialogFragment.STYLE_NO_FRAME
        val theme = R.style.ThemeOverlay_AppCompat_Dialog_Alert
        setStyle(style, theme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView:View=inflater.inflate(R.layout.add_task_dialog,container,false)

        return rootView;
    }*/
}