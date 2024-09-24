package com.example.testbalinasoftapp.domain

import android.graphics.Bitmap
import android.util.Base64
import com.example.testbalinasoftapp.data.models.ImageEntity
import com.example.testbalinasoftapp.data.models.ImageItem
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Unix {
    fun convertUnixToDate(unixTime: Long): String {
        val date = Date(unixTime * 1000)
        val format = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return format.format(date)
    }
    fun convertUnixToDateWithTime(unixTime: Long): String {
        val date = Date(unixTime * 1000)
        val format = SimpleDateFormat("dd-MM-yyyy hh:mm", Locale.getDefault())
        return format.format(date)
    }

    fun ImageEntity.toImageItem(): ImageItem {
        return ImageItem(
            id = this.id,
            url = this.url,
            date = this.date,
            lat = this.lat,
            lng = this.lng
        )
    }

    fun convertImageToBase64(imageFile: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        imageFile.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }
}