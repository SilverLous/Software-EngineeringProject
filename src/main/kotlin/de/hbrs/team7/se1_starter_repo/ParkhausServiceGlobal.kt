package de.hbrs.team7.se1_starter_repo


import de.hbrs.team7.se1_starter_repo.dto.ParkhausServletPostDto
import jakarta.annotation.PostConstruct
import jakarta.enterprise.context.ApplicationScoped
import jakarta.persistence.Persistence
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.eclipse.persistence.internal.sessions.DirectCollectionChangeRecord
import java.time.Instant
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


/*

Functions may need to be open
open means not final
kotlin is final by default
 */

@ApplicationScoped
//@Singleton
open class ParkhausServiceGlobal { // : ParkhausServiceIF {

    // private is currently not possible due to Kotlins "feature" private must be final
    open var globalCars: Int = 0
        protected set

    //TODO Add hashmap with sessionID and SessionCars for global management
    //TODO Add hashmap getter

    open var carDict: HashMap<String, ArrayList<CarIF>> = HashMap<String, ArrayList<CarIF>> ()
        protected set

    open var currentCarDict: HashMap<String,HashMap<Int, CarIF>> = HashMap<String, HashMap<Int, CarIF>> ()
        protected set

    open var jsonData: ParkhausServletPostDto? = null
        protected set

    open fun addCar(ID: String, params: Array<String>) {
        globalCars++
        val placeNumber = params[1].substring(params[1].indexOf(":")+2).toInt()
        val newCar: CarIF = Car(params)
        if (carDict[ID] == null){
            carDict[ID] = arrayListOf(newCar)
        }
        else {
            carDict[ID]?.plusAssign(newCar)
            //currentCarDict[ID]?.plusAssign(newCar)
        }
        currentCarDict[ID]?.set(placeNumber, newCar)

        for (car in carDict[ID]!!){
            val carParams = car.paramsToArray()?.drop(1)
            if (carParams != null) {
                for (string in carParams){
                    println(string)
                }
            }

        }
        //println(carDict[ID])
        println("enter,$newCar")
        if (jsonData == null){
            //jsonData = ParkhausServletPostDto(HashMap<String, String>())
        }

    }

    open fun addCar(ID: String, params: ParkhausServletPostDto) {
        globalCars++
        val placeNumber = params.nr
        val paramArray = arrayOf(params.toString())
        val newCar: CarIF = Car(paramArray)
        newCar.type = params.vehicleType
        if (carDict[ID] == null){
            carDict[ID] = arrayListOf(newCar)
            var carsDict = HashMap<Int, CarIF>()
            currentCarDict[ID] = carsDict

        }
        else {
            carDict[ID]?.add(newCar)
            //currentCarDict[ID]?.plusAssign(newCar)
        }
        currentCarDict[ID]?.set(placeNumber, newCar)

        for (car in carDict[ID]!!){
            val carParams = car.paramsToArray()?.drop(1)
            if (carParams != null) {
                for (string in carParams){
                    println(string)
                }
            }

        }
        //println(carDict[ID])
        println("enter,$newCar")
        if (jsonData == null){
            //jsonData = ParkhausServletPostDto(HashMap<String, String>())
        }

    }

    open fun leaveCar(ID: String, params: Array<String>){
        val placeNumber = params[1].substring(params[2].indexOf(":")+2).toInt()
        val dataList = ArrayList<ParkhausServletPostDto>()
        currentCarDict[ID]?.remove(placeNumber)
        for ((k,v) in carDict){
            print(k)
            println(v)
        }
        //val dataT = Json.decodeFromString<ParkhausServletPostDto>(params[1])

    }

    open fun leaveCar(ID: String, params: ParkhausServletPostDto){
        val placeNumber = params.nr
        val dataList = ArrayList<ParkhausServletPostDto>()
        currentCarDict[ID]?.remove(placeNumber)
        for ((k,v) in carDict){
            print(k)
            println(v)
        }
        //val dataT = Json.decodeFromString<ParkhausServletPostDto>(params[1])

    }


    // this is the constructor for own functionality (single called)
    @PostConstruct
    open fun sessionInit() {
        print("Hello from Singleton Service")


    }


}