package com.example.musicplayertp1

// Interface de l'Observateur dans le Pattern Observer
interface MusicUpdateObserver {
    fun succes(lm: ListeMusics)
    fun error()
}