package com.example.testbalinasoftapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.testbalinasoftapp.data.models.ImageEntity

@Dao
interface ImageDao {
    @Query("SELECT * FROM images")
    suspend fun getAllImages(): List<ImageEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(images: List<ImageEntity>)

    @Query("DELETE FROM images WHERE id = :imageId")
    suspend fun deleteImageById(imageId: Int)

    @Query("DELETE FROM images")
    suspend fun clearTable()
}
