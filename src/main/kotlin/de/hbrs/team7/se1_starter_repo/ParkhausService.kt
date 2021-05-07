package de.hbrs.team7.se1_starter_repo

// import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Singleton

// @ApplicationScoped
@Singleton
class ParkhausService : ParkhausServiceIF {

    override var initNumber: Int = 0

    public override fun iterInit() = initNumber++;

    @Throws(Exception::class)
    fun createHelloMessage(name: String): String {
        return "Hello $name + $initNumber"
    }

}