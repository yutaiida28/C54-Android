package com.example.musicplayer
//ceci est la class qui gere tout les element de l'app qui possede les musique est les playliste
class MusicManager private constructor() {
    // Ici on force que toute l'app se serve de listeMusics en lecture seule
    // Seul MusicManager peut la modifier (setter privé)
    var listeMusics: ListeMusics = ListeMusics()
        private set

    var isDataLoaded: Boolean = false
        private set

    //  une liste mutable qui est modifiable meme si elle est private
    private val playlists = mutableListOf<Playlist>()
    //
    companion object {
//      Volatile est ma solution du web qui assure que tous les threads voient immédiatement les changements sur instance
        @Volatile
        private var instance: MusicManager? = null
//      la function pour pouvoir obtenir l'instance a l'exterieur de la class
        fun getInstance(): MusicManager {
            return instance ?: synchronized(this) {
                instance ?: MusicManager().also { instance = it }
            }
        }
    }

    fun updateMusicList(lm: ListeMusics) {
        this.listeMusics = lm
        this.isDataLoaded = true
    }

    fun getMusicList(): ListeMusics {
        return listeMusics
    }

    fun getMusicAt(position: Int): Music? {
        return if (position in listeMusics.listeMusic.indices) {
            listeMusics.listeMusic[position]
        } else {
            null
        }
    }

    // pour ajouter une playliste
    fun addPlaylist(playlist: Playlist) {
        playlists.add(playlist)
    }

    fun getPlaylists(): List<Playlist> {
        return playlists.toList()
    }

    fun getMusicFromPlaylist(playlist: Playlist): List<Music> {
        return playlist.musicIndices.mapNotNull { index ->
            getMusicAt(index)
        }
    }

    fun deletePlaylist(index: Int) {
        if (index in playlists.indices) {
            playlists.removeAt(index)
        }
    }
}