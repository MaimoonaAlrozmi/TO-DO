package com.maimoona.to_do

import android.content.ContentValues.TAG
import android.os.Bundle
import android.telecom.Call
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.add_task_dialog.*
import java.util.*

private const val REQUEST_DATE = 0
private const val ARG_TASK_ID = "task_id"
private const val DIALOG_DATE = "DialogDate"

class TaskFragment : Fragment(), DatePickerFragment.Callbacks {

    private lateinit var task: Task
    private lateinit var noField: EditText
    private lateinit var titleField: EditText
    private lateinit var detailsField: EditText
    private lateinit var dateButton: Button

    private val taskDetailViewModel: TaskDetailViewModel by lazy {
        ViewModelProviders.of(this).get(TaskDetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        task = Task()
        val taskId: UUID = arguments?.getSerializable(ARG_TASK_ID) as UUID
        taskDetailViewModel.loadTask(taskId)
        Log.d(TAG, "args bundle crime ID: $taskId")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.add_task_dialog, container, false)

        titleField = view.findViewById(R.id.edit_title)
        detailsField = view.findViewById(R.id.edit_details)
        dateButton = view.findViewById(R.id.btn_date) as Button

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        taskDetailViewModel.taskLiveData.observe(
            viewLifecycleOwner,
            Observer { task ->
                task?.let {
                    this.task = task
                    updateUI()
                }
            })
    }

    override fun onStop() {
        super.onStop()
        taskDetailViewModel.saveTask(task)
    }

    override fun onStart() {
        super.onStart()
        taskDetailViewModel.saveTask(task)

        val detailsWatcher = object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int) {
                task.details = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        detailsField.addTextChangedListener(detailsWatcher)

        val titleWatcher = object : TextWatcher {
            override fun beforeTextChanged(
                sequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {}

            override fun onTextChanged(
                sequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                task.title = sequence.toString()
            }

            override fun afterTextChanged(sequence: Editable?) {
            }
        }
        titleField.addTextChangedListener(titleWatcher)

        dateButton.setOnClickListener {
            DatePickerFragment.newInstance(task.date).apply {
                setTargetFragment(this@TaskFragment, REQUEST_DATE)
                show(this@TaskFragment.requireFragmentManager(), DIALOG_DATE)
            }
        }
    }

    override fun onDateSelected(expireDate: Date) {
        task.date = expireDate
        updateUI()
    }

    private fun updateUI() {
        titleField.setText(task.title)
        detailsField.setText(task.details)
        dateButton
    }

    companion object {
        fun newInstance(taskId: UUID): TaskFragment {
            val args = Bundle().apply {
                putSerializable(ARG_TASK_ID, taskId)
            }
            return TaskFragment().apply {
                arguments = args
            }
        }
    }
}