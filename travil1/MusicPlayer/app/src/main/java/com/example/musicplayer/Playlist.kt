package com.example.musicplayer

import java.io.Serializable

data class Playlist(val name: String,
                    val musicIndices: List<Int>) : Serializable
