package de.hbrs.team7.se1_starter_repo.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Basic Einführungs DTO für charts.
 *
 * @author Lukas Gerlach
 *  */

@Serializable
data class EinnahmenBarDTO(
    @SerialName("data")
    val data: List<BarData>
)

@Serializable
data class BarData(
    @SerialName("type")
    val type: String,
    @SerialName("x")
    val x: List<String>,
    @SerialName("y")
    val y: List<Double>
)