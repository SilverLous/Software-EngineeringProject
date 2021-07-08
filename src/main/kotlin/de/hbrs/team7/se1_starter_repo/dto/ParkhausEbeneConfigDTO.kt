package de.hbrs.team7.se1_starter_repo.dto

import de.hbrs.team7.se1_starter_repo.entities.Parkhaus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
class ParkhausEbeneConfigDTO(

    @Transient
    var ebenenNamen: String = "",

    @SerialName("max") var maxPlätze: Int,
    @SerialName("open_from") var öffnungszeit: Int,
    @SerialName("open_to") var ladenschluss: Int,
    @SerialName("delay") var verzögerung: Int,
    @SerialName("time_shift") var zeitverschub: Int,
    @SerialName("simulation_speed") var simulationsGeschwindigkeit: Int,

    @Transient
    var FahrzeugPreise: HashMap<String, Double> = hashMapOf(),

    @Transient
    var parkhaus: Parkhaus? = null,


    ) {
    fun toCSV(): String {
        return listOf(maxPlätze, öffnungszeit, ladenschluss, verzögerung, simulationsGeschwindigkeit).joinToString(",")
    }
}
