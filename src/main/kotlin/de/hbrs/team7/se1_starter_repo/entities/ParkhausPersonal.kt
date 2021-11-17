package de.hbrs.team7.se1_starter_repo.entities

import jakarta.persistence.*
import kotlinx.serialization.json.Json


@Entity
@Inheritance(strategy = InheritanceType.JOINED)
open class ParkhausPersonal(

    @Column(nullable = false)
    open var name: String? = null,

    @Column(nullable = false)
    open var password: String? = null,
) {

    @Id
    @GeneratedValue
    open val Personalnummer: Long = 0

    @Column(nullable = false)
    open var loggedIn = false

    @Column(nullable = false)
    open var accessLevel = 0

    fun login(username: String, givenpassword: String): Boolean {
        loggedIn = (name == username) && (password == givenpassword)
        return loggedIn
    }


    fun logout() {
        this.loggedIn = false
    }

    /**
     * die weiteren Funktionen wurden durch den Einsatz der Datenbank überflüssig
     *
     *
     */
    fun statistikAbrufen(parkhaus: Parkhaus): Json? {
        return null
    }

    fun statistikAbrufen(parkhaus: Parkhaus, parkhausebene: ParkhausEbene): Json? {
        return null
    }

    fun schrankeOeffnen(parkhausebene: ParkhausEbene, schranke: Int) = {

    }

    fun schrankeSchliessen(parkhausebene: ParkhausEbene, schranke: Int) = {

    }

}