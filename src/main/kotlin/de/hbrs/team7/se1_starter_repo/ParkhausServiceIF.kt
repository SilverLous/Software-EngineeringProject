package de.hbrs.team7.se1_starter_repo

import java.util.ArrayList

interface ParkhausServiceIF {

    var initNumber: Int

    public fun iterInit() = initNumber++

    fun totalCarCount(ID: String): Int

    fun totalUsers(ID: String): Int

    fun average(ID: String): Double?

    fun leaveCar(ID: String, params: Array<String>)

    fun sum(ID: String): Double

    fun addCar(ID: String, params: Array<String>)

    fun currentCars(NAME: String): ArrayList<CarIF>?




}