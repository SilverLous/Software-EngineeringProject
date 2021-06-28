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
import jakarta.persistence.Tuple
import jakarta.persistence.TupleElement
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

    open val undoList: ArrayList<Auto> = ArrayList<Auto>()
    open val redoList: ArrayList<Auto> = ArrayList<Auto>()
    open val deletedDatum: ArrayList<Date> = ArrayList<Date>()

    private var parkhausEbenen: MutableList<ParkhausEbene> = mutableListOf()

    // must be this way to ensure it is loaded and the injector has time to do its job
    @Inject private lateinit var parkhausServiceGlobal: ParkhausServiceGlobal

    @Inject private lateinit var databaseGlobal: DatabaseServiceGlobal

    @Inject private lateinit var logGlobal: LoggerServiceGlobal


    // scrapped idea because it is not testable with junit
    //@Inject private lateinit var servletContext: ServletContext

    @Deprecated("Nutze parkhaus.stadtname etc")
    open lateinit var city: citiesDTO
        protected set


    // this is the constructor for own functionality (called per new browser connection)
    @PostConstruct
    // open fun sessionInit(@Observes @Initialized(SessionScoped::class) pServletContext: ServletContext) {
    override fun sessionInit() {

        erstelleInitStadt()
        print("Hello from ${parkhaus.stadtname} (ParkhausEbeneID: ${parkhaus.id}) Service new User ")

    }

    open fun erstelleInitStadt() {
        val city = parkhausServiceGlobal.cities.random()

        val pa = databaseGlobal.getParkhausüberStandtnamen(city.name)

        if (pa != null) {
            ladeParkhausStadt(pa.id)
        } else {
            val ph = Parkhaus.fromCitiesDTO(city)
            parkhaus = databaseGlobal.persistEntity(ph)

            if(parkhausServiceGlobal.ebenenSet.isNotEmpty()) {
                parkhausEbenen.addAll(parkhausServiceGlobal.ebenenSet.map { e -> initEbene(e) })
            }
        }


    }

    open fun ladeParkhausStadt(id: Long) {

        val pa = databaseGlobal.findeParkhausMitEbeneUeberId(id);
        if (pa != null) {
            this.parkhaus = pa
            this.parkhausEbenen = pa.ebenen
            }
    }

    @Deprecated("use config")
    override fun initEbene(name: String): ParkhausEbene {
        val pe = ParkhausEbene(name, this.parkhaus, )
        val pePersist = databaseGlobal.persistEntity(pe)

        parkhaus = databaseGlobal.findeUeberID(parkhaus.id, Parkhaus::class.java) !!

        this.parkhausServiceGlobal.levelSet.add(name)
        parkhausEbenen.add(pe)
        return databaseGlobal.persistEntity(pe)
    }

    open fun initEbene(config: ParkhausEbeneConfigDTO): ParkhausEbene {
        config.parkhaus = this.parkhaus
        val pe = ParkhausEbene.ausEbenenConfig(config)
        val pePersist = databaseGlobal.persistEntity(pe)

        parkhaus = databaseGlobal.findeUeberID(parkhaus.id, Parkhaus::class.java) !!

        this.parkhausServiceGlobal.ebenenSet.add(config)
        parkhausEbenen.add(pe)
        return databaseGlobal.persistEntity(pe)
    }

    override fun erstelleTicket(ParkhausEbeneName: String, params: ParkhausServletPostDto): Ticket {
        val parkhausEbeneID = getIdUeberName(ParkhausEbeneName)
        val belegtePlätze = getAutosInParkEbene(parkhausEbeneID, true)
            .map { auto -> auto.Platznummer !! }.toSet()

        val gesamtPlätze = (1..this.parkhausEbenen.find { pe -> pe.id == parkhausEbeneID }!!.maxPlätze).toSet()

        val verfügbarePlätze = gesamtPlätze.subtract(belegtePlätze)

        if(params.space !in verfügbarePlätze) {
            params.space = verfügbarePlätze.random()
        }

        val auto = autoHinzufügen(parkhausEbeneID, params)

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

    override fun autoHinzufügen(ParkhausEbeneID: Long, params: ParkhausServletPostDto):Auto {
        val auto = Auto(params.hash,params.color,params.space,params.license, params.vehicleType, params.clientCategory)
        this.databaseGlobal.persistEntity(auto)
        logGlobal.schreibeInfo("Auto wurde hinzugefügt ${auto.Autonummer}")
        undoList.add(auto)
        return auto
    }

    override fun ticketBezahlen(ParkhausEbeneName: String, ticket: Ticket, timeCheckOut: Date): Long {
        val parkhausEbeneID = getIdUeberName(ParkhausEbeneName)
        ticket.Ausfahrdatum = timeCheckOut
        val duration = ticket.Ausfahrdatum!!.time - ticket.Ausstellungsdatum.time
        ticket.price = (duration).toInt()
        this.databaseGlobal.mergeUpdatedEntity(ticket)
        ticket.Auto?.ImParkhaus = false
        this.databaseGlobal.mergeUpdatedEntity(ticket.Auto)

        parkhausServiceGlobal.statisticUpdateSubj
            .onNext(listOf(ManagerStatistikUpdateDTO.TAGESEINNAHMEN, ManagerStatistikUpdateDTO.WOCHENEINNAHMEN,))
        if (ticket.Auto != null){
            undoList.add(ticket.Auto!!)
        }

        return duration/100
    }

    override fun getSummeTicketpreiseUeberAutos(ParkhausEbeneName: String): Int {
        val parkhausEbeneID = getIdUeberName(ParkhausEbeneName)
        return databaseGlobal.getSummeDerTicketpreise(parkhausEbeneID) !!
    }

    override fun getEbeneUeberId(ParkhausEbeneID: Long):ParkhausEbene{
        return parkhausEbenen.first{e-> e.id == ParkhausEbeneID }
    }
    override fun getAlleUser(ParkhausEbeneName: String):Int{
        val parkhausEbeneID = getIdUeberName(ParkhausEbeneName)
        return databaseGlobal.getAnzahlAllerUser(parkhausEbeneID) !!
    }
    override fun getAktuelleUser(ParkhausEbeneName: String): Int{
        val parkhausEbeneID = getIdUeberName(ParkhausEbeneName)
        return databaseGlobal.getautosInParkEbene(parkhausEbeneID).size
    }

    override fun getDurchschnittUeberAutos(ParkhausEbeneName: String): Int {
        val divisor = (getAlleUser(ParkhausEbeneName) - getAktuelleUser(ParkhausEbeneName))
        return if (divisor == 0) {
            0;
        } else
            (getSummeTicketpreiseUeberAutos(ParkhausEbeneName) / divisor)
    }

    override fun findeTicketUeberParkplatz(ParkhausEbeneName: String, placeNumber: Int): Ticket? {
        val parkhausEbeneID = getIdUeberName(ParkhausEbeneName)
        return databaseGlobal.findeTicketUeberParkplatz(parkhausEbeneID, placeNumber)
    }
    override fun getIdUeberName(ParkhausEbeneName: String):Long{
        return parkhausEbenen.first{e-> e.name == ParkhausEbeneName }.id
    }

    override fun getParkhausEbenen():List<ParkhausEbene>{
        return parkhausEbenen
    }

    override fun getAutosInParkEbene(ParkhausEbeneName: String, ImParkhaus : Boolean): List<Auto>{
        val parkhausEbeneID = getIdUeberName(ParkhausEbeneName)
        return getAutosInParkEbene(parkhausEbeneID, ImParkhaus)
    }

    override fun getAutosInParkEbene(ParkhausEbeneID: Long, ImParkhaus : Boolean): List<Auto>{
        return databaseGlobal.getautosInParkEbene(ParkhausEbeneID, ImParkhaus)
    }

    override fun erstelleStatistikenUeberFahrzeuge(ParkhausEbeneName: String): statisticsChartDto {
        val parkhausEbeneID = getIdUeberName(ParkhausEbeneName)
        val allCarsThatLeft = databaseGlobal.getautosInParkEbene(parkhausEbeneID, false)
        val allVehicleTypes = allCarsThatLeft.map { a->a.Typ }.toSet().toList()
        val sumPricesOverVehicleTypes = allVehicleTypes.map { a->allCarsThatLeft.filter { a2-> a2.Typ==a }.fold(0.0) { acc, i -> acc + (i.Ticket!!.price)/100 } }
        return statisticsChartDto(data = listOf(carData("bar", allVehicleTypes, sumPricesOverVehicleTypes)))
    }

    override fun getTageseinnahmen(ParkhausEbeneName: String): einnahmenBarDTO {
        val parkhausEbeneID = getIdUeberName(ParkhausEbeneName)
        return getTageseinnahmen(parkhausEbeneID)
    }

    override fun getTageseinnahmen(ParkhausEbeneName: Long): einnahmenBarDTO {
        val sumOverDay = databaseGlobal.errechneTagesEinnahmen(ParkhausEbeneName)
            return einnahmenBarDTO(data = listOf(BarData("bar", listOf("Tages Einnahmen"),
                listOf((sumOverDay?.toDouble())?.div(100) ?: 0.0))))

    }

    override fun getWocheneinnahmen(ParkhausEbeneName: String): einnahmenBarDTO {
        val parkhausEbeneID = getIdUeberName(ParkhausEbeneName)
        return getWocheneinnahmen(parkhausEbeneID)
    }

    override fun getWocheneinnahmen(ParkhausEbeneName: Long): einnahmenBarDTO {
        val sumOverDay = databaseGlobal.errechneWochenEinnahmen(ParkhausEbeneName)
        return einnahmenBarDTO(data = listOf(BarData("bar", listOf("Wochen Einnahmen"),
            listOf((sumOverDay?.toDouble())?.div(100) ?: 0.0))))

    }

    override fun getPrintStringAutos(ParkhausEbeneName: String): String {
        val parkhausEbeneID = getIdUeberName(ParkhausEbeneName)
        return getPrintStringAutos(parkhausEbeneID)
    }

    override fun getPrintStringAutos(ParkhausEbeneName: Long): String {
        val autosInParkhausEbene =  databaseGlobal.autosInParkEbeneHistoric(ParkhausEbeneName)
        var printString = ""
        for(e: Auto in autosInParkhausEbene ){
            printString += ("${e.Autonummer}/${e.Ticket?.Ausstellungsdatum?.time}" +
                    "/${ if (e.ImParkhaus) 0 else e.getParkdauer()}/${e.getParkdauer()/100}/Ticket${e.Ticket?.Ticketnummer}/${e.Farbe}/${e.Platznummer}" +
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

    override fun erstellePreiseFürBundesländer(): statisticsChartDto? {
        val carMap = databaseGlobal.getTicketpreiseProBundesland()
        if (carMap != null) {
            return statisticsChartDto(data = listOf(carData("bar", carMap.keys.toList(), carMap.values.map{a -> a.toDouble()/100})))
        }
        return null


    }

    override fun undo() {
        if (undoList.size>0){
            val toUndo = undoList.last()
            redoList.add(toUndo)
            if (toUndo.ImParkhaus){
                deletedDatum.add(toUndo.Ticket?.Ausstellungsdatum!!)
                toUndo.Ticket?.Auto = null
                databaseGlobal.mergeUpdatedEntity(toUndo.Ticket)
                databaseGlobal.deleteByID(toUndo.Autonummer,Auto::class.java)
                toUndo.Ticket?.Ticketnummer?.let { databaseGlobal.deleteByID(it,Ticket::class.java) }
                toUndo.ImParkhaus = false
                toUndo.Ticket?.parkhausEbenen?.dropLast(1)
            }
            else{
                toUndo.Ticket?.Ausfahrdatum?.let { deletedDatum.add(it) }
                toUndo.Ticket?.Ausfahrdatum = null
                toUndo.Ticket?.price = 0
                databaseGlobal.mergeUpdatedEntity(toUndo.Ticket)
                toUndo.ImParkhaus = true
                databaseGlobal.mergeUpdatedEntity(toUndo)
            }
            undoList.dropLast(1)

        }
    }

    override fun redo() {
        val toRedo = redoList.last()
        undoList.add(toRedo)
        if (toRedo.ImParkhaus){
            toRedo.Ticket?.parkhausEbenen?.last()?.name?.let { ticketBezahlen(it, toRedo.Ticket!!,deletedDatum.last(),) }
        }
        else{
            val pSPdto = ParkhausServletPostDto(-1,deletedDatum.last().time,0,0.0,toRedo.Hash!!,toRedo.Farbe!!,toRedo.Platznummer!!,toRedo.Kategorie,toRedo.Typ,toRedo.Kennzeichen!!)
            autoHinzufügen(toRedo.Ticket?.parkhausEbenen?.last()?.id!!,pSPdto)
            databaseGlobal.persistEntity(toRedo.Ticket)
        }
    }

    open fun wechsleEbeneMaxParkplätze(name: String, aktuell: Int, neu: Int) {


    }

    open fun wechsleEbeneÖffnungszeit(name: String, aktuell: Int, neu: Int) {

    }

    open fun wechsleEbeneLadenschluss(name: String, aktuell: Int, neu: Int) {

    }


}

