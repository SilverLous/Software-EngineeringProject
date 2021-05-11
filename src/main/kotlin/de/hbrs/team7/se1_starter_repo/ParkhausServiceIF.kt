package de.hbrs.team7.se1_starter_repo

import java.util.ArrayList

interface ParkhausServiceIF {

    var initNumber: Int;

    public fun iterInit() = initNumber++;

    open fun totalCarCount(ID: String): Int;

    open fun totalUsers(ID: String): Int;

    open fun average(ID: String): Double?;

    open fun leaveCar(ID: String, params: Array<String>);

    open fun sum(ID: String): Double;

    open fun addCar(ID: String, params: Array<String>);

    open fun currentCars(NAME: String): ArrayList<CarIF>?;




}