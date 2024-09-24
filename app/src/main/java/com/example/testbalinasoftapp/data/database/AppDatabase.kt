package com.example.testbalinasoftapp.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.testbalinasoftapp.data.dao.UserDao
import com.example.testbalinasoftapp.data.models.UserEntity
import android.content.Context
import com.example.testbalinasoftapp.data.dao.ImageDao
import com.example.testbalinasoftapp.data.models.ImageEntity

@Database(entities = [UserEntity::class, ImageEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun imageDao(): ImageDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
