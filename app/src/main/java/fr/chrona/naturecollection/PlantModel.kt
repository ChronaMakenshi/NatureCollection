package fr.chrona.naturecollection

import android.accounts.AuthenticatorDescription

class PlantModel (
    val id: String = "plant0",
    val name: String = "Tulipe",
    val description: String = "Petit description",
    val imageUrl: String = "http://chrona.yt/plante.jpg",
    val grow: String = "Faible",
    val water: String = "Moyenne",
    var liked: Boolean = false
)