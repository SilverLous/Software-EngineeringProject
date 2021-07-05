package de.hbrs.team7.se1_starter_repo.entities

import de.hbrs.team7.se1_starter_repo.dto.ParkhausEbeneConfigDTO
import jakarta.persistence.*

@Entity
open class ParkhausEbene (

    @Column(nullable = false)
    var name: String? = null,

    @ManyToOne(fetch = FetchType.EAGER, cascade = [CascadeType.ALL, CascadeType.PERSIST])
    @JoinColumn(name = "Parkhaus_id", nullable = false)
    open var parkhaus: Parkhaus? = null,

    @Column(nullable = false)
    var maxPlätze: Int = 0,

    @Column(nullable = false)
    var öffnungszeit: Int = 0,

    @Column(nullable = false)
    var ladenschluss: Int = 0,

    @Column(nullable = false)
    var verzögerung: Int = 0,

    @Column(nullable = false)
    var simulationsGeschwindigkeit: Int = 0,

        ){

    @Id
    @GeneratedValue
    val id: Long = 0

    @Column(nullable = false)
    @Deprecated("use maxPlätze")
    internal var gesamtPlaetze: Int = 0

    @ManyToMany
    var tickets: ArrayList<Ticket> = ArrayList()

    @OneToMany(mappedBy="ParkhausEbene", fetch = FetchType.EAGER, cascade = [CascadeType.ALL, CascadeType.PERSIST], orphanRemoval = true)
    var fahrzeugTypen: MutableList<FahrzeugTyp> = mutableListOf()


    fun toConfigCSV(): String {
        return listOf(maxPlätze, öffnungszeit, ladenschluss, verzögerung, simulationsGeschwindigkeit).joinToString(",")
    }



    private var freiePlaetze: Int = 0

    fun getIdAlsString(): String {
        return id.toString()
    }

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

    fun parkhausZuweisen(parkhaus: Parkhaus) {
        this.parkhaus = parkhaus;

    }

    companion object {

        fun ausEbenenConfig(ebenenConfig: ParkhausEbeneConfigDTO): ParkhausEbene {

            return ParkhausEbene(
                ebenenConfig.ebenenNamen,
                ebenenConfig.parkhaus,
                ebenenConfig.maxPlätze,
                ebenenConfig.öffnungszeit,
                ebenenConfig.ladenschluss,
                ebenenConfig.verzögerung,
                ebenenConfig.simulationsGeschwindigkeit,
            )
        }
    }

}