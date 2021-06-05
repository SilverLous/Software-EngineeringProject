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

    override fun addCar(ParkhausEbeneID: String, params: ParkhausServletPostDto):Auto {
        val auto = Auto(params.hash,params.color,params.space,params.license)
        this.DatabaseGlobal.persistEntity(auto)
        // print(this.DatabaseGlobal.queryAllEntities(Auto::class.java))
        return auto
    }

    override  fun initEbene(name: String): ParkhausEbene {
        val pe = ParkhausEbene(name, this.parkhaus)
        val pePersist = DatabaseGlobal.persistEntity(pe)

        parkhaus = DatabaseGlobal.findByID(parkhaus.id, Parkhaus::class.java) !!

        this.parkhausServiceGlobal.levelSet.add(name)
        parkhausEbenen.add(pe)
        return DatabaseGlobal.persistEntity(pe)
    }

    override fun generateTicket(ParkhausEbeneID: String, params: ParkhausServletPostDto): Ticket {
        val auto = addCar(ParkhausEbeneID, params)

        val ticket = Ticket()
        // ticket.Ausstellungsdatum = Date.from(Instant.now())
        ticket.Preisklasse = 2
        ticket.Auto = auto
        auto.Ticket = ticket

        val saved = this.DatabaseGlobal.persistEntity(ticket)
        val test = this.DatabaseGlobal.nativeSQLQuerySample(saved.Ticketnummer)
        print(test.first().Ausstellungsdatum)

        //TODO add zieheTicket functionality
        // throw NotImplementedError()
        return ticket
    }

    override fun payForTicket(ParkhausEbeneID: String, ticket: Ticket, timeCheckOut: Date): Long {
        ticket.Ausfahrdatum = timeCheckOut
        val duration = ticket.Ausstellungsdatum.time - ticket.Ausfahrdatum!!.time
        ticket.price = (duration/100).toInt()
        this.DatabaseGlobal.mergeUpdatedEntity(ticket)
        ticket.Auto?.ImParkhaus = false
        this.DatabaseGlobal.mergeUpdatedEntity(ticket.Auto)
        return duration/100
    }

    override fun sumOverCars(ParkhausEbeneID: String): Int {
        //TODO("Not yet implemented") SQL Abfrage ImParkhaus False, Sum over Price
        return DatabaseGlobal.getSumAndCountOfLevel(ParkhausEbeneID).first
        // return price in Cent
    }

    override fun averageOverCars(ParkhausEbeneID: String): Int {
        //TODO("Not yet implemented") SQL Abfrage ImParkhaus False, Sum over Price, durch Anzahl wo ImParkhaus false ist
        val result = DatabaseGlobal.getSumAndCountOfLevel(ParkhausEbeneID)
        return result.first/result.second
        // return round(price in Cent)
    }

    override fun statsToChart(ParkhausEbeneID: String): statisticsChartDto {
        //TODO("Not yet implemented")
        throw NotImplementedError()
    }
    override fun getLevelByName(ParkhausEbeneID: String):ParkhausEbene{
        // print(parkhausEbenen)
        return parkhausEbenen.filter{e->e.name.equals(ParkhausEbeneID)}.first { true }
    }
    override fun getTotalUsers(ParkhausEbeneID: String):Int{
        // TODO("Not yet implemented") SQL Abfrage die die Anzahl der totalen Tickets ausgibt
        return DatabaseGlobal.getSumAndCountOfLevel(ParkhausEbeneID).second
        // return Anzahl als Int
    }

    override fun findTicketByPlace(ParkhausEbeneID: String, placeNumber: Int): Ticket {
        // TODO("Not yet implemented") SQL Abfrage die das Ticket zu der zugehoerigen Platznummer findet
        return DatabaseGlobal.findTicketByPlace(ParkhausEbeneID, placeNumber)
        // return Ticket
    }

    open fun getParkhausEbenen():List<ParkhausEbene>{
        return parkhausEbenen
    }


}

