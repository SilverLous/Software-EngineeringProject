package de.hbrs.team7.se1_starter_repo.entities

import de.hbrs.team7.se1_starter_repo.CarIF
import jakarta.persistence.*

@Entity
open class Auto(
    @Column(nullable = false)
    open var Kennzeichen: String? = null

) : CarIF {

    override var price: Int = 0
    override var type: String = ""

    @Id
    @GeneratedValue
    open val Autonummer: Long = 0

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
