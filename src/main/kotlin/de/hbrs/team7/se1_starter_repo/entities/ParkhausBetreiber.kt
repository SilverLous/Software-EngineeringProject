package de.hbrs.team7.se1_starter_repo.entities

import jakarta.persistence.Entity

@Entity
open class ParkhausBetreiber(
    name: String, password: String) : ParkhausPersonal(name, password) {
    constructor() : this("", "") {

    }


    fun preisfaktorAendern(parkhaus: Parkhaus, newValue: Float) {

    }

    fun preisfaktorAendern(parkhaus: Parkhaus, ebene: ParkhausEbene, newValue: Float) {

    }
}