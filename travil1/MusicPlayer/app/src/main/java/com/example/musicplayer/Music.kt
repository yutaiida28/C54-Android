package com.example.musicplayer

import java.io.Serializable

data class Music(
    val id: String,              // Identifiant unique de la chanson
    val title: String,           // titre de la chanson
    val album: String,           // Nom de l'album
    val artist: String,          // nom de l'artiste
    val genre: String,           // genre musical
    val source: String,          // URL du fichier audio
    val image: String,           // URL de l'image de l'album
    val trackNumber: Int,        // numéro de piste dans l'album
    val totalTrackCount: Int,    // nombre total de pistes dans l'album
    val duration: Int,           // durée en secondes
    val site: String             // lien vers le site web de l'artiste/chanson

) : Serializable { }
