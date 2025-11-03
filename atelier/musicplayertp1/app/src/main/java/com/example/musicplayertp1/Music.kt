package com.example.musicplayertp1

import com.beust.klaxon.Json
import java.io.Serializable

/**
 * Classe représentant une musique individuelle
 * Correspond à la structure JSON du serveur
 * Implémente Serializable pour être passée entre activités
 */
data class Music(
    val id: String,              // Identifiant unique de la chanson
    val title: String,           // Titre de la chanson
    val album: String,           // Nom de l'album
    val artist: String,          // Nom de l'artiste
    val genre: String,           // Genre musical
    val source: String,          // URL du fichier audio
    val image: String,           // URL de l'image de l'album
    val trackNumber: Int,        // Numéro de piste dans l'album
    val totalTrackCount: Int,    // Nombre total de pistes dans l'album
    val duration: Int,           // Durée en secondes
    val site: String             // Lien vers le site web de l'artiste/chanson
) : Serializable


