package de.hbrs.team7.se1_starter_repo.dto

import de.hbrs.team7.se1_starter_repo.entities.Parkhaus

class ParkhausEbeneConfigDTO(

    var ebenenNamen: String = "",
    var maxPlätze: Int = 0,
    var öffnungszeit: Int = 0,
    var ladenschluss: Int = 0,
    var verzögerung: Int = 0,
    var simulationsGeschwindigkeit: Int = 0,
    var parkhaus: Parkhaus? = null,


    ) {
    fun toCSV(): String {
        return listOf(maxPlätze, öffnungszeit, ladenschluss, verzögerung, simulationsGeschwindigkeit).joinToString(",")
    }
}
