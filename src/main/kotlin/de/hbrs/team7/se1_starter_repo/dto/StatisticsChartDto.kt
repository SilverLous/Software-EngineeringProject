package de.hbrs.team7.se1_starter_repo.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Ein DTO dass Daten für charts speichert und die Achsen beschriftbar macht
 *
 * @author Lukas Gerlach
 *  */

@Serializable
data class StatisticsChartDto(
    @SerialName("data")
    val `data`: List<CarData>,
    @SerialName("layout")
    val Layout: Layout
)

@Serializable
data class CarData(
    @SerialName("type")
    val type: String,
    @SerialName("x")
    val x: List<String>,
    @SerialName("y")
    val y: List<Double>,
    @SerialName("marker")
    val Marker: Marker,
)

@Serializable
data class Marker(
    @SerialName("color")
    val color: List<String>
)

@Serializable
data class Layout(
    @SerialName("xaxis")
    val Xaxis: Xaxis,
    @SerialName("yaxis")
    val Yaxis: Yaxis
)

@Serializable
data class Xaxis(
    @SerialName("title")
    val Title: Title
)

@Serializable
data class Yaxis(
    @SerialName("title")
    val Title: Title
)

@Serializable
data class Title(
    @SerialName("text")
    val text: String
)
