package com.maimoona.to_do

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*
@Entity
data class Task(@PrimaryKey val id: UUID = UUID.randomUUID(),
                var no:Int=0,
                var title: String="",
                var details: String="",
                var date: Date= Date(),
                var tab: Int = 0
) {}