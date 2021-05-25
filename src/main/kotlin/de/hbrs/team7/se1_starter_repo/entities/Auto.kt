package de.hbrs.team7.se1_starter_repo.entities

import de.hbrs.team7.se1_starter_repo.CarIF
import jakarta.persistence.*

@Entity
open class Auto(
    @Column(nullable = false)
    open var Kennzeichen: String? = null

) : CarIF {

    @Id
    @GeneratedValue
    open val Autonummer: Long = 0

    @Column(nullable = true)
    override var price: Int = 0

    @Column(nullable = true)
    override var type: String = ""

    @OneToOne(mappedBy = "Auto")
    private val Ticket: Ticket? = null

    override fun toString(): String {
        // return params.contentToString()
        return "";
    }
    override fun paramsToArray(): Array<String>? {
        return null
    }
}
