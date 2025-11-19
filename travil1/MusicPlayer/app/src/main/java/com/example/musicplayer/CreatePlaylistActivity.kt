package com.example.musicplayer

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class CreatePlaylistActivity : AppCompatActivity() {

    private lateinit var playlistNameEditText: EditText
    private lateinit var musicListView: ListView
    private lateinit var doneButton: Button
    private lateinit var cancelButton: Button
    private val musicManager = MusicManager.getInstance()
    private val selectedMusicIndices = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_playlist)

        playlistNameEditText = findViewById(R.id.playlistNameEditText)
        musicListView = findViewById(R.id.musicListView)
        doneButton = findViewById(R.id.doneButton)
        cancelButton = findViewById(R.id.cancelButton)

        // verifie si la music a ete telecharger
        if (!musicManager.isDataLoaded) {
            Toast.makeText(this, "Please load music first", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        displayMusicList()

        doneButton.setOnClickListener {
            createPlaylist()
        }
        // anuule l'action de creation de music
        cancelButton.setOnClickListener {
            // Go back without creating playlist
            setResult(RESULT_CANCELED)
            finish()
        }
    }

    private fun displayMusicList() {
        val musicList = musicManager.getMusicList()
        val musicTitles = musicList.listeMusic.mapIndexed { index, music ->
            "${index + 1}. ${music.title} - ${music.artist}"
        }

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_multiple_choice,
            musicTitles
        )

        musicListView.adapter = adapter
        musicListView.choiceMode = ListView.CHOICE_MODE_MULTIPLE

        musicListView.setOnItemClickListener { _, _, position, _ ->
            if (selectedMusicIndices.contains(position)) {
                selectedMusicIndices.remove(position)
            } else {
                selectedMusicIndices.add(position)
            }
        }
    }

    private fun createPlaylist() {
        val playlistName = playlistNameEditText.text.toString().trim()

        // assure que l'utilisateur a mis un nom a la playliste
        if (playlistName.isEmpty()) {
            Toast.makeText(this, "Please enter a playlist name", Toast.LENGTH_SHORT).show()
            playlistNameEditText.error = "Name is required"
            return
        }

        // assure quil y a aumoin une music de selectionner
        if (selectedMusicIndices.isEmpty()) {
            Toast.makeText(this, "Please select at least one song", Toast.LENGTH_SHORT).show()
            return
        }

        // fait la playliste
        val playlist = Playlist(
            name = playlistName,
            musicIndices = selectedMusicIndices.sorted()
        )

        // ajoute la playliste dans le manager
        musicManager.addPlaylist(playlist)

        // fait un itent pour renvoiller
        val resultIntent = Intent()
        resultIntent.putExtra("playlistName", playlistName)
        resultIntent.putIntegerArrayListExtra("selectedIndices", ArrayList(selectedMusicIndices))
        resultIntent.putExtra("playlistSize", selectedMusicIndices.size)
        setResult(RESULT_OK, resultIntent)

        Toast.makeText(
            this,
            "Playlist '${playlistName}' created with ${selectedMusicIndices.size} songs",
            Toast.LENGTH_LONG
        ).show()

        // quite l'activite
        finish()
    }
}