package com.example.musicplayertp1

//Interface du Sujet dans le Pattern Observer
interface Sujet {
    fun addObserver(o: MusicUpdateObserver)
    fun removeObserver(o: MusicUpdateObserver)
    fun notifySuccess(lm: ListeMusics)
    fun notifyError()
}