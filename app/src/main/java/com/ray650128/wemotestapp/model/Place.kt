package com.ray650128.wemotestapp.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*

open class Place : RealmObject() {
    @PrimaryKey()
    var id: Int = 0
    var title: String = ""
    var content: String = ""
    var timestamp: Long = -1L
    var photos: RealmList<String>? = null
    var latitude: Double = 0.0
    var longitude: Double = 0.0

    val updateTime: String
        get() = timestamp2Str(timestamp)

    private fun timestamp2Str(timestamp: Long): String {
        val simpleDateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.TAIWAN)
        return simpleDateFormat.format(timestamp)
    }
}
