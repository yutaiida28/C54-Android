package com.example.musicplayertp1

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.bumptech.glide.Glide

class MainActivity : AppCompatActivity(), MusicUpdateObserver {

    private val url = "https://api.jsonbin.io/v3/b/680a6a1d8561e97a5006b822?meta=false"
    private var musicUpdate: GetMusic? = null

    // Composants UI
    private lateinit var playerView: PlayerView
    private lateinit var playButton: ImageButton
    private lateinit var pauseButton: ImageButton
    private lateinit var nextButton: ImageButton
    private lateinit var previousButton: ImageButton
    private lateinit var forward10Button: ImageButton
    private lateinit var backward10Button: ImageButton
    private lateinit var seekBar: SeekBar
    private lateinit var currentTimeText: TextView
    private lateinit var totalTimeText: TextView
    private lateinit var songTitleText: TextView
    private lateinit var artistText: TextView
    private lateinit var albumImageView: ImageView
    private lateinit var showPlaylistButton: Button
    private lateinit var webLinkButton: Button

    private var player: ExoPlayer? = null
    private var musicList: ListeMusics? = null
    private var countDownTimer: CountDownTimer? = null
    private lateinit var prefs: SharedPreferences

    // Launcher pour le pattern boomerang
    var playlistLauncher: ActivityResultLauncher<Intent>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        prefs = getSharedPreferences("MusicPlayerPrefs", MODE_PRIVATE)

        initializeViews()

        player = ExoPlayer.Builder(this).build()
        playerView.player = player
        playerView.useController = false

        setupButtonListeners()
        setupSeekBar()
        setupPlayerListener()

        // Enregistrement du launcher avec le callback
        playlistLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            CallBackPlaylist()  // ✅ Inner class callback
        )
    }

    private fun initializeViews() {
        playerView = findViewById(R.id.playerView)
        playButton = findViewById(R.id.playButton)
        pauseButton = findViewById(R.id.pauseButton)
        nextButton = findViewById(R.id.nextButton)
        previousButton = findViewById(R.id.previousButton)
        forward10Button = findViewById(R.id.forward10Button)
        backward10Button = findViewById(R.id.backward10Button)
        seekBar = findViewById(R.id.seekBar)
        currentTimeText = findViewById(R.id.currentTime)
        totalTimeText = findViewById(R.id.totalTime)
        songTitleText = findViewById(R.id.songTitle)
        artistText = findViewById(R.id.artist)
        albumImageView = findViewById(R.id.albumImage)
        showPlaylistButton = findViewById(R.id.showPlaylistButton)
        webLinkButton = findViewById(R.id.webLinkButton)
    }

    private fun setupButtonListeners() {
        playButton.setOnClickListener {
            player?.play()
            startSeekBarUpdate()
        }

        pauseButton.setOnClickListener {
            player?.pause()
            stopSeekBarUpdate()
        }

        nextButton.setOnClickListener {
            if (player?.hasNextMediaItem() == true) {
                player?.seekToNext()
                resetSeekBar()
            } else {
                Toast.makeText(this, "Dernière chanson de la playlist", Toast.LENGTH_SHORT).show()
            }
        }

        previousButton.setOnClickListener {
            if (player?.hasPreviousMediaItem() == true) {
                player?.seekToPrevious()
                resetSeekBar()
            } else {
                Toast.makeText(this, "Première chanson de la playlist", Toast.LENGTH_SHORT).show()
            }
        }

        forward10Button.setOnClickListener {
            player?.let {
                val newPosition = it.currentPosition + 10000
                it.seekTo(newPosition.coerceAtMost(it.duration))
            }
        }

        backward10Button.setOnClickListener {
            player?.let {
                val newPosition = it.currentPosition - 10000
                it.seekTo(newPosition.coerceAtLeast(0))
            }
        }

        // ✅ Bouton pour afficher la playlist avec le launcher
        showPlaylistButton.setOnClickListener {
            musicList?.let { list ->
                val intent = Intent(this, PlaylistActivity::class.java)
                intent.putExtra("music_list", ArrayList(list.listeMusic))
                playlistLauncher?.launch(intent)  // ✅ Utilisation du launcher
            }
        }

        webLinkButton.setOnClickListener {
            musicList?.let { list ->
                val currentIndex = player?.currentMediaItemIndex ?: 0
                if (currentIndex < list.listeMusic.size) {
                    val webUrl = list.listeMusic[currentIndex].site
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(webUrl))
                    startActivity(intent)
                }
            }
        }
    }

    // ✅ Inner class pour gérer le retour de PlaylistActivity (pattern boomerang)
    inner class CallBackPlaylist : ActivityResultCallback<ActivityResult> {
        override fun onActivityResult(result: ActivityResult) {
            if (result.resultCode == Activity.RESULT_OK) {
                val selectedPosition = result.data?.getIntExtra("selected_position", -1) ?: -1
                if (selectedPosition >= 0) {
                    playSongAtPosition(selectedPosition)
                }
            }
        }
    }

    private fun playSongAtPosition(position: Int) {
        player?.seekTo(position, 0)
        player?.play()
        updateUIForCurrentSong()
        startSeekBarUpdate()
    }

    // ... (le reste de votre code reste identique)

    private fun setupSeekBar() {
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    player?.seekTo(progress.toLong())
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                stopSeekBarUpdate()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                startSeekBarUpdate()
            }
        })
    }

    private fun setupPlayerListener() {
        player?.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_READY) {
                    updateUIForCurrentSong()
                }
            }

            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                updateUIForCurrentSong()
                resetSeekBar()
            }
        })
    }

    private fun updateUIForCurrentSong() {
        musicList?.let { list ->
            val currentIndex = player?.currentMediaItemIndex ?: 0
            if (currentIndex < list.listeMusic.size) {
                val currentMusic = list.listeMusic[currentIndex]

                songTitleText.text = currentMusic.title
                artistText.text = currentMusic.artist

                Glide.with(this)
                    .load(currentMusic.image)
                    .placeholder(R.drawable.ic_music_note)
                    .into(albumImageView)

                player?.duration?.let {
                    totalTimeText.text = formatTime(it)
                    seekBar.max = it.toInt()
                }
            }
        }
    }

    private fun startSeekBarUpdate() {
        stopSeekBarUpdate()
        countDownTimer = object : CountDownTimer(Long.MAX_VALUE, 100) {
            override fun onTick(millisUntilFinished: Long) {
                player?.let {
                    seekBar.progress = it.currentPosition.toInt()
                    currentTimeText.text = formatTime(it.currentPosition)
                }
            }

            override fun onFinish() {}
        }.start()
    }

    private fun stopSeekBarUpdate() {
        countDownTimer?.cancel()
    }

    private fun resetSeekBar() {
        stopSeekBarUpdate()
        seekBar.progress = 0
        currentTimeText.text = "0:00"
        startSeekBarUpdate()
    }

    private fun formatTime(millis: Long): String {
        val seconds = (millis / 1000).toInt()
        val minutes = seconds / 60
        val secs = seconds % 60
        return String.format("%d:%02d", minutes, secs)
    }

    override fun onStart() {
        super.onStart()
        musicUpdate = GetMusic(this, url)
        musicUpdate?.addObserver(this)
        restorePlayerState()
    }

    override fun succes(lm: ListeMusics) {
        musicList = lm
        Toast.makeText(this, "${lm.listeMusic.size} chansons chargées", Toast.LENGTH_SHORT).show()

        for (music in lm.listeMusic) {
            val mediaItem = MediaItem.fromUri(music.source)
            player?.addMediaItem(mediaItem)
        }

        player?.prepare()

        val savedPosition = prefs.getInt("last_position", 0)
        if (savedPosition < lm.listeMusic.size) {
            player?.seekTo(savedPosition, prefs.getLong("last_seek", 0))
        }

        updateUIForCurrentSong()
    }

    override fun error() {
        Toast.makeText(this, "Erreur lors du chargement des chansons", Toast.LENGTH_LONG).show()
    }

    private fun savePlayerState() {
        prefs.edit().apply {
            putInt("last_position", player?.currentMediaItemIndex ?: 0)
            putLong("last_seek", player?.currentPosition ?: 0)
            apply()
        }
    }

    private fun restorePlayerState() {
        // Restauration si nécessaire
    }

    override fun onStop() {
        super.onStop()
        savePlayerState()
    }

    override fun onDestroy() {
        super.onDestroy()
        player?.release()
        stopSeekBarUpdate()
    }
}