package de.hbrs.team7.se1_starter_repo.dto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class einnahmenBarDTO(
    @SerialName("data")
    val `data`: List<BarData>
)
@Serializable
data class BarData(

    val type: String = "bar",
    @SerialName("x")
    val x: String,
    @SerialName("y")
    val y: Double
)