package de.hbrs.team7.se1_starter_repo.entities

import jakarta.persistence.*
import java.time.Instant
import java.util.*

/**
 * @author Thomas Gerlach
 */
@Entity
open class Auto(
    @Column(nullable = false)
    open var hash: String? = null,

    @Column(nullable = false)
    open var farbe: String? = null,

    @Column(nullable = false)
    var platznummer: Int? = null,

    @Column(nullable = false)
    open var kennzeichen: String? = null,

    @Column(nullable = false)
    var typ: String = "",

    @Column(nullable = false)
    var kategorie: String = ""

) {

    @Id
    @GeneratedValue
    open val autonummer: Long = 0


    @Column(nullable = false)
    var imParkhaus: Boolean = true

    @OneToOne(mappedBy = "Auto")
    var ticket: Ticket? = null

    fun getParkdauer(): Long {
        return this.ticket!!.Ausfahrdatum?.time?.minus(this.ticket!!.Ausstellungsdatum.time)
            ?: Date.from(Instant.now()).time - this.ticket!!.Ausstellungsdatum.time
    }

}
