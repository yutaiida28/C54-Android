package com.example.musicplayer

import com.beust.klaxon.Json

data class ListeMusics(
    @Json(name = "music")
    var listeMusic: List<Music> = emptyList()
)
