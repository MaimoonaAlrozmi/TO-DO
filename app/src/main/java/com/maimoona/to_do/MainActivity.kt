package com.maimoona.to_do

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.maimoona.to_do.ToDoFragment.Companion.newInstance
import java.util.*

lateinit var tabLayout: TabLayout;
lateinit var viewPager: ViewPager2;

class MainActivity : AppCompatActivity() , ToDoFragment.Callbacks{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tab);
        viewPager = findViewById(R.id.view_pager);

        viewPager.adapter = object : FragmentStateAdapter(this) {

            override fun getItemCount(): Int {
                return 3
            }

            override fun createFragment(position: Int): Fragment {

                return when (position) {
                    0 -> ToDoFragment.newInstance("", "")
                    1 -> In_ProgressFragment.newInstance("", "")
                    2 -> DoneFragment.newInstance("", "")
                    else -> {
                        DoneFragment.newInstance("", "")
                    }
                }
            }
        }

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.f_ToDo);
                1 -> getString(R.string.f_In_Progress);
                2 -> getString(R.string.f_Done);
                else -> null
            }
        }.attach()
        /*tabLayout.getTabAt(0)!!.setIcon(R.drawable.ic_baseline_chat_24)
        tabLayout.getTabAt(1)!!.setIcon(R.drawable.ic_baseline_chat_24)
        tabLayout.getTabAt(2)!!.setIcon(R.drawable.ic_baseline_chat_24)
*/
    }

    override fun onTaskSelected(taskId: UUID) {
        val fragment = TaskFragment.newInstance(taskId)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

}