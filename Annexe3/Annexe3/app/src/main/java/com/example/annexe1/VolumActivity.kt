package com.example.annexe1


import android.content.Context
//import android.health.connect.datatypes.units.Volume
import android.os.Bundle
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import java.io.FileNotFoundException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

class VolumActivity: AppCompatActivity() {
    lateinit var sonerieSeek: SeekBar
    lateinit var mediaSeek: SeekBar
    lateinit var notificationSeek: SeekBar

    var v: Volum? = null // types est volum ou null

    private fun deSerialiserListe(): Volum? {

        try {
            val fis = openFileInput("Volume.ser")
            val ois = ObjectInputStream(fis)
            ois.use { v = ois.readObject() as Volum }
        } catch (f: FileNotFoundException) {
            Toast.makeText(this@VolumActivity, "le volume n'est pas initialiser", Toast.LENGTH_LONG)
                .show()
        }
        return v
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_volum)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        var volum = deSerialiserListe()

        sonerieSeek = findViewById(R.id.sonnerie)
        mediaSeek = findViewById(R.id.media)
        notificationSeek = findViewById(R.id.notification)

        sonerieSeek.progress = volum?.sonnerie ?: 7
        mediaSeek.progress = volum?.media ?: 7
        notificationSeek.progress = volum?.notification ?: 7

    }

    override fun onStop() {
        super.onStop()
        val fos = openFileOutput("Volume.ser", Context.MODE_PRIVATE)
        val oos = ObjectOutputStream(fos)
        oos.use {
            oos.writeObject(
                Volum(
                    sonerieSeek.progress,
                    mediaSeek.progress,
                    notificationSeek.progress
                )
            )
        }
    }
}




















    //class VolumActivity: AppCompatActivity() {
//    lateinit var sonerieSeek: SeekBar
//    lateinit var mediaSeek: SeekBar
//    lateinit var notificationSeek: SeekBar
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_volum)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
//        val audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
//        var currentRingtoneVolume = audioManager.getStreamVolume(AudioManager.STREAM_RING)
//        var currentMediaVolume = audioManager.getStreamVolume(AudioManager.STREAM_SYSTEM)
//        var currentNotificationVolume = audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION)
//
//        sonerieSeek = findViewById(R.id.sonnerie)
//        mediaSeek = findViewById(R.id.media)
//        notificationSeek = findViewById(R.id.notification)
//
//
//        val ec = Ecouteur()
//        sonerieSeek.setOnSeekBarChangeListener(ec)
//        mediaSeek.setOnSeekBarChangeListener(ec)
//        notificationSeek.setOnSeekBarChangeListener(ec)
//
//        sonerieSeek.progress = currentRingtoneVolume
//        mediaSeek.progress = currentMediaVolume
//        notificationSeek.progress = currentNotificationVolume
//
//
//
//    }
//
//
//        inner class Ecouteur : OnSeekBarChangeListener {
//        override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
//            var liste:ArrayList<Volume> = ArrayList()
//            // here, you react to the value being set in seekBar
//
//        }
//
//        override fun onStartTrackingTouch(seekBar: SeekBar) {
//            // you can probably leave this empty
//        }
//
//        override fun onStopTrackingTouch(seekBar: SeekBar) {
//            // you can probably leave this empty
//        }
//    }

