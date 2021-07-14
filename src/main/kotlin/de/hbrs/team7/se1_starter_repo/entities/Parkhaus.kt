package de.hbrs.team7.se1_starter_repo.entities

import de.hbrs.team7.se1_starter_repo.dto.CitiesDTO
import jakarta.persistence.*

@Entity
open class Parkhaus(
    // Kombinierte Tabelle aus Stadt und Parkhaus

    @Column(nullable = false)
    var stadtname: String? = null,

    @Column(nullable = false)
    var bundesland: String? = null,

    @Column(nullable = false)
    var lat: Double? = null,

    @Column(nullable = false)
    var lng: Double? = null,

    @Column(nullable = false)
    var population: Double? = null,

    @Column(nullable = false)
    var preisklasse: Int? = null,

    ) {

    @Id
    @GeneratedValue
    open val id: Long = 0


    @OneToMany(
        mappedBy = "Parkhaus",
        fetch = FetchType.EAGER,
        cascade = [CascadeType.ALL, CascadeType.PERSIST],
        orphanRemoval = true
    )
    var ebenen: MutableList<ParkhausEbene> = mutableListOf()

    fun parkhausEbeneHinzufügen(ebene: ParkhausEbene) {
        ebenen.add(ebene)
    }

    /**
     *
     * Wird in der JSP Seite verwendet
     */
    fun getUebersetztesBundesland(): String? {

        return if (this.bundesland in BundeslandÜbersetzung) {
            BundeslandÜbersetzung[bundesland]
        } else {
            bundesland
        }
    }

    companion object {

        val BundeslandÜbersetzung = hashMapOf(
            "Munich" to "München",
            "Bavaria" to "Bayern",
            "North Rhine-Westphalia" to "Nordrhein-Westfalen",
            "Hesse" to "Hessen",
            "Mecklenburg-Western Pomerania" to "Mecklenburg-Vorpommern",
            "Lower Saxony" to "Niedersachsen",
            "Rhineland-Palatinate" to "Rheinland-Pfalz",
            "Saxony" to "Sachsen",
            "Saxony-Anhalt" to "Sachsen-Anhalt",
            "Thuringia" to "Thüringen",
        )

        fun fromCitiesDTO(city: CitiesDTO): Parkhaus {

            return Parkhaus(
                city.name,
                city.bundesland,
                city.lat,
                city.lng,
                city.population,
                city.preisklasse
            )
        }
    }
}