package de.hbrs.team7.se1_starter_repo


import jakarta.annotation.PostConstruct
//import jakarta.ejb.Singleton
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Singleton


//@ApplicationScoped
// @Stateful
// @ManagedBean
// @SessionScoped
//@ManagedBean
// @ApplicationScoped
//@Stateless
@Singleton

public class ParkhausService(public var initNumber: Int = 0) { // : ParkhausServiceIF {

    // https://dzone.com/articles/understanding-jakarta-ee-8-cdi-part-2-qualifying-your-beans


    init {
        initNumber = 0
        print(initNumber)
        println("hello from service")
    }


    public fun iterInit() = initNumber++;

    @Throws(Exception::class)
    fun createHelloMessage(name: String): String? {
        return "Hello $name + $initNumber"
    }

}