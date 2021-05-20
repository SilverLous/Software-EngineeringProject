package de.hbrs.team7.se1_starter_repo.dto
import kotlinx.serialization.*

@Serializable
public data class ParkhausServletPostDto(
    val nr : Int,
    val timer : Long,
    val duration : Long,
    val price : Double,
    val hash : String,
    val color : String,
    val space : Int,
    @SerialName("client_category") val clientCategory : String,
    @SerialName("vehicle_type") val vehicleType : String,
    val license : String
)
