package de.hbrs.team7.se1_starter_repo

import de.hbrs.team7.se1_starter_repo.entities.Auto
import java.util.ArrayList

interface ParkhausServiceIF {

    /*Kontextvariablen:
    persistentSum: Summe der Parkgebühren aller Autos
    totalCars: Counter, wie viele Autos jemals im Parkhaus waren
    carsHaveLeft: Autos, die das Parkhaus verlassen haben
    */

    var initNumber: Int

    public fun iterInit() = initNumber++

    fun totalCarCount(ID: String): Int

    fun totalUsers(ID: String): Int

    fun average(ID: String): Double?

    fun leaveCar(ID: String, params: Array<String>)

    fun sum(ID: String): Double

    fun addCar(ID: String, params: Array<String>)

    fun currentCars(NAME: String): ArrayList<Auto>?

}