package de.hbrs.team7.se1_starter_repo.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * Request zum Parkenden Auto aus der ccm Komponente
 * @property nr Nummer des Fahrzeugs
 *
 * @property begin Einparkzeit
 * @property duration Parkdauer
 * @property clientCategory Kundentyp (Client Category)
 * @property vehicleType Fahrzeugtyp (Vehicle Type)
 * @property license Fahrzeug-Kennzeichen
 * @property color Farbe des Autos
 * @property space Stellplatz-Nummer
 * @property price Parkgebühr
 *
 * @author Thomas Gerlach
 *  */
@Serializable
data class ParkhausServletPostDto(
    val nr: Int,
    val timer: Long,
    val duration: Long,
    val price: Double,
    val hash: String,
    val color: String,
    var space: Int,
    @SerialName("client_category") val clientCategory: String,
    @SerialName("vehicle_type") val vehicleType: String,
    val license: String,
    val begin: Long,

    )
