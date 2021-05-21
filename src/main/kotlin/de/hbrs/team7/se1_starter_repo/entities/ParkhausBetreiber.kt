package de.hbrs.team7.se1_starter_repo.entities

class ParkhausBetreiber(private var name: String, private var password: String) : ParkhausPersonal(name, password) {

    fun preisfaktorAendern(parkhaus: Parkhaus, newValue: Float) {

    }

    fun preisfaktorAendern(parkhaus: Parkhaus, ebene: ParkhausEbene, newValue: Float) {

    }
}