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


    open var jsonData: ParkhausServletPostDto? = null
        protected set

    open fun addCar(ID: String, params: Array<String>) {
        globalCars++

        val newCar: CarIF = Car(params)
        val data = Json.decodeFromString<ParkhausServletPostDto>(params[1])
        println("enter,$newCar")
        carDict[ID]?.plusAssign(newCar)
        if (jsonData == null){
            //jsonData = ParkhausServletPostDto(HashMap<String, String>())
        }

    }

    open fun leaveCar(ID: String){
        val dataList = ArrayList<ParkhausServletPostDto>()
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