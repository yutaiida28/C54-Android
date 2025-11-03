package com.example.atelier2

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView


class MainActivity : AppCompatActivity() {
    lateinit var playerView: PlayerView
    var player : ExoPlayer? = null // ici on donne acces sur  a player de onCreate
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        playerView=findViewById(R.id.playerView)

        player = ExoPlayer.Builder(this).build()
    }

    override fun onStart() {
        super.onStart()
        // il serat interdit de utiliser player view
        playerView.player = player
        // plus que c'est un acces externe qui require wifi il faut aller rajouter une permission dansle manifest
        val mp3url = "https://storage.googleapis.com/uamp/The_Kyoto_Connection_-_Wake_Up/01_-_Intro_-_The_Way_Of_Waking_Up_feat_Alan_Watts.mp3"
        // creer un MediaItem
        val mediaItem = MediaItem.fromUri(mp3url)
        player?.addMediaItem(mediaItem)
        player?.prepare()
        player?.play()

    }
}