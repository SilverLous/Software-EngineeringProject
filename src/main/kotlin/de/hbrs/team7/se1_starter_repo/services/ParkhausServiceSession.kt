package de.hbrs.team7.se1_starter_repo.services

import de.hbrs.team7.se1_starter_repo.ParkhausServiceSessionIF
import de.hbrs.team7.se1_starter_repo.dto.ParkhausServletPostDto
import de.hbrs.team7.se1_starter_repo.dto.citiesDTO
import de.hbrs.team7.se1_starter_repo.dto.statisticsChartDto
import de.hbrs.team7.se1_starter_repo.entities.Auto
import de.hbrs.team7.se1_starter_repo.entities.Parkhaus
import de.hbrs.team7.se1_starter_repo.entities.ParkhausEbene
import de.hbrs.team7.se1_starter_repo.entities.Ticket
import jakarta.annotation.PostConstruct
import jakarta.enterprise.context.SessionScoped
import jakarta.inject.Inject
import jakarta.inject.Named
import java.io.Serializable
import java.time.Instant
import java.util.*


/*

BIG WARNING DURING LANG FEATURES ALL VALUES MUST BE OPEN!!!

 */

@Named
@SessionScoped
open class ParkhausServiceSession : Serializable, ParkhausServiceSessionIF {

    private lateinit var parkhaus: Parkhaus

    private val parkhausEbenen: MutableList<ParkhausEbene> = mutableListOf()

    // must be this way to ensure it is loaded and the injector has time to do its job
    @Inject private lateinit var parkhausServiceGlobal: ParkhausServiceGlobal

    @Inject private lateinit var DatabaseGlobal: DatabaseServiceGlobal


    // scrapped idea because it is not testable with junit
    //@Inject private lateinit var servletContext: ServletContext


    open lateinit var city: citiesDTO
        protected set

    // this is the constructor for own functionality (called per new browser connection)
    @PostConstruct
    // open fun sessionInit(@Observes @Initialized(SessionScoped::class) pServletContext: ServletContext) {
    override fun sessionInit() {
        city = parkhausServiceGlobal.cities.random()

        val ph = Parkhaus(city.name)
        parkhaus = DatabaseGlobal.persistEntity(ph)

        if(parkhausServiceGlobal.levelSet.isNotEmpty()) {
            parkhausEbenen.addAll(parkhausServiceGlobal.levelSet.map { e -> initEbene(e) })
        }

        print("Hello from $city (ParkhausEbeneID: ${parkhaus.id}) Service new User ")

    }

    override fun addCar(ParkhausEbeneID: String, params: ParkhausServletPostDto) {
        val auto = Auto()
        auto.Kennzeichen = params.license

        val ticket = Ticket()
        ticket.Ausstellungsdatum = Date.from(Instant.now())
        ticket.Preisklasse = 2
        ticket.Auto = auto

        val saved = this.DatabaseGlobal.persistEntity(ticket)

        print(this.DatabaseGlobal.queryAllEntities(Auto::class.java))
        val test = this.DatabaseGlobal.nativeSQLQuerySample(saved.Ticketnummer)
        print(test.first().Ausstellungsdatum)
    }

    override  fun initEbene(name: String): ParkhausEbene {
        val pe = ParkhausEbene(name, this.parkhaus)
        val pePersist = DatabaseGlobal.persistEntity(pe)

        parkhaus = DatabaseGlobal.findByID(parkhaus.id, Parkhaus::class.java) !!

        return DatabaseGlobal.persistEntity(pe)
    }

    override fun generateTicket(ParkhausEbeneID: String, params: ParkhausServletPostDto): Ticket {
        //TODO add zieheTicket functionality
        throw NotImplementedError()
    }

    override fun payForTicket(ParkhausEbeneID: String, autoHash: String, timeCheckOut: Long): Int {
        //autoHash soll nur für eine unique Sache stehen die das Auto identifiziert, andere Keys wären auch ok.
        //TODO add bezahleTicket functionality
        throw NotImplementedError()
    }

    override fun sumOverCars(ParkhausEbeneID: String): Int {
        //TODO("Not yet implemented")
        throw NotImplementedError()
    }

    override fun averageOverCars(ParkhausEbeneID: String): Int {
        //TODO("Not yet implemented")
        throw NotImplementedError()
    }

    override fun statsToChart(ParkhausEbeneID: String): statisticsChartDto {
        //TODO("Not yet implemented")
        throw NotImplementedError()
    }


}

