package com.example.musicplayertp1

//Interface du Sujet dans le Pattern Observer
interface Sujet {
    fun addObserver(observer: MusicUpdateObserver)
    fun removeObserver(observer: MusicUpdateObserver)
    fun notifySuccess(lm: ListeMusics)
    fun notifyError()
}