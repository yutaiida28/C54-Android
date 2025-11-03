package com.example.musicplayer

class MusicManager private constructor() {

    var listeMusics: ListeMusics = ListeMusics()
        private set

    var isDataLoaded: Boolean = false
        private set

    // Store user playlists
    private val playlists = mutableListOf<Playlist>()

    companion object {
        @Volatile
        private var instance: MusicManager? = null

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

    fun getMusicCount(): Int {
        return listeMusics.listeMusic.size
    }

    // Playlist management
    fun addPlaylist(playlist: Playlist) {
        playlists.add(playlist)
    }

    fun getPlaylists(): List<Playlist> {
        return playlists.toList()
    }

    fun getPlaylist(index: Int): Playlist? {
        return if (index in playlists.indices) playlists[index] else null
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