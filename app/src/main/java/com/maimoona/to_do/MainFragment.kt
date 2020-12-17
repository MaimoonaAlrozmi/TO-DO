package com.maimoona.to_do

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isEmpty
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val TAG = "TaskListFragment"

class ToDoFragment : Fragment() {
    private var param1: Int? = null
    private var param2: String? = null

    var formatter: SimpleDateFormat = SimpleDateFormat("EEE, MMM d, yyyy")

    private lateinit var toDoRecyclerView: RecyclerView;
    private lateinit var fab: FloatingActionButton;
    private lateinit var no_task: TextView;
    private var adapter: TaskAdapter? = TaskAdapter(emptyList())

    private val taskListViewModel: TaskListViewModel by lazy {
        ViewModelProviders.of(this).get(TaskListViewModel::class.java)
    }

    private val taskDetailViewModel: TaskDetailViewModel by lazy {
        ViewModelProviders.of(this).get(TaskDetailViewModel::class.java)
    }

    interface Callbacks {
        fun onTaskSelected(crimeId: UUID)
    }

    private var callbacks: Callbacks? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getInt(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val v = inflater.inflate(R.layout.fragment_main, container, false)
        no_task=v.findViewById(R.id.no_task)
        toDoRecyclerView =
            v.findViewById(R.id.recyclerview_to_do) as RecyclerView
        toDoRecyclerView.layoutManager = LinearLayoutManager(context)
        toDoRecyclerView.adapter = adapter

        fab = v.findViewById(R.id.fab)
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
        adapter = TaskAdapter(tasks)
        toDoRecyclerView.adapter = adapter

      /*  if (tasks.isNotEmpty()) {
            adapter = TaskAdapter(tasks)
            no_task.visibility = View.GONE
            toDoRecyclerView.adapter = adapter
        } else {
            toDoRecyclerView.visibility = View.GONE
        }*/
    }

    override fun onStart() {
        super.onStart()
        if (param1 == 0) {
            taskListViewModel.toDoListLiveData.observe(viewLifecycleOwner, Observer { tasks ->
                tasks?.let {
                    updateUI(tasks)
                }
            })
        }
        if (param1 == 1) {
            taskListViewModel.in_ProgressListLiveData.observe(
                viewLifecycleOwner,
                Observer { tasks ->
                    tasks?.let {
                        updateUI(tasks)
                    }
                })
        }
        if (param1 == 2) {
            taskListViewModel.doneListLiveData.observe(viewLifecycleOwner, Observer { tasks ->
                tasks?.let {
                    updateUI(tasks)
                }
            })
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        taskListViewModel.taskListLiveData.observe(
//            viewLifecycleOwner,
//            Observer { tasks ->
//                tasks?.let {
//                    Log.i(TAG, "Got crimes ${tasks.size}")
//                    updateUI(tasks)
//                }
//            })

        if (param1 == 0) {
            fab.visibility = View.VISIBLE
        } else if (param1 == 1) {
            fab.visibility = View.GONE
        } else if (param1 == 2) {
            fab.visibility = View.GONE
        }
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }


    private inner class TaskHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {
        //val noTextView: TextView = itemView.findViewById(R.id.task_no)
        val titleTextView: TextView = itemView.findViewById(R.id.task_title)
        val detailsTextView: TextView = itemView.findViewById(R.id.task_details)
        val dateTextView: TextView = itemView.findViewById(R.id.task_date)
        var btn1: Button = itemView.findViewById(R.id.b1)
        var btn2: Button = itemView.findViewById(R.id.b2)
        var constraint_layout: ConstraintLayout = itemView.findViewById(R.id.card_view)

        private val imageIcon_delete: ImageView = itemView.findViewById(R.id.imageIcon_delete)

        private lateinit var task: Task

        init {
            itemView.setOnClickListener(this)
        }


        fun date(last3days: Int): Date {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_YEAR, last3days)
            return calendar.time
        }

        fun checkTaskTab() {

            if (param1 == 0) {
                if (task.date.time < Date().time) {
                    constraint_layout.setBackgroundResource(R.drawable.cv1)
                } else if (date(3) > task.date) {
                    constraint_layout.setBackgroundResource(R.drawable.cv_r)
                    btn1.setBackgroundResource(R.drawable.btnbgr)
                    btn2.setBackgroundResource(R.drawable.btnbgr)
                }
                btn2.setText(R.string.f_Done)
                btn1.setText(R.string.f_In_Progress)
            } else if (param1 == 1) {
                constraint_layout.setBackgroundResource(R.drawable.cv_b)
                btn1.setBackgroundResource(R.drawable.btnbg_b)
                btn2.setBackgroundResource(R.drawable.btnbg_b)
                btn2.setText(R.string.f_Done)
                btn1.setText(R.string.f_ToDo)
            } else if (param1 == 2) {
                constraint_layout.setBackgroundResource(R.drawable.cv_y)
                btn1.setBackgroundResource(R.drawable.btnbg_y)
                btn2.setBackgroundResource(R.drawable.btnbg_y)
                btn1.setText(R.string.f_In_Progress)
                btn2.setText(R.string.f_ToDo)
            }
        }

        fun bind(task: Task) {
            this.task = task;
            checkTaskTab();
            titleTextView.text = this.task.title;
            detailsTextView.text = this.task.details;
            dateTextView.text = formatter.format(this.task.date);

            imageIcon_delete.setOnClickListener {
                taskDetailViewModel.deleteTask(task)
            }

            btn1.setOnClickListener {
                if (param1 == 0)
                    this.task.tab = 1
                else if (param1 == 1)
                    this.task.tab = 0
                else if (param1 == 2)
                    this.task.tab = 1
                taskListViewModel.updateTask(this.task)
            }
            btn2.setOnClickListener {
                if (param1 == 0)
                    this.task.tab = 2
                else if (param1 == 1)
                    this.task.tab = 2
                else if (param1 == 2)
                    this.task.tab = 0
                taskListViewModel.updateTask(this.task)
            }


        }

        override fun onClick(v: View) {
            callbacks?.onTaskSelected(task.id)
        }
    }

    private inner class TaskAdapter(var tasks: List<Task>) : RecyclerView.Adapter<TaskHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
                : TaskHolder {
            val view = layoutInflater.inflate(R.layout.list_item_task, parent, false)
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
        fun newInstance(param1: Int, param2: String) =
            ToDoFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}