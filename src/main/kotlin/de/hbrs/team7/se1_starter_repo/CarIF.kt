package de.hbrs.team7.se1_starter_repo

internal interface CarIF {
    val nr: Int
        get() = 0

    val begin: Long
        get() = 0

    val end: Long
        get() = 0

    val duration: Int
        get() = 0

    val price: Int
        get() = 0

}