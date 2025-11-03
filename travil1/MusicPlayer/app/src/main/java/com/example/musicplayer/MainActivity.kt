package com.example.musicplayer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ListView
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
    lateinit var playingListe: ListView
    lateinit var playingNow: FrameLayout
    var player : ExoPlayer? = null
    private val musicManager = MusicManager.getInstance()

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

            // Display the newly created playlist
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
        playingNow = findViewById(R.id.playingMusic)
        player = ExoPlayer.Builder(this).build()

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

        // Setup Add Playlist button
        val addPlaylist = findViewById<TextView>(R.id.AddPlaylist)
        addPlaylist.setOnClickListener {
            if (musicManager.isDataLoaded) {
                val intent = Intent(this, CreatePlaylistActivity::class.java)
                createPlaylistLauncher.launch(intent)
            } else {
                Toast.makeText(this, "Please wait for music to load", Toast.LENGTH_SHORT).show()
            }
        }

        // If data is already loaded, display it immediately
        if (musicManager.isDataLoaded) {
            afficher(musicManager.getMusicList())
        }
    }

    override fun onStart() {
        super.onStart()
        // Only fetch if data hasn't been loaded yet
        if (!musicManager.isDataLoaded) {
            musicUpdate = GetMusic(this, url)
            (musicUpdate as GetMusic).addObserver(this)
        }
    }

    override fun succes(lm: ListeMusics) {
        Toast.makeText(this, "Loaded ${lm.listeMusic.size} songs", LENGTH_LONG).show()

        // Update the singleton with the music list
        musicManager.updateMusicList(lm)

        // Clear existing player items first
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

        adap.viewBinder = SimpleAdapter.ViewBinder { view, data, textRepresentation ->
            if (view.id == R.id.name && view is TextView) {
                view.text = textRepresentation
                view.isSelected = true
                true
            } else {
                false
            }
        }

        playingListe.adapter = adap
        playingListe.setOnItemClickListener { _, _, position, _ ->
            val selectedMusic = lm.listeMusic[position]
            playMusic(selectedMusic, position)
        }
    }

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

    private fun displayPlaylistMusic(playlist: Playlist) {
        val playlistMusic = musicManager.getMusicFromPlaylist(playlist)

        val remplir = ArrayList<HashMap<String, String>>()
        for (music in playlistMusic) {
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

        adap.viewBinder = SimpleAdapter.ViewBinder { view, data, textRepresentation ->
            if (view.id == R.id.name && view is TextView) {
                view.text = textRepresentation
                view.isSelected = true
                true
            } else {
                false
            }
        }

        playingListe.adapter = adap
        playingListe.setOnItemClickListener { _, _, position, _ ->
            val selectedMusic = playlistMusic[position]
            // Get the original position in the full music list
            val originalPosition = playlist.musicIndices[position]
            playMusic(selectedMusic, originalPosition)
        }

        Toast.makeText(this, "Playing: ${playlist.name}", Toast.LENGTH_SHORT).show()
    }

    private fun playMusic(music: Music, position: Int) {
        playingNow.removeAllViews()
        val musicView = layoutInflater.inflate(R.layout.playing_music_main, playingNow, false)

        val titleView = musicView.findViewById<TextView>(R.id.musicTitle)
        val imageView = musicView.findViewById<ImageView>(R.id.musicIcon)

        titleView.text = music.title

        Glide.with(this)
            .load(music.image)
            .into(imageView)

        player?.seekTo(position, 0)
        player?.play()
        playingNow.addView(musicView)
        playingNow.visibility = View.VISIBLE
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
        Toast.makeText(this, "Error loading music", LENGTH_LONG).show()
    }
}