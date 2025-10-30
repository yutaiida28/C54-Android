package com.example.musicplayer

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.bumptech.glide.Glide

// merci yuta :)
class MainActivity : AppCompatActivity(), MusicUpdateObserver {
    val url = "https://api.jsonbin.io/v3/b/680a6a1d8561e97a5006b822?meta=false"
    var musicUpdate: Sujet? = null
    lateinit var playingListe: ListView
    lateinit var playingNow: FrameLayout
    var player : ExoPlayer? = null;
    var mp3 : ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        playingListe = findViewById(R.id.playingListe)
        playingNow = findViewById(R.id.playingMusic)
        player = ExoPlayer.Builder(this).build()
    }

    override fun onStart() {
        super.onStart()
        musicUpdate = GetMusic(this ,url)
        (musicUpdate as GetMusic).addObserver(this)
    }

    override fun succes(lm: ListeMusics) {
        Toast.makeText(this, "response is ${lm.listeMusic.size}", LENGTH_LONG).show()

        for (music in lm.listeMusic){
            val mediaItem = MediaItem.fromUri(music.source)
            player?.addMediaItem(mediaItem)
        }
        player?.prepare()
        Toast.makeText(this, "response is ${player}", LENGTH_LONG).show()
        afficher(lm)
    }

    fun afficher(lm: ListeMusics){
        val remplir = ArrayList<HashMap<String, String>>()

        for (music in lm.listeMusic) {
            val temp = HashMap<String, String>()
            temp["title"] = music.title
            temp["sec"] = music.duration.toString()
            temp["image"] = music.image.toString()
            remplir.add(temp)
        }

        val adap = simpleAdapter2(
            this,
            remplir,
            R.layout.layout,
            arrayOf("title", "sec","image"),
            intArrayOf(R.id.name, R.id.length, R.id.imageView2)
        )
//        fait par un ia elle permet d'avoir le nom de la music defiler a linfinie
        adap.viewBinder = SimpleAdapter.ViewBinder { view, data, textRepresentation ->
            if (view.id == R.id.name && view is TextView) {
                view.text = textRepresentation
                view.isSelected = true  // This enables the marquee to run continuously
                true  // We handled the binding ourselves
            } else {
                false // Default binding for other views
            }
        }


        playingListe.adapter = adap
        playingListe.setOnItemClickListener { _, _, position, _ ->
            val selectedMusic = lm.listeMusic[position]
//            retire tous music enterieur qui jouais
            playingNow.removeAllViews()
//            remplilie le layout avec les infos de la music selectionner
            val musicView = layoutInflater.inflate(R.layout.playing_music_main, playingNow, false)

            val titleView = musicView.findViewById<TextView>(R.id.musicTitle)
            val imageView = musicView.findViewById<ImageView>(R.id.musicIcon)

            titleView.text = selectedMusic.title

            Glide.with(this)
                .load(selectedMusic.image)
                .into(imageView)
            player?.seekTo(position, 0)
            player?.play()
            playingNow.addView(musicView)
            playingNow.visibility = View.VISIBLE
        }
    }

    inner class simpleAdapter2(
        context: Context,
        remplir: ArrayList<HashMap<String, String>>,
        layout: Int,
        data: Array<String>,
        to: IntArray
    ) : SimpleAdapter(context, remplir, layout, data, to){
        override fun setViewImage(v: ImageView, value: String) {
            Glide.with(v.context)
                .load(value)
                .into(v)
        }
    }

    override fun error() {
        TODO("Not yet implemented")
    }


}