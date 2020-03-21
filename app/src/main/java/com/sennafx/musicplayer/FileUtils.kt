package com.sennafx.musicplayer

import android.os.Environment
import java.io.File

object
FileUtils {
    fun listDir(
        path: String,
        list: ArrayList<String>
    ) {
        val dir = File(path)
        if (!dir.exists() || dir.isFile) return

        var listFiles: Array<File> = dir.listFiles()

        for (f in listFiles) {
            if (f.isDirectory && !f.absolutePath.startsWith("."))
                listDir(f.absolutePath, list)
        }

        if (listFiles == null || listFiles.size <= 0) return

        if (list == null) return

        for (f in listFiles) {
            if (f.absolutePath.endsWith(".mp3"))
                list.add(f.absolutePath)
        }
    }

    fun getExternalStorageDir(): String {
        return Environment.getExternalStorageDirectory().absolutePath
    }
}