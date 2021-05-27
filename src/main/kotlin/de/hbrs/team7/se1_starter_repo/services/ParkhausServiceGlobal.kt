package de.hbrs.team7.se1_starter_repo.services


import de.hbrs.team7.se1_starter_repo.dto.ParkhausServletPostDto
import jakarta.annotation.PostConstruct
import jakarta.enterprise.context.ApplicationScoped


/*
Functions may need to be open
open means not final
kotlin is final by default
 */

@ApplicationScoped
//@Singleton
open class ParkhausServiceGlobal { // : ParkhausServiceIF {


    open val levelSet = mutableSetOf<String>()

    // private is currently not possible due to Kotlins "feature" private must be final
    open var globalCars: Int = 0
        protected set


    // this is the constructor for own functionality (single called)
    @PostConstruct
    open fun sessionInit() {
        print("Hello from Singleton Service")
    }


}