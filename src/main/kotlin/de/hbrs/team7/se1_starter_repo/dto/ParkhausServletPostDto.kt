package de.hbrs.team7.se1_starter_repo.dto
import kotlinx.serialization.*


/**
 * Request zum Parkenden Auto
 * @property nr Nummer des Fahrzeugs
 *
 * @property von Einparkzeit
 * @property bis Ausparkzeit
 * @property dauer Parkdauer
 * @property clientCategory Kundentyp (Client Category)
 * @property vehicleType Fahrzeugtyp (Vehicle Type)
 * @property ticket Ticket-Hashwert
 * @property license Fahrzeug-Kennzeichen
 * @property color Farbe des Autos
 * @property space Stellplatz-Nummer
 * @property preis Parkgebühr
 *
 *  */
@Serializable
public data class ParkhausServletPostDto(
    val nr : Int,
    val timer : Long,
    val duration : Long,
    val price : Double,
    val hash : String,
    val color : String,
    var space : Int,
    @SerialName("client_category") val clientCategory : String,
    @SerialName("vehicle_type") val vehicleType : String,
    val license : String
)
