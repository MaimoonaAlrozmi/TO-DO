package Database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.maimoona.to_do.Task

@Database(entities = [ Task::class ], version=2,exportSchema = false)
@TypeConverters(TaskTypeConverters::class)

abstract class TaskDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao
}