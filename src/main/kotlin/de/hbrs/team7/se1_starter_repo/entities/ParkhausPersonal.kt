package de.hbrs.team7.se1_starter_repo.entities

import kotlinx.serialization.json.Json

open class ParkhausPersonal(private var name: String, private var password: String) {

    private var loggedIn: Boolean = false
    fun login(username: String, givenpassword: String): Boolean {
        if (name.equals(username) and password.equals(givenpassword)) {
            loggedIn = true
        }
        return loggedIn
    }

    fun logout(username: String){
        loggedIn = false
    }

    fun statistikAbrufen(parkhaus: Parkhaus): Json? {
        return null
    }

    fun statistikAbrufen(parkhaus: Parkhaus, parkhausebene: ParkhausEbene): Json? {
        return null
    }

    fun schrankeOeffnen(parkhausebene: ParkhausEbene, schranke: Int) = {

    }

    fun schrankeSchliessen(parkhausebene: ParkhausEbene, schranke: Int) = {

    }

}