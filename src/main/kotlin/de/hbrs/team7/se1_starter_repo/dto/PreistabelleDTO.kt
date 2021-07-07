package de.hbrs.team7.se1_starter_repo.dto

import kotlinx.serialization.Serializable

@Serializable
data class PreistabelleDTO (
    var fahrzeugKlassen: MutableList<String>,
    var preise: MutableList<Double>,
    val festpreis: Float,
    val festpreisString: String
)