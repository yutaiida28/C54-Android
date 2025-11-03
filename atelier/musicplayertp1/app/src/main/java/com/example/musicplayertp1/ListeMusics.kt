package com.example.musicplayertp1

import com.beust.klaxon.Json

/**
 * Classe représentant la liste complète de musiques
 * Correspond à la structure JSON du serveur
 * Implémente Serializable pour être passée entre activités
 */

data class ListeMusics(
    @Json(name = "music")
    var listeMusic: List<Music> = emptyList()
)
