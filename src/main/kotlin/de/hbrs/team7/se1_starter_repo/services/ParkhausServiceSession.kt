package de.hbrs.team7.se1_starter_repo.services

import de.hbrs.team7.se1_starter_repo.ParkhausServiceSessionIF
import de.hbrs.team7.se1_starter_repo.dto.*
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

    open lateinit var parkhaus: Parkhaus
        protected set

    private var parkhausEbenen: MutableList<ParkhausEbene> = mutableListOf()

    // must be this way to ensure it is loaded and the injector has time to do its job
    @Inject private lateinit var parkhausServiceGlobal: ParkhausServiceGlobal

    @Inject private lateinit var databaseGlobal: DatabaseServiceGlobal


    // scrapped idea because it is not testable with junit
    //@Inject private lateinit var servletContext: ServletContext

    @Deprecated("Nutze parkhaus.stadtname etc")
    open lateinit var city: citiesDTO
        protected set


    // this is the constructor for own functionality (called per new browser connection)
    @PostConstruct
    // open fun sessionInit(@Observes @Initialized(SessionScoped::class) pServletContext: ServletContext) {
    override fun sessionInit() {

        createInitCity()
        print("Hello from ${parkhaus.stadtname} (ParkhausEbeneID: ${parkhaus.id}) Service new User ")

    }

    open fun createInitCity() {
        val city = parkhausServiceGlobal.cities.random()

        val pa = databaseGlobal.getParkhausByCityName(city.name)

        if (pa != null) {
            loadParkhausCity(pa.id)
        } else {
            val ph = Parkhaus.fromCitiesDTO(city)
            parkhaus = databaseGlobal.persistEntity(ph)

            if(parkhausServiceGlobal.levelSet.isNotEmpty()) {
                parkhausEbenen.addAll(parkhausServiceGlobal.levelSet.map { e -> initEbene(e) })
            }
        }


    }

    open fun loadParkhausCity(id: Long) {

        val pa = databaseGlobal.findeParkhausMitEbeneUeberId(id);
        if (pa != null) {
            this.parkhaus = pa
            this.parkhausEbenen = pa.ebenen
            }
    }

    override fun initEbene(name: String): ParkhausEbene {
        val pe = ParkhausEbene(name, this.parkhaus)
        val pePersist = databaseGlobal.persistEntity(pe)

        parkhaus = databaseGlobal.findByID(parkhaus.id, Parkhaus::class.java) !!

        this.parkhausServiceGlobal.levelSet.add(name)
        parkhausEbenen.add(pe)
        return databaseGlobal.persistEntity(pe)
    }

    override fun generateTicket(ParkhausEbeneName: String, params: ParkhausServletPostDto): Ticket {
        val parkhausEbeneID = getIdByName(ParkhausEbeneName)
        val auto = addCar(parkhausEbeneID, params)

        val ticket = Ticket()
        // ticket.Ausstellungsdatum = Date.from(Instant.now())
        ticket.Preisklasse = 2
        ticket.Auto = auto
        auto.Ticket = ticket
        val parkhausEbeneToAdd = getParkhausEbenen().first { e -> e.id == parkhausEbeneID }
        parkhausEbeneToAdd.tickets.add(ticket)
        // this.DatabaseGlobal.mergeUpdatedEntity(parkhausEbeneToAdd)
        val saved = this.databaseGlobal.persistEntity(ticket)
        val test = this.databaseGlobal.nativeSQLQuerySample(saved.Ticketnummer)
        print(test.first().Ausstellungsdatum)
        return ticket
    }

    override fun addCar(ParkhausEbeneID: Long, params: ParkhausServletPostDto):Auto {
        val auto = Auto(params.hash,params.color,params.space,params.license, params.vehicleType, params.clientCategory)
        this.databaseGlobal.persistEntity(auto)
        return auto
    }

    override fun payForTicket(ParkhausEbeneName: String, ticket: Ticket, timeCheckOut: Date): Long {
        val parkhausEbeneID = getIdByName(ParkhausEbeneName)
        ticket.Ausfahrdatum = timeCheckOut
        val duration = ticket.Ausfahrdatum!!.time - ticket.Ausstellungsdatum.time
        ticket.price = (duration).toInt()
        this.databaseGlobal.mergeUpdatedEntity(ticket)
        ticket.Auto?.ImParkhaus = false
        this.databaseGlobal.mergeUpdatedEntity(ticket.Auto)

        parkhausServiceGlobal.StatisticUpdateSubj
            .onNext(listOf(ManagerStatistikUpdateDTO.TAGESEINNAHMEN, ManagerStatistikUpdateDTO.WOCHENEINNAHMEN,))

        return duration/100
    }

    override fun sumOverCars(ParkhausEbeneName: String): Int {
        val parkhausEbeneID = getIdByName(ParkhausEbeneName)
        return databaseGlobal.getSumOfTicketPrices(parkhausEbeneID) !!
    }

    override fun getLevelById(ParkhausEbeneID: Long):ParkhausEbene{
        return parkhausEbenen.first{e-> e.id == ParkhausEbeneID }
    }
    override fun getTotalUsers(ParkhausEbeneName: String):Int{
        val parkhausEbeneID = getIdByName(ParkhausEbeneName)
        return databaseGlobal.getTotalUsersCount(parkhausEbeneID) !!
    }
    override fun getCurrenUsers(ParkhausEbeneName: String): Int{
        val parkhausEbeneID = getIdByName(ParkhausEbeneName)
        return databaseGlobal.autosInParkEbene(parkhausEbeneID).size
    }

    override fun averageOverCars(ParkhausEbeneName: String): Int {
        val divisor = (getTotalUsers(ParkhausEbeneName) - getCurrenUsers(ParkhausEbeneName))
        return if (divisor == 0) {
            0;
        } else
            (sumOverCars(ParkhausEbeneName) / divisor)
    }

    override fun findTicketByPlace(ParkhausEbeneName: String, placeNumber: Int): Ticket? {
        val parkhausEbeneID = getIdByName(ParkhausEbeneName)
        return databaseGlobal.findTicketByPlace(parkhausEbeneID, placeNumber)
    }
    override fun getIdByName(ParkhausEbeneName: String):Long{
        return parkhausEbenen.first{e-> e.name == ParkhausEbeneName }.id
    }

    override fun getParkhausEbenen():List<ParkhausEbene>{
        return parkhausEbenen
    }

    override fun autosInParkEbene(ParkhausEbeneName: String, ImParkhaus : Boolean): List<Auto>{
        val parkhausEbeneID = getIdByName(ParkhausEbeneName)
        return autosInParkEbene(parkhausEbeneID, ImParkhaus)
    }

    override fun autosInParkEbene(ParkhausEbeneID: Long, ImParkhaus : Boolean): List<Auto>{
        return databaseGlobal.autosInParkEbene(ParkhausEbeneID, ImParkhaus)
    }

    override fun generateStatisticsOverVehicle(ParkhausEbeneName: String): statisticsChartDto {
        val parkhausEbeneID = getIdByName(ParkhausEbeneName)
        val allCarsThatLeft = databaseGlobal.autosInParkEbene(parkhausEbeneID, false)
        val allVehicleTypes = allCarsThatLeft.map { a->a.Typ }.toSet().toList()
        val sumPricesOverVehicleTypes = allVehicleTypes.map { a->allCarsThatLeft.filter { a2-> a2.Typ==a }.fold(0.0) { acc, i -> acc + (i.Ticket!!.price)/100 } }
        return statisticsChartDto(data = listOf(carData("bar", allVehicleTypes, sumPricesOverVehicleTypes)))
    }

    override fun showDaysTakings(ParkhausEbeneName: String): einnahmenBarDTO {
        val parkhausEbeneID = getIdByName(ParkhausEbeneName)
        return showDaysTakings(parkhausEbeneID)
    }

    override fun showDaysTakings(ParkhausEbeneName: Long): einnahmenBarDTO {
        val sumOverDay = databaseGlobal.errechneTagesEinnahmen(ParkhausEbeneName)
            return einnahmenBarDTO(data = listOf(BarData("bar", listOf("Tages Einnahmen"),
                listOf((sumOverDay?.toDouble())?.div(100) ?: 0.0))))

    }

    override fun showWeeksTakings(ParkhausEbeneName: String): einnahmenBarDTO {
        val parkhausEbeneID = getIdByName(ParkhausEbeneName)
        return showWeeksTakings(parkhausEbeneID)
    }

    override fun showWeeksTakings(ParkhausEbeneName: Long): einnahmenBarDTO {
        val sumOverDay = databaseGlobal.errechneWochenEinnahmen(ParkhausEbeneName)
        return einnahmenBarDTO(data = listOf(BarData("bar", listOf("Wochen Einnahmen"),
            listOf((sumOverDay?.toDouble())?.div(100) ?: 0.0))))

    }

    override fun getPrintStringCars(ParkhausEbeneName: String): String {
        val parkhausEbeneID = getIdByName(ParkhausEbeneName)
        return getPrintStringCars(parkhausEbeneID)
    }

    override fun getPrintStringCars(ParkhausEbeneName: Long): String {
        val autosInParkhausEbene =  databaseGlobal.autosInParkEbeneHistoric(ParkhausEbeneName)
        var printString = ""
        for(e: Auto in autosInParkhausEbene ){
            printString += ("${e.Autonummer}/${e.Ticket?.Ausstellungsdatum?.time}" +
                    "/${ if (e.ImParkhaus) 0 else e.getDuration()}/${e.getDuration()/100}/Ticket${e.Ticket?.Ticketnummer}/${e.Farbe}/${e.Platznummer}" +
                    "/${e.Typ}/${e.Kategorie}/${e.Kennzeichen},")
        }
        return printString.dropLast(1)
    }

    open fun zeigeHTMLParkhausListe(): String {
        val parkhaeuser = databaseGlobal.queryAllEntities(Parkhaus::class.java);
        val parkhausButtons = parkhaeuser?.map { p -> """<button type="button" onclick="wechsleStadt(this, '${p.id}')" class="btn btn-outline-primary" data-cityid="${p.id}">${p.stadtname}</button>"""
        }
        val buttonVariable = parkhausButtons?.joinToString (separator="") ?: ""
        return buttonVariable
        //"""<button type="button" class="btn btn-outline-primary" data-cityid="${}">Primary</button>"""
    }

    override fun generatePriceByFederalState(): statisticsChartDto? {
        val carMap = databaseGlobal.ticketPriceByBundesland()
        if (carMap != null) {
            return statisticsChartDto(data = listOf(carData("bar", carMap.keys.toList(), carMap.values.map{a -> a.toDouble()/100})))
        }
        return null


    }


}

