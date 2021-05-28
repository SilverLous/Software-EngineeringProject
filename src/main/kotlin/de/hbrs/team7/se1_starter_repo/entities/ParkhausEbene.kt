package de.hbrs.team7.se1_starter_repo.entities

import jakarta.persistence.*

@Entity
open class ParkhausEbene (

    @Column(nullable = false)
    var name: String? = null,

    @ManyToOne(fetch = FetchType.EAGER, cascade = [CascadeType.ALL, CascadeType.PERSIST])
    @JoinColumn(name = "Parkhaus_id", nullable = false)
    open var parkhaus: Parkhaus? = null

        ){

    @Id
    @GeneratedValue
    val id: Long = 0

    @Column(nullable = false)
    private var gesamtPlaetze: Int = 0



    @ManyToMany
    var tickets: ArrayList<Ticket> = ArrayList()

    private var freiePlaetze: Int = 0

    fun getGesamtPlaetze(): Int {
        return gesamtPlaetze
    }

    fun setGesamtPlaetze(neuePlaetze: Int): Int {
        val differenz = gesamtPlaetze - neuePlaetze
        if (differenz > freiePlaetze) {
            return -1
        } else {
            gesamtPlaetze = neuePlaetze
            return 0
        }
    }

    fun getFreiePlaetze(): Int {
        return freiePlaetze
    }

    fun getBelegtePlaetze(): Int {
        return gesamtPlaetze - freiePlaetze
    }

    fun autoEinfahren(): Int {
        if (freiePlaetze == 0) {
            return -1
        } else {
            freiePlaetze--
            return freiePlaetze
        }
    }

    fun autoAusfahren(): Int {
        if (getBelegtePlaetze() >= gesamtPlaetze) {
            return -1
        } else {
            freiePlaetze++
            return freiePlaetze
        }
    }

}