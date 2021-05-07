package de.hbrs.team7.se1_starter_repo

import jakarta.annotation.PostConstruct
import jakarta.enterprise.context.SessionScoped
import java.io.Serializable


/*

BIG WARNING DURING LANG FEATURES ALL VALUES MUST BE OPEN!!!

 */
@SessionScoped
open class ParkhausServiceSession : Serializable {

    open var sessionCars: Int = 0

    // this is the constructor for own functionality (called per new browser connection)
    @PostConstruct
    open fun sessionInit() {
        print("Hello from Session Service new User")
    }

}