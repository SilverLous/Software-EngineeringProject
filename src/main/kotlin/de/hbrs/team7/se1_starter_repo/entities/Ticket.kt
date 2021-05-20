package de.hbrs.team7.se1_starter_repo.entities

import jakarta.persistence.*
import java.time.Instant
import java.util.*


// maybe helpful later https://kotlinexpertise.com/hibernate-with-kotlin-spring-boot/
// https://blog.jetbrains.com/idea/2018/10/kotlin-jpa-and-spring-data/
@Entity
// @Table(name = "Tickets") //TODO Decide if class should named TicketEntity or just Ticket
open class Ticket {

    @Id @GeneratedValue
    open val Ticketnummer: Long = 0

    @Column(nullable = true)
    open var Kennzeichen: String? = null

    @Column(nullable = false)
    open var Ausstellungsdatum: Date = Date.from(Instant.now())

    @Column(nullable = true)
    open var Ausfahrdatum: Date? = null

    @Column(nullable = true)
    open var Preisklasse: Int = 0



}
