package com.example.annexe1

import android.content.Context
import android.media.AudioManager
import android.os.Bundle
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class VolumActivity: AppCompatActivity() {
    lateinit var sonerieSeek: SeekBar
    lateinit var mediaSeek: SeekBar
    lateinit var notificationSeek: SeekBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_volum)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
        var currentRingtoneVolume = audioManager.getStreamVolume(AudioManager.STREAM_RING)
        var currentMediaVolume = audioManager.getStreamVolume(AudioManager.STREAM_SYSTEM)
        var currentNotificationVolume = audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION)

        sonerieSeek = findViewById(R.id.sonnerie)
        mediaSeek = findViewById(R.id.media)
        notificationSeek = findViewById(R.id.notification)


        val ec = Ecouteur()
        sonerieSeek.setOnSeekBarChangeListener(ec)
        mediaSeek.setOnSeekBarChangeListener(ec)
        notificationSeek.setOnSeekBarChangeListener(ec)

        sonerieSeek.progress = currentRingtoneVolume
        mediaSeek.progress = currentMediaVolume
        notificationSeek.progress = currentNotificationVolume

    }

    inner class Ecouteur : OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
            // here, you react to the value being set in seekBar
        }

        override fun onStartTrackingTouch(seekBar: SeekBar) {
            // you can probably leave this empty
        }

        override fun onStopTrackingTouch(seekBar: SeekBar) {
            // you can probably leave this empty
        }
    }
}