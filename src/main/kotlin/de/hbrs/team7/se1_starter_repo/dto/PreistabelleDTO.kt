package de.hbrs.team7.se1_starter_repo.dto

import kotlinx.serialization.Serializable

@Serializable
/**
 * fahrzeugKlassen: eine Liste der Fahrzeugtyp.typ, die einen Preisfaktor im Parkhaus haben
 * preise: eine Liste der Preismultiplikatoren der Fahrzeuge, die in fahrzeugKlassen am gleichen Index stehen
 * festpreis: gibt den Festpreis an, den man bezahlt, selbst wenn man sofort wieder rausfährt
 * festpreisString: eine String-Darstellung des festpreises mit etwas Text drumherum
 *
 * @author Alexander Bohl
 */
data class PreistabelleDTO (
    var fahrzeugKlassen: MutableList<String>,
    var preise: MutableList<Double>,
    val festpreis: Float,
    val festpreisString: String
)