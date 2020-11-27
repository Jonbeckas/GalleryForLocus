package net.tetraowl.galleryinlocus

import android.content.Context
import com.google.gson.Gson
import java.io.File

class Storage(val context:Context) {
    val gson = Gson()
    var paths:List<String>
        get() {
            val file = File("${context.filesDir}/paths.json")
            if (!file.exists()) {
                return listOf()
            }
            val json = file.readText()
            val value = gson.fromJson<List<String>>(json,List::class.java);
            return value;
        } set(value) {
            val json = gson.toJson(value)
            val file = File("${context.filesDir}/paths.json")
            file.writeText(json)
        }

    var ids:List<String>
        get() {
            val file = File("${context.filesDir}/ids.json")
            if (!file.exists()) {
                return listOf()
            }
            val json = file.readText()
            val value = gson.fromJson<List<String>>(json,List::class.java);
            return value;
        } set(value) {
            val json = gson.toJson(value)
            val file = File("${context.filesDir}/ids.json")
            file.writeText(json)
        }

    fun addId(long:String) {
        val value = ids.toMutableList()
        if (!value.contains(long)) {
            value.add(long)
            ids = value
        }
    }

    fun addPath(path:String) {
        val paths = this.paths.toMutableList()
        if (!this.paths.contains(path)) {
            paths.add(path)
            this.paths = paths
        }
    }

}
