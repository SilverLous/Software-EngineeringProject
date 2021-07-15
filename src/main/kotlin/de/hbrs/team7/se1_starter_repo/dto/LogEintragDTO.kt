package de.hbrs.team7.se1_starter_repo.dto

import kotlinx.serialization.Serializable

/**
 * Eine Datenklasse um die log Einträge zu vereinheitlichen
 * @author Thomas Gerlach
 */
@Serializable
data class LogEintragDTO(
    val zeitstempel: Long,
    val kategorie: LogKategorieDTO,
    val nachricht: String
)
