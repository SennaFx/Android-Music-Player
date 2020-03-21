package com.sennafx.musicplayer

import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val REQUEST_PERMISSION_CODE = 101
    var list: ArrayList<String> = ArrayList()
    var mp: MediaPlayer = MediaPlayer()
    var adapter: FilesAdapter = FilesAdapter(list)
    var folder = ""

    var r = Runnable {
        while (mp.isPlaying) {
            try {
                Thread.sleep(1000)
                song_progress.progress = mp.currentPosition / 60
                tv_progress.text = convertTime(mp.currentPosition)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            if (!checkPermissions())
                requestPermissions()
            else {
                recyclerview.layoutManager = LinearLayoutManager(this)
                adapter = FilesAdapter(list)

                folder = FileUtils.getExternalStorageDir()
                refreshList()

                song_progress.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(
                        seekBar: SeekBar?,
                        progress: Int,
                        fromUser: Boolean
                    ) {
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    }

                    override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    }
                })

                adapter.setClickListener(object : FilesAdapter.itemClick {
                    override fun onSongClickListener(position: Int) {
                        if (!mp.isPlaying) {
                            startPlaying(position)
                        } else {
                            mp.reset()
                            startPlaying(position)
                        }
                    }
                })
            }
    }

    fun startPlaying(position: Int) {
        mp.setDataSource(list[position])
        mp.prepare()
        mp.start()
        song_progress.max = mp.duration / 60
        tv_duration.text = convertTime(mp.duration)
        Thread(r).start()
    }

    private fun convertTime(t: Int): String {
        val min = t / 1000 / 60
        val sec = t / 1000 % 60
        return String.format("%d:%02d", min, sec)
    }

    fun refreshList() {
        list.clear()
        FileUtils.listDir(folder, list)
        recyclerview.adapter = adapter
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            REQUEST_PERMISSION_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_PERMISSION_CODE ->
                if (grantResults.size > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun checkPermissions(): Boolean {
        var result = ContextCompat.checkSelfPermission(
            applicationContext,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        )
        var result2 = ContextCompat.checkSelfPermission(
            applicationContext,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        return result == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED
    }
}
