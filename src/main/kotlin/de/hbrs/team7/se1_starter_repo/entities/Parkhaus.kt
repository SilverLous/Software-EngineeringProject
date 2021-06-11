package de.hbrs.team7.se1_starter_repo.entities

import de.hbrs.team7.se1_starter_repo.dto.citiesDTO
import jakarta.persistence.*


@Entity
open class Parkhaus (
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



    @OneToMany(mappedBy="Parkhaus", fetch = FetchType.EAGER, cascade = [CascadeType.ALL, CascadeType.PERSIST], orphanRemoval = true)
    var ebenen: MutableList<ParkhausEbene> = mutableListOf()

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

    companion object {

        fun fromCitiesDTO(city: citiesDTO): Parkhaus {

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