package com.maimoona.to_do

import android.app.ProgressDialog.show
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import java.util.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ToDoFragment : Fragment() {

    private lateinit var toDoRecyclerView: RecyclerView
    private var adapter: TaskAdapter? = TaskAdapter(emptyList())

    private val taskListViewModel: TaskListViewModel by lazy {
        ViewModelProviders.of(this).get(TaskListViewModel::class.java)
    }

    interface Callbacks {
        fun onTaskSelected(crimeId: UUID)
    }

    private var callbacks: Callbacks? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val v = inflater.inflate(R.layout.fragment_to_do, container, false)
        toDoRecyclerView =
            v.findViewById(R.id.recyclerview_to_do) as RecyclerView
        toDoRecyclerView.layoutManager = LinearLayoutManager(context)
        toDoRecyclerView.adapter = adapter

        // updateUI()
        val fab: FloatingActionButton = v.findViewById(R.id.fab)
        fab.setOnClickListener { view ->

            val task = Task()
            taskListViewModel.addTask(task)
            callbacks?.onTaskSelected(task.id)

        }
        /* DialogAddTask().apply {
             setTargetFragment(this@ToDoFragment, 0)
             show(this@ToDoFragment.requireFragmentManager(), "Input")
         }*/
        return v;
    }


    private fun updateUI(tasks: List<Task>) {
        // val crimes = taskListViewModel.tasks
        adapter = TaskAdapter(tasks)
        toDoRecyclerView.adapter = adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        taskListViewModel.taskListLiveData.observe(
            viewLifecycleOwner,
            Observer { tasks ->
                tasks?.let {
                    Log.i(TAG, "Got crimes ${tasks.size}")
                    updateUI(tasks)
                }
            })
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    private inner class TaskHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {
        val noTextView: TextView = itemView.findViewById(R.id.task_no)
        val titleTextView: TextView = itemView.findViewById(R.id.task_title)
        val detailsTextView: TextView = itemView.findViewById(R.id.task_details)
        val dateTextView: TextView = itemView.findViewById(R.id.task_date)

        private lateinit var task: Task

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(task: Task) {
            this.task = task
            titleTextView.text = this.task.title
            dateTextView.text = this.task.date.toString()
        }

        override fun onClick(v: View) {
            callbacks?.onTaskSelected(task.id)
        }
    }

    private inner class TaskAdapter(var tasks: List<Task>) : RecyclerView.Adapter<TaskHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
                : TaskHolder {
            val view = layoutInflater.inflate(R.layout.list_item_add_todo, parent, false)
            return TaskHolder(view)
        }

        override fun getItemCount() = tasks.size

        override fun onBindViewHolder(holder: TaskHolder, position: Int) {
            val task = tasks[position]
            holder.bind(task)
        /*    val crime = tasks[position]
            holder.apply {
                noTextView.text = crime.no.toString()
                titleTextView.text = crime.title
                detailsTextView.text = crime.details
                dateTextView.text = crime.date.toString()
            }*/
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ToDoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}