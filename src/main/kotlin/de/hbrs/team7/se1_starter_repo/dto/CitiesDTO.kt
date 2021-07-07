package de.hbrs.team7.se1_starter_repo.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 *
 * Eine Datenklasse um die JSON werte der Städte zu verarbeiten
 *
 * @author Thomas Gerlach
 */
@Serializable
data class CitiesDTO(
    @SerialName("an")
    val bundesland: String,
    @SerialName("ct")
    val name: String,
    @SerialName("id")
    val id: Int,
    @SerialName("lat")
    val lat: Double,
    @SerialName("lng")
    val lng: Double,
    @SerialName("po")
    val population: Double,
    @SerialName("pt")
    val preisklasse: Int
)