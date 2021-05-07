package de.hbrs.team7.se1_starter_repo


import jakarta.annotation.PostConstruct
import jakarta.inject.Singleton

/*

Functions may need to be open
open means not final
kotlin is final by default
 */

@Singleton
public open class ParkhausServiceGlobal { // : ParkhausServiceIF {

    open var globalCars: Int = 0

    // this is the constructor for own functionality (single called)
    @PostConstruct
    open fun sessionInit() {
        print("Hello from Singleton Service")
    }

}