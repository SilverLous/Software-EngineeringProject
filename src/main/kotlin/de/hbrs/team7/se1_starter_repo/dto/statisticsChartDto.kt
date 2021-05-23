package de.hbrs.team7.se1_starter_repo.dto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class statisticsChartDto(
    @SerialName("data")
    val `data`: List<Data>
)
@Serializable
data class Data(
    @SerialName("type")
    val type: String,
    @SerialName("x")
    val x: List<String>,
    @SerialName("y")
    val y: ArrayList<Double>
)