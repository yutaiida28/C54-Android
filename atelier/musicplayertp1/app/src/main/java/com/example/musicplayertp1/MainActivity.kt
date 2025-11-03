package com.example.musicplayertp1

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.bumptech.glide.Glide

/**
 * Activité principale du lecteur multimédia
 * Gère la lecture de musique avec ExoPlayer et l'interface utilisateur
 */
class MainActivity : AppCompatActivity(), MusicUpdateObserver {

    // URL du serveur contenant les données JSON des musiques
    private val url = "https://api.jsonbin.io/v3/b/680a6a1d8561e97a5006b822?meta=false"

    // Sujet observé pour la mise à jour des données musicales (Pattern Observer)
    private var musicUpdate: GetMusic? = null

    // Composants de l'interface utilisateur
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

    // ExoPlayer pour la lecture multimédia
    private var player: ExoPlayer? = null

    // Liste des musiques chargées depuis le serveur
    private var musicList: ListeMusics? = null

    // Timer pour mettre à jour la seekBar
    private var countDownTimer: CountDownTimer? = null

    // SharedPreferences pour la sérialisation (sauvegarder l'état)
    private lateinit var prefs: SharedPreferences

    // Launcher pour le résultat de l'activité PlaylistActivity (boomerang)
    private val playlistLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedPosition = result.data?.getIntExtra("selected_position", -1) ?: -1
            if (selectedPosition >= 0) {
                // Jouer la chanson sélectionnée depuis la playlist
                playSongAtPosition(selectedPosition)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialisation des SharedPreferences pour la sérialisation
        prefs = getSharedPreferences("MusicPlayerPrefs", MODE_PRIVATE)

        // Initialisation des vues
        initializeViews()

        // Création du ExoPlayer
        player = ExoPlayer.Builder(this).build()

        // Configuration du PlayerView (contrôles désactivés comme demandé)
        playerView.player = player
        playerView.useController = false

        // Configuration des écouteurs de boutons
        setupButtonListeners()

        // Configuration de la SeekBar
        setupSeekBar()

        // Ajout d'un écouteur d'événements sur le ExoPlayer
        setupPlayerListener()
    }

    /**
     * Initialise toutes les vues de l'interface
     */
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

    /**
     * Configure les écouteurs de clics pour tous les boutons
     */
    private fun setupButtonListeners() {
        // Bouton Play - démarre la lecture
        playButton.setOnClickListener {
            player?.play()
            startSeekBarUpdate()
        }

        // Bouton Pause - met en pause la lecture
        pauseButton.setOnClickListener {
            player?.pause()
            stopSeekBarUpdate()
        }

        // Bouton Suivant - passe à la chanson suivante
        nextButton.setOnClickListener {
            if (player?.hasNextMediaItem() == true) {
                player?.seekToNext()
                resetSeekBar()
            } else {
                Toast.makeText(this, "Dernière chanson de la playlist", Toast.LENGTH_SHORT).show()
            }
        }

        // Bouton Précédent - revient à la chanson précédente
        previousButton.setOnClickListener {
            if (player?.hasPreviousMediaItem() == true) {
                player?.seekToPrevious()
                resetSeekBar()
            } else {
                Toast.makeText(this, "Première chanson de la playlist", Toast.LENGTH_SHORT).show()
            }
        }

        // Bouton Avancer de 10 secondes
        forward10Button.setOnClickListener {
            player?.let {
                val newPosition = it.currentPosition + 10000 // 10 secondes en millisecondes
                it.seekTo(newPosition.coerceAtMost(it.duration))
            }
        }

        // Bouton Reculer de 10 secondes
        backward10Button.setOnClickListener {
            player?.let {
                val newPosition = it.currentPosition - 10000
                it.seekTo(newPosition.coerceAtLeast(0))
            }
        }

        // Bouton pour afficher la playlist (boomerang avec startActivityForResult)
        showPlaylistButton.setOnClickListener {
            musicList?.let { list ->
                val intent = Intent(this, PlaylistActivity::class.java)
                intent.putExtra("music_list", ArrayList(list.listeMusic))
                playlistLauncher.launch(intent)
            }
        }


        // Bouton pour ouvrir le lien web (Intent externe)
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
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            val selectedPosition = data?.getIntExtra("selected_position", -1) ?: -1
            if (selectedPosition >= 0) {
                playSongAtPosition(selectedPosition)
            }
        }
    }


    /**
     * Configure la SeekBar pour permettre de chercher dans la chanson (BONUS)
     */
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
                if (player?.isPlaying == true) {
                    startSeekBarUpdate()
                }
            }
        })
    }

    /**
     * Configure l'écouteur d'événements du ExoPlayer
     * Détecte les changements de média et met à jour l'interface
     */
    private fun setupPlayerListener() {
        player?.addListener(object : Player.Listener {
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                // Mise à jour de l'interface lors du changement de chanson
                updateUIForCurrentSong()
                resetSeekBar()
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                when (playbackState) {
                    Player.STATE_READY -> {
                        // Le média est prêt à être lu
                        updateUIForCurrentSong()
                    }
                    Player.STATE_ENDED -> {
                        // La playlist est terminée
                        stopSeekBarUpdate()
                    }
                }
            }
        })
    }

    /**
     * Démarre le CountDownTimer pour mettre à jour la SeekBar
     * Suit la progression de la chanson en temps réel
     */
    private fun startSeekBarUpdate() {
        stopSeekBarUpdate() // Arrêter le timer existant s'il y en a un

        val duration = player?.duration ?: 0
        val currentPosition = player?.currentPosition ?: 0

        // CountDownTimer qui se met à jour toutes les 100ms
        countDownTimer = object : CountDownTimer(duration - currentPosition, 100) {
            override fun onTick(millisUntilFinished: Long) {
                player?.let {
                    val current = it.currentPosition
                    val total = it.duration

                    if (total > 0) {
                        seekBar.max = total.toInt()
                        seekBar.progress = current.toInt()

                        // Affichage du temps en format mm:ss
                        currentTimeText.text = formatTime(current)
                        totalTimeText.text = formatTime(total)
                    }
                }
            }

            override fun onFinish() {
                // Redémarre le timer si la chanson continue
                if (player?.isPlaying == true) {
                    startSeekBarUpdate()
                }
            }
        }.start()
    }

    /**
     * Arrête le CountDownTimer
     */
    private fun stopSeekBarUpdate() {
        countDownTimer?.cancel()
        countDownTimer = null
    }

    /**
     * Réinitialise la SeekBar au début
     */
    private fun resetSeekBar() {
        seekBar.progress = 0
        currentTimeText.text = "0:00"
        if (player?.isPlaying == true) {
            startSeekBarUpdate()
        }
    }

    /**
     * Formate le temps en millisecondes en format mm:ss
     */
    private fun formatTime(millis: Long): String {
        val seconds = (millis / 1000).toInt()
        val minutes = seconds / 60
        val secs = seconds % 60
        return String.format("%d:%02d", minutes, secs)
    }

    /**
     * Met à jour l'interface pour afficher les infos de la chanson courante
     */
    private fun updateUIForCurrentSong() {
        musicList?.let { list ->
            val currentIndex = player?.currentMediaItemIndex ?: 0
            if (currentIndex < list.listeMusic.size) {
                val currentMusic = list.listeMusic[currentIndex]

                // Affichage du titre et de l'artiste
                songTitleText.text = currentMusic.title
                artistText.text = currentMusic.artist

                // Chargement de l'image avec Glide
                Glide.with(this)
                    .load(currentMusic.image)
                    .placeholder(R.drawable.ic_music_note)
                    .into(albumImageView)

                // Mise à jour du temps total
                player?.duration?.let {
                    totalTimeText.text = formatTime(it)
                    seekBar.max = it.toInt()
                }
            }
        }
    }

    /**
     * Joue une chanson à une position donnée
     */
    private fun playSongAtPosition(position: Int) {
        player?.seekTo(position, 0)
        player?.play()
        updateUIForCurrentSong()
        startSeekBarUpdate()
    }

    override fun onStart() {
        super.onStart()

        // Récupération des données musicales depuis le serveur (Pattern Observer)
        musicUpdate = GetMusic(this, url)
        musicUpdate?.addObserver(this)

        // Restauration de l'état si l'app a été quittée (sérialisation)
        restorePlayerState()
    }

    /**
     * Méthode appelée quand les données sont reçues avec succès (Pattern Observer)
     */
    override fun succes(lm: ListeMusics) {
        musicList = lm
        Toast.makeText(this, "${lm.listeMusic.size} chansons chargées", Toast.LENGTH_SHORT).show()

        // Ajout de toutes les chansons au player
        for (music in lm.listeMusic) {
            val mediaItem = MediaItem.fromUri(music.source)
            player?.addMediaItem(mediaItem)
        }

        player?.prepare()

        // Si aucune chanson n'était en cours, démarrer la première
        val savedPosition = prefs.getInt("last_position", 0)
        if (savedPosition < lm.listeMusic.size) {
            player?.seekTo(savedPosition, prefs.getLong("last_seek", 0))
        }

        updateUIForCurrentSong()
    }

    /**
     * Méthode appelée en cas d'erreur (Pattern Observer)
     */
    override fun error() {
        Toast.makeText(this, "Erreur lors du chargement des chansons", Toast.LENGTH_LONG).show()
    }

    /**
     * Sauvegarde l'état du lecteur (sérialisation)
     */
    private fun savePlayerState() {
        prefs.edit().apply {
            putInt("last_position", player?.currentMediaItemIndex ?: 0)
            putLong("last_seek", player?.currentPosition ?: 0)
            apply()
        }
    }

    /**
     * Restaure l'état du lecteur (sérialisation)
     */
    private fun restorePlayerState() {
        // L'état sera restauré après le chargement des chansons
    }

    override fun onStop() {
        super.onStop()
        // Sauvegarde de l'état lors de la fermeture
        savePlayerState()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Libération du player proprement
        stopSeekBarUpdate()
        player?.release()
        player = null
    }
}