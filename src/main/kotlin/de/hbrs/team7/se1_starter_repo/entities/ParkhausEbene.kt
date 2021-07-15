package de.hbrs.team7.se1_starter_repo.entities

import de.hbrs.team7.se1_starter_repo.dto.ParkhausEbeneConfigDTO
import jakarta.persistence.*

/**
 * @author Thomas Gerlach
 */
@Entity
open class ParkhausEbene(

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

    ) {

    @Id
    @GeneratedValue
    val id: Long = 0

    @Column(nullable = false)
    @Deprecated("use maxPlätze")
    internal var gesamtPlaetze: Int = 0

    @ManyToMany
    var tickets: ArrayList<Ticket> = ArrayList()

    @OneToMany(
        mappedBy = "ParkhausEbene",
        fetch = FetchType.EAGER,
        cascade = [CascadeType.ALL, CascadeType.PERSIST],
        orphanRemoval = true
    )
    var fahrzeugTypen: MutableList<FahrzeugTyp> = mutableListOf()


    fun toConfigCSV(): String {
        return listOf(maxPlätze, öffnungszeit, ladenschluss, verzögerung, simulationsGeschwindigkeit).joinToString(",")
    }

    fun toConfigDTO(): ParkhausEbeneConfigDTO {

        return ParkhausEbeneConfigDTO(
            ebenenNamen = this.name!!,
            maxPlätze = this.maxPlätze,
            öffnungszeit = this.öffnungszeit,
            ladenschluss = this.ladenschluss,
            verzögerung = this.verzögerung,
            zeitverschub = 0,
            simulationsGeschwindigkeit = simulationsGeschwindigkeit,
            FahrzeugPreise = hashMapOf(),
        )
    }

    /**
     * Wurde zur Typkonversion zwischen Java und Kotlin verwendet, wird für zukünftige eventuelle Probleme behalten
     *
     * @author Alexander Bohl
     */
    fun getIdAlsString(): String {
        return id.toString()
    }

    fun parkhausZuweisen(parkhaus: Parkhaus) {
        this.parkhaus = parkhaus

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