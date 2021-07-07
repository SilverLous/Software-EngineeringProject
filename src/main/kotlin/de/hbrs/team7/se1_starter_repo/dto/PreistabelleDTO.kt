package de.hbrs.team7.se1_starter_repo.dto

import de.hbrs.team7.se1_starter_repo.entities.FahrzeugTyp
import java.util.*

data class PreistabelleDTO (
    var fahrzeugKlassen: MutableList<String>,
    var preise: MutableList<Double>,
    val festpreis: Float = 0.5f,
    val festpreisString: String = "50 cent * Preisklasse * Fahrzeugklasse"
)