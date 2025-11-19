package com.example.musicplayer

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ListView
import android.widget.SeekBar
import android.widget.SimpleAdapter
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.bumptech.glide.Glide

class MainActivity : AppCompatActivity(), MusicUpdateObserver {
    val url = "https://api.jsonbin.io/v3/b/680a6a1d8561e97a5006b822?meta=false"
    var musicUpdate: Sujet? = null
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
    private lateinit var webLinkButton: Button
    private var countDownTimer: CountDownTimer? = null

    lateinit var playingListe: ListView
    var player : ExoPlayer? = null
    private val musicManager = MusicManager.getInstance()
    private val displayHelper = HelperAffichage() //un helper car jai trouver qui a du code dont complique ma lecture ducode

    private var currentDisplayMode = DisplayMode.ALL_MUSIC // Track what we're displaying

    enum class DisplayMode {
        ALL_MUSIC,
        PLAYLIST
    }

    // Boomerang launcher for creating playlist
    private val createPlaylistLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            val playlistName = data?.getStringExtra("playlistName")
            val selectedIndices = data?.getIntegerArrayListExtra("selectedIndices")
            val playlistSize = data?.getIntExtra("playlistSize", 0)

            Toast.makeText(
                this,
                "Playlist '$playlistName' created with $playlistSize songs!",
                LENGTH_LONG
            ).show()

            displayPlaylists()
        }
    }

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
        player = ExoPlayer.Builder(this).build()
//        changer le contenue des listeview entre playlists et toutes les music
        // Setup location button to switch between All Music and Playlists view
        val location = findViewById<TextView>(R.id.location)
        location.text = "All Music"
        location.setOnClickListener {
            if (currentDisplayMode == DisplayMode.ALL_MUSIC) {
                // Switch to playlist view
                displayPlaylists()
                location.text = "Playlists"
            } else {
                // Switch back to all music view
                if (musicManager.isDataLoaded) {
                    afficher(musicManager.getMusicList())
                    currentDisplayMode = DisplayMode.ALL_MUSIC
                    location.text = "All Music"
                }
            }
        }



        // bouton pour creer une playliste
        val addPlaylist = findViewById<TextView>(R.id.AddPlaylist)
        addPlaylist.setOnClickListener {
            if (musicManager.isDataLoaded) {
                val intent = Intent(this, CreatePlaylistActivity::class.java)
                createPlaylistLauncher.launch(intent)
            } else {
                Toast.makeText(this, "Please wait for music to load", Toast.LENGTH_SHORT).show()
            }
        }

        //si music deja telecharger affiche
        if (musicManager.isDataLoaded) {
            afficher(musicManager.getMusicList())
        }
        initializerViews()
        setupButtonListeners()
    }

    private fun initializerViews() {
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
        playingListe = findViewById(R.id.playingListe)
        albumImageView = findViewById(R.id.albumImage)
        webLinkButton = findViewById(R.id.webLinkButton)
    }
    override fun onStart() {
        super.onStart()
        // lfetch si seulment music na pas ete loader
        if (!musicManager.isDataLoaded) {
            musicUpdate = GetMusic(this, url)
            (musicUpdate as GetMusic).addObserver(this)
        }
    }

    override fun succes(lm: ListeMusics) {
        Toast.makeText(this, "Loaded ${lm.listeMusic.size} songs", LENGTH_LONG).show()

        // mise a niveaux du singleton
        musicManager.updateMusicList(lm)

        // vide le mediaitem
        player?.clearMediaItems()

        for (music in lm.listeMusic){
            val mediaItem = MediaItem.fromUri(music.source)
            player?.addMediaItem(mediaItem)
        }
        player?.prepare()

        afficher(lm)
    }

    fun afficher(lm: ListeMusics){
        currentDisplayMode = DisplayMode.ALL_MUSIC
        displayHelper.setupMusicListView(
            this,
            playingListe,
            lm.listeMusic
        ) { position, _ ->
            playSongAtPosition(position)
        }
    }
    private fun playSongAtPosition(position: Int) {
        player?.seekTo(position, 0)
        player?.play()
        updateCurentSong()
        startSeekBarUpdate()
    }
//    afficher les playliste creer
    private fun displayPlaylists() {
        currentDisplayMode = DisplayMode.PLAYLIST
        val playlists = musicManager.getPlaylists()

        if (playlists.isEmpty()) {
            Toast.makeText(this, "No playlists yet. Create one!", Toast.LENGTH_SHORT).show()
            return
        }

        val playlistNames = playlists.map { playlist ->
            "${playlist.name} (${playlist.musicIndices.size} songs)"
        }

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            playlistNames
        )

        playingListe.adapter = adapter
        playingListe.setOnItemClickListener { _, _, position, _ ->
            val selectedPlaylist = playlists[position]
            displayPlaylistMusic(selectedPlaylist)
        }
    }
//    affiche toutes les music loader
    private fun displayPlaylistMusic(playlist: Playlist) {
        val playlistMusic = musicManager.getMusicFromPlaylist(playlist)

        displayHelper.setupMusicListView(
            this,
            playingListe,
            playlistMusic
        ) { position, _ ->
            val originalPosition = playlist.musicIndices[position]
            playSongAtPosition(originalPosition)
        }

        Toast.makeText(this, "Playing: ${playlist.name}", Toast.LENGTH_SHORT).show()
    }
// initialise toute les bouton de controlle
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

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    player?.seekTo(progress.toLong())
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        webLinkButton.setOnClickListener {
            val currentIndex = player?.currentMediaItemIndex ?: 0
            val musicList = musicManager.getMusicList()
            if (currentIndex < musicList.listeMusic.size) {
                val webUrl = musicList.listeMusic[currentIndex].site
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(webUrl))
                startActivity(intent)
            }
        }
    }

    private fun updateCurentSong() {
        val musicList = musicManager.getMusicList()
        if (musicList.listeMusic.isNotEmpty()) {
            val currentIndex = player?.currentMediaItemIndex ?: 0
            if (currentIndex < musicList.listeMusic.size) {
                val currentMusic = musicList.listeMusic[currentIndex]

                songTitleText.text = currentMusic.title
                artistText.text = currentMusic.artist

                Glide.with(this)
                    .load(currentMusic.image)
                    .into(albumImageView)

                val durationMillis = currentMusic.duration * 1000L
                totalTimeText.text = formatTime(durationMillis)
                seekBar.max = durationMillis.toInt()
            }
        }
    }
//    thread pour la mise a jour du seekbar
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

        updateCurentSong()
        startSeekBarUpdate()
    }
    private fun formatTime(millis: Long): String {
        val seconds = (millis / 1000).toInt()
        val minutes = seconds / 60
        val secs = seconds % 60
        return String.format("%d:%02d", minutes, secs)
    }
    override fun error() {
        Toast.makeText(this, "Error loading music", LENGTH_LONG).show()
    }
}