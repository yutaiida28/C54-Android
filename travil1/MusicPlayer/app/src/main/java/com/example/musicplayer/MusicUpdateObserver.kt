package com.example.musicplayer

interface MusicUpdateObserver {
    fun succes(lm : ListeMusics)
    fun error()
}