package de.hbrs.team7.se1_starter_repo.entities

import jakarta.persistence.*

/**
 * @author Thomas Gerlach
 */
@Entity
open class FahrzeugTyp(

    @Column(nullable = false)
    var typ: String? = null,

    @Column(nullable = false)
    var multiplikator: Double = 1.0,

    @ManyToOne(fetch = FetchType.EAGER, cascade = [CascadeType.ALL, CascadeType.PERSIST])
    @JoinColumn(name = "parkhausebene_id", nullable = false)
    open var parkhausEbene: ParkhausEbene? = null
) {

    @Id
    @GeneratedValue
    open val id: Long = 0


    companion object {

        fun ausHashMapEintrag(mapEintrag: Map.Entry<String, Double>, parkhausEbene: ParkhausEbene): FahrzeugTyp {

            return FahrzeugTyp(
                typ = mapEintrag.key,
                multiplikator = mapEintrag.value,
                parkhausEbene = parkhausEbene
            )
        }
    }
}

