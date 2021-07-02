package de.hbrs.team7.se1_starter_repo.dto

import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class LogEintragDTO(
    val zeitstempel: Long,
    val kategorie: LogKategorieDTO,
    val nachricht: String
)
