package de.hbrs.team7.se1_starter_repo.dto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class statisticsChartDto(
    @SerialName("data")
    val `data`: List<carData>,
    @SerialName("layout")
    val `layout`: layout
)
@Serializable
data class carData(
    @SerialName("type")
    val type: String,
    @SerialName("x")
    val x: List<String>,
    @SerialName("y")
    val y: List<Double>,
    @SerialName("marker")
    val marker: marker,
)

@Serializable
data class marker(
    @SerialName("color")
    val color: List<String>
)

@Serializable
data class layout(
    @SerialName("xaxis")
    val xaxis: xaxis,
    @SerialName("yaxis")
    val yaxis: yaxis
)

@Serializable
data class xaxis(
    @SerialName("title")
    val title: title
)

@Serializable
data class yaxis(
    @SerialName("title")
    val title: title
)

@Serializable
data class title(
    @SerialName("text")
    val text: String
)
