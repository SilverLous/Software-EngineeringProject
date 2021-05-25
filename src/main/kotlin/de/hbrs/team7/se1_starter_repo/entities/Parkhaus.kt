package de.hbrs.team7.se1_starter_repo.entities

import jakarta.persistence.*

@Entity
open class Parkhaus (

    @Column(nullable = false)
    var name: String? = null

        ) {

    @Id
    @GeneratedValue
    open val id: Long = 0



    @OneToMany(mappedBy="Parkhaus")
    var ebenen: ArrayList<ParkhausEbene> = ArrayList()

    fun addParkhausEbene(ebene: ParkhausEbene) {
        ebenen.add(ebene)
    }

    fun removeParkhausEbene(ebene: ParkhausEbene): Boolean {
        return ebenen.remove(ebene)
    }

    fun autoEinfahren(ebene: Int): Int {
        return ebenen[ebene].autoEinfahren()
    }

    fun autoAusfahren(ebene: Int): Int {
        return ebenen[ebene].autoAusfahren()
    }
}