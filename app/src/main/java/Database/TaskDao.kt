package Database


import androidx.lifecycle.LiveData
import androidx.room.*
import com.maimoona.to_do.Task
import java.util.*

@Dao
interface TaskDao {

    @Query("SELECT * FROM Task")
    fun getTasks(): LiveData<List<Task>>

    @Query("SELECT * FROM Task WHERE id=(:id)")
    fun getTask(id: UUID): LiveData<Task?>

    @Query("SELECT * FROM task WHERE move=(:move) order by date")
    fun getTasksByMove(move: Int):  LiveData<List<Task>>

    @Update
    fun updateTask(task: Task)

    @Insert
    fun addTask(task: Task)

    @Delete
    fun removeTask(task: Task)

}