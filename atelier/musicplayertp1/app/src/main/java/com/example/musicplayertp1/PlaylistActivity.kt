package com.example.musicplayertp1

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide

/**
 * Activité affichant la liste complète des chansons
 * Utilise un ListView avec SimpleAdapter
 * Implémente le pattern "boomerang" avec startActivityForResult
 */
class PlaylistActivity : AppCompatActivity() {

    private lateinit var playlistView: ListView
    private var musicList: ArrayList<Music>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_playlist)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Récupération de la liste de musiques passée par l'intent (ArrayList)

        musicList = intent.getSerializableExtra("music_list") as? ArrayList<Music>
        playlistView = findViewById(R.id.playlistView)

        // Affichage de la playlist
        displayPlaylist()
    }

    /**
     * Affiche la playlist dans un ListView avec SimpleAdapter
     */
    private fun displayPlaylist() {
        musicList?.let { list ->
            // Préparation des données pour le SimpleAdapter
            val dataList = ArrayList<HashMap<String, String>>()

            // Itérer directement sur l'ArrayList<Music>
            for (music in list) {
                val item = HashMap<String, String>()
                item["title"] = music.title
                item["artist"] = "${music.artist} • ${music.album}"  // Artiste + Album
                item["duration"] = formatDuration(music.duration)
                item["image"] = music.image
                dataList.add(item)
            }

            // Création du SimpleAdapter personnalisé
            val adapter = CustomSimpleAdapter(
                this,
                dataList,
                R.layout.playlist_item,
                arrayOf("title", "artist", "duration", "image"),
                intArrayOf(R.id.itemTitle, R.id.itemArtist, R.id.itemDuration, R.id.itemImage)
            )

            // Configuration du ListView
            playlistView.adapter = adapter

            // Gestion du clic sur un item - CRITIQUE pour le boomerang
            playlistView.setOnItemClickListener { parent, view, position, id ->
                // Retour à MainActivity avec la position sélectionnée (boomerang)
                val resultIntent = Intent()
                resultIntent.putExtra("selected_position", position)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
        }
    }

    /**
     * Formate la durée en secondes vers mm:ss
     */
    private fun formatDuration(seconds: Int): String {
        val minutes = seconds / 60
        val secs = seconds % 60
        return String.format("%d:%02d", minutes, secs)
    }

    /**
     * Adapter personnalisé qui étend SimpleAdapter
     * Permet de charger les images avec Glide
     */
    inner class CustomSimpleAdapter(
        context: Context,
        data: ArrayList<HashMap<String, String>>,
        resource: Int,
        from: Array<String>,
        to: IntArray
    ) : SimpleAdapter(context, data, resource, from, to) {

        override fun setViewImage(imageView: ImageView, value: String) {
            // Chargement de l'image avec Glide au lieu de l'implémentation par défaut
            Glide.with(imageView.context)
                .load(value)
                .placeholder(R.drawable.ic_music_note)
                .into(imageView)
        }

        override fun setViewText(textView: TextView, text: String) {
            super.setViewText(textView, text)
            // Active le défilement pour les titres longs (marquee)
            if (textView.id == R.id.itemTitle) {
                textView.isSelected = true
            }
        }
    }
}