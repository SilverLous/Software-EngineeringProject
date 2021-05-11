package de.hbrs.team7.se1_starter_repo


import jakarta.annotation.PostConstruct
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Singleton
import java.util.ArrayList

/*

Functions may need to be open
open means not final
kotlin is final by default
 */

@ApplicationScoped
//@Singleton
public open class ParkhausServiceGlobal { // : ParkhausServiceIF {

    // private is currently not possible due to Kotlins "feature" private must be final
    open var globalCars: Int = 0
        protected set

    //TODO Add hashmap with sessionID and SessionCars for global management
    //TODO Add hashmap getter

    open var carDict: HashMap<String, ArrayList<CarIF>> = HashMap<String, ArrayList<CarIF>> ()
        protected set



    open fun addCar(ID: String, params: Array<String>) {
        globalCars++

        val newCar: CarIF = Car(params)
        println("enter,$newCar")
        carDict[ID]?.plusAssign(newCar)
    }

    // this is the constructor for own functionality (single called)
    @PostConstruct
    open fun sessionInit() {
        print("Hello from Singleton Service")
    }


}