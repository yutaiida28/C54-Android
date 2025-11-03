package com.example.musicplayertp1

import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.beust.klaxon.Klaxon

/**
 * Classe pour récupérer les données musicales depuis le serveur
 * Implémente le Pattern Observer (Sujet)
 * Cette classe N'EST PAS une Activité comme demandé dans les consignes
 */
class GetMusic(context: Context, url: String) : Sujet{
    private val url = url
    private val context = context
    // Liste des observateurs qui seront notifiés
    private val mObs = mutableListOf<MusicUpdateObserver>()

    init {

        fetchMusicData()
    }

//  rajouter observer
    override fun addObserver(o: MusicUpdateObserver) {
        mObs.add(o)
    }

//  retire observer
    override fun removeObserver(o: MusicUpdateObserver) {
        mObs.remove(o)
    }

    /**
     * Notifie tous les observateurs du succès avec les données
     */
    override fun notifySuccess(lm: ListeMusics) {
        for (o in mObs) {
            o.succes(lm)
        }
    }

    /**
     * Notifie tous les observateurs d'une erreur
     */
    override fun notifyError() {
        for (o in mObs) {
            o.error()
        }
    }

    /**
     * Récupère les données JSON depuis le serveur avec Volley
     * Utilise Klaxon pour parser le JSON en objets Kotlin
     */
    private fun fetchMusicData() {
        // Création de la file de requêtes Volley
        val queue = Volley.newRequestQueue(context)

        // Création de la requête String
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                // Succès - parsing du JSON avec Klaxon
                try {
                    val musicList = Klaxon().parse<ListeMusics>(response) ?: ListeMusics()

                    if (musicList != null) {
                        // Notification des observateurs avec les données
                        notifySuccess(musicList)
                    } else {
                        // Erreur de parsing
                        notifyError()
                    }
                } catch (e: Exception) {
                    notifyError()
                }
            },
            { error ->
                // Erreur de réseau
                notifyError()
            }
        )

        // Ajout de la requête à la file
        queue.add(stringRequest)
    }
}

