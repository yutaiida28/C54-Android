package com.example.musicplayer

interface Sujet {
    fun addObserver(o:MusicUpdateObserver)
    fun removeObserver(o:MusicUpdateObserver)
}