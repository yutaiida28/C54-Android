package com.example.musicplayer

import java.io.Serializable

data class Music(
    val id: String,
    val title: String,
    val album: String,
    val artist: String,
    val genre: String,
    val source: String,
    val image: String,
    val trackNumber: Int,
    val totalTrackCount: Int,
    val duration: Int,
    val site: String

) : Serializable { }
