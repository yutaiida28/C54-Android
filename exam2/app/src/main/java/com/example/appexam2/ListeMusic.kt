package com.example.appexam2

import com.beust.klaxon.Json

class ListeMusic {
    @Json(name = "items" )
    var nom: List<Music> = emptyList()
}