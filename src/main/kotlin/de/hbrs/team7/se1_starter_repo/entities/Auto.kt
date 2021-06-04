package de.hbrs.team7.se1_starter_repo.entities

import de.hbrs.team7.se1_starter_repo.CarIF
import jakarta.persistence.*

@Entity
open class Auto(
    @Column(nullable = false)
    open var Hash: String? = null,

    @Column(nullable = false)
    open var Farbe: String? = null,

    @Column(nullable = false)
    var Platznummer: Int? = null,

    @Column(nullable = false)
    open var Kennzeichen: String? = null


) {

    @Id
    @GeneratedValue
    open val Autonummer: Long = 0

    @Column(nullable = true)
    var Typ: String = ""

    @Column(nullable = false)
    var ImParkhaus: Boolean = true

    @OneToOne(mappedBy = "Auto")
    var Ticket: Ticket? = null

    override fun toString(): String {
        // return params.contentToString()
        return "";
    }
    fun paramsToArray(): Array<String>? {
        return null
    }
}
