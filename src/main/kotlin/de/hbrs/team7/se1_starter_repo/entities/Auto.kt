package de.hbrs.team7.se1_starter_repo.entities

import de.hbrs.team7.se1_starter_repo.CarIF
import jakarta.persistence.*
import java.time.Instant
import java.util.*

@Entity
open class Auto(
    @Column(nullable = false)
    open var Hash: String? = null,

    @Column(nullable = false)
    open var Farbe: String? = null,

    @Column(nullable = false)
    var Platznummer: Int? = null,

    @Column(nullable = false)
    open var Kennzeichen: String? = null,

    @Column(nullable = false)
    var Typ: String = "",

    @Column(nullable = false)
    var Kategorie: String = ""


) {

    @Id
    @GeneratedValue
    open val Autonummer: Long = 0


    @Column(nullable = false)
    var ImParkhaus: Boolean = true

    @OneToOne(mappedBy = "Auto")
    var Ticket: Ticket? = null

    override fun toString(): String {
        // return params.contentToString()
        return "";
    }

    fun getParkdauer():Long{
        return this.Ticket!!.Ausfahrdatum?.time?.minus(this.Ticket!!.Ausstellungsdatum.time) ?: Date.from(Instant.now()).time - this.Ticket!!.Ausstellungsdatum.time
    }

    fun paramsToArray(): Array<String>? {
        return null
    }
}
