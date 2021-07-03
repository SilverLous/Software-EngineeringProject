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

    fun parkhausEbeneHinzufügen(ebene: ParkhausEbene) {
        ebenen.add(ebene)
    }

    fun getUebersetztesBundesland(): String? {
        var map: HashMap<String, String> = HashMap()
        map.put("Munich", "München")
        map.put("Bavaria", "Bayern")
        map.put("North Rhine-Westphalia", "Nordrhein-Westfalen")
        map.put("Hesse", "Hessen")
        map.put("Mecklenburg-Western Pomerania", "Mecklenburg-Vorpommern")
        map.put("Lower Saxony","Niedersachsen")
        map.put("Rhineland-Palatinate","Rheinland-Pfalz")
        map.put("Saxony","Sachsen")
        map.put("Lower Saxony", "Sachsen-Anhalt")
        map.put("Thuringia", "Thüringen")
        if (map.containsKey(this.bundesland)) {
            return map[bundesland]
        } else {
            return bundesland
        }
    }

    fun parkhausEbeneEntfernen(ebene: ParkhausEbene): Boolean {
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