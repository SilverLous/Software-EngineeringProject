package de.hbrs.team7.se1_starter_repo.entities

import de.hbrs.team7.se1_starter_repo.CarIF
import jakarta.persistence.*

@Entity
open class Auto(
    @Column(nullable = false)
    open var Kennzeichen: String? = null

) {

    @Id
    @GeneratedValue
    open val Autonummer: Long = 0

    @Column(nullable = true)
    var type: String = ""

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
