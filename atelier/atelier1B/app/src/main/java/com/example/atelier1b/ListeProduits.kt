package com.example.atelier1b

import com.beust.klaxon.Json

data class ListeProduits(
    @Json(name = "accessoires" )
    var articles: List<Produit> = emptyList()
)