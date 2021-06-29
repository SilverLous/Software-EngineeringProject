package de.hbrs.team7.se1_starter_repo.services

import de.hbrs.team7.se1_starter_repo.ParkhausServiceSessionIF
import de.hbrs.team7.se1_starter_repo.dto.*
import de.hbrs.team7.se1_starter_repo.entities.*
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

    open val undoList: ArrayList<Auto> = ArrayList<Auto>()
    open val redoList: ArrayList<Auto> = ArrayList<Auto>()
    open val deletedDatum: ArrayList<Date> = ArrayList<Date>()
    open val deletedReferenceToLevelName: ArrayList<String> = ArrayList<String>()

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

        val pa = databaseGlobal.findeParkhausMitEbeneUeberId(id)
        if (pa != null) {
            this.parkhaus = pa
            this.parkhausEbenen = pa.ebenen
            }
    }

    @Deprecated("use config")
    override fun initEbene(name: String): ParkhausEbene {
        val pe = ParkhausEbene(name, this.parkhaus)
        val pePersist = databaseGlobal.persistEntity(pe)

        parkhaus = databaseGlobal.findeUeberID(parkhaus.id, Parkhaus::class.java) !!

        this.parkhausServiceGlobal.levelSet.add(name)
        parkhausEbenen.add(pe)
        return databaseGlobal.persistEntity(pe)
    }

    open fun initEbene(config: ParkhausEbeneConfigDTO): ParkhausEbene {
        val parkhaus = databaseGlobal.findeUeberID(parkhaus.id, Parkhaus::class.java) !!

        config.parkhaus = parkhaus
        val pe = ParkhausEbene.ausEbenenConfig(config)

        val fahrzeugTypen = config.FahrzeugPreise.map { eintrag ->  FahrzeugTyp.ausHashMapEintrag(eintrag, pe) }.toMutableList()
        pe.fahrzeugTypen = fahrzeugTypen

        parkhaus.parkhausEbeneHinzufügen(pe)

        this.parkhaus = databaseGlobal.mergeUpdatedEntity(parkhaus)

        val peGespeichert = this.parkhaus.ebenen.find { pE -> pE.name == config.ebenenNamen } !!
        this.parkhausServiceGlobal.ebenenSet.add(config)
        parkhausEbenen.add(peGespeichert)
        return peGespeichert
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
        return ticketBezahlen(parkhausEbeneID, ticket, timeCheckOut)
    }

    override fun ticketBezahlen(ParkhausEbeneId: Long, ticket: Ticket, timeCheckOut: Date): Long {

        ticket.Ausfahrdatum = timeCheckOut


        ticket.price = errechneTicketPreis(ParkhausEbeneId, ticket, ticket.Auto !!)

        this.databaseGlobal.mergeUpdatedEntity(ticket)
        ticket.Auto?.ImParkhaus = false
        this.databaseGlobal.mergeUpdatedEntity(ticket.Auto)

        parkhausServiceGlobal.statisticUpdateSubj
            .onNext(listOf(ManagerStatistikUpdateDTO.TAGESEINNAHMEN, ManagerStatistikUpdateDTO.WOCHENEINNAHMEN))
        if (ticket.Auto != null){
            undoList.add(ticket.Auto!!)
        }

        return ticket.price.toLong()/100
    }

    open fun errechneTicketPreis(parkhausEbeneID: Long, ticket: Ticket, auto: Auto  ): Int {
        val ebene = databaseGlobal.findeUeberID(parkhausEbeneID, ParkhausEbene::class.java)
        val duration = (ticket.Ausfahrdatum?.time?.minus(ticket.Ausstellungsdatum.time))

        val fahrzeugMultiplikator: Double = ebene!!.fahrzeugTypen.find {
                entry -> entry.typ!!.lowercase() == ticket.Auto!!.Typ.lowercase() }?.multiplikator ?: 1.0
        return ((duration?:0) + 0.5 * this.parkhaus.preisklasse!! * fahrzeugMultiplikator).toInt()
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

    override fun getTageseinnahmen(ParkhausEbeneId: Long): einnahmenBarDTO {
        val sumOverDay = databaseGlobal.errechneTagesEinnahmen(ParkhausEbeneId)
            return einnahmenBarDTO(data = listOf(BarData("bar", listOf("Tages Einnahmen"),
                listOf((sumOverDay?.toDouble())?.div(100) ?: 0.0))))

    }

    override fun getWocheneinnahmen(ParkhausEbeneName: String): einnahmenBarDTO {
        val parkhausEbeneID = getIdUeberName(ParkhausEbeneName)
        return getWocheneinnahmen(parkhausEbeneID)
    }

    override fun getWocheneinnahmen(ParkhausEbeneId: Long): einnahmenBarDTO {
        val sumOverDay = databaseGlobal.errechneWochenEinnahmen(ParkhausEbeneId)
        return einnahmenBarDTO(data = listOf(BarData("bar", listOf("Wochen Einnahmen"),
            listOf((sumOverDay?.toDouble())?.div(100) ?: 0.0))))

    }

    override fun getPrintStringAutos(ParkhausEbeneName: String): String {
        val parkhausEbeneID = getIdUeberName(ParkhausEbeneName)
        return getPrintStringAutos(parkhausEbeneID)
    }

    override fun getPrintStringAutos(ParkhausEbeneId: Long): String {
        val autosInParkhausEbene =  databaseGlobal.autosInParkEbeneHistoric(ParkhausEbeneId)
        var printString = ""
        for(e: Auto in autosInParkhausEbene ){

            val preis = errechneTicketPreis(ParkhausEbeneId, e.Ticket !!, e)

            printString += ("${e.Autonummer}/${e.Ticket?.Ausstellungsdatum?.time}" +
                    "/${ if (e.ImParkhaus) 0 else e.getParkdauer()}/${e.getParkdauer()/100}/Ticket${e.Ticket?.Ticketnummer}/${e.Farbe}/${e.Platznummer}" +
                    "/${e.Typ}/${e.Kategorie}/${e.Kennzeichen},")
        }
        return printString.dropLast(1)
    }

    open fun zeigeHTMLParkhausListe(): String {
        val parkhaeuser = databaseGlobal.queryAllEntities(Parkhaus::class.java)?.filter { pa -> pa.ebenen.size != 0 && pa.stadtname != parkhaus.stadtname}
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
                toUndo.ImParkhaus = false
                val parkhausEbene = databaseGlobal.findeParkhausEbeneByTicket(toUndo.Ticket?.Ticketnummer!!)
                deletedDatum.add(toUndo.Ticket?.Ausstellungsdatum!!)
                toUndo.Ticket?.Auto = null
                databaseGlobal.mergeUpdatedEntity(toUndo.Ticket)
                databaseGlobal.deleteByID(toUndo.Autonummer,Auto::class.java)
                databaseGlobal.deleteByID(toUndo?.Ticket?.Ticketnummer!!,Ticket::class.java)
                toUndo.Ticket?.parkhausEbenen?.dropLast(1)
                parkhausEbene?.tickets?.dropLast(1)
                databaseGlobal.mergeUpdatedEntity(parkhausEbene)
            }
            else{
                toUndo.ImParkhaus = true
                toUndo.Ticket?.Ausfahrdatum?.let { deletedDatum.add(it) }
                deletedReferenceToLevelName.add(databaseGlobal.findeParkhausEbeneByTicket(toUndo.Ticket?.Ticketnummer!!)?.name!!)
                toUndo.Ticket?.Ausfahrdatum = null
                toUndo.Ticket?.price = 0
                databaseGlobal.mergeUpdatedEntity(toUndo.Ticket)
                databaseGlobal.mergeUpdatedEntity(toUndo)
            }
            undoList.dropLast(1)

        }
    }

    override fun redo() {
        val toRedo = redoList.last()
        undoList.add(toRedo)
        if (toRedo.ImParkhaus){
            toRedo.Ticket?.Auto = toRedo
            ticketBezahlen(deletedReferenceToLevelName.last(), toRedo.Ticket!!, deletedDatum.last())
            redoList.dropLast(1)
        }
        else{
            toRedo.ImParkhaus = true
            val pSPdto = ParkhausServletPostDto(2,deletedDatum.last().time,0,0.0,toRedo.Hash!!,toRedo.Farbe!!,toRedo.Platznummer!!,toRedo.Kategorie,toRedo.Typ,toRedo.Kennzeichen!!)
            val auto = erstelleTicket(deletedReferenceToLevelName.last(),pSPdto).Auto
            redoList.dropLast(1)
            if (redoList.last().Autonummer==toRedo.Autonummer){
                redoList.dropLast(1)
                redoList.add(auto!!)
            }

        }
        redoList.dropLast(1)
        deletedReferenceToLevelName.dropLast(1)
        deletedDatum.dropLast(1)
    }

    open fun wechsleEbeneMaxParkplätze(name: String, aktuell: Int, neu: Int) {
        val parkhausEbeneArrayPos = this.parkhausEbenen.indexOfFirst { pe -> pe.name == name }


        this.parkhausEbenen[parkhausEbeneArrayPos].maxPlätze = neu

        this.parkhausEbenen[parkhausEbeneArrayPos] =
            databaseGlobal.mergeUpdatedEntity(this.parkhausEbenen[parkhausEbeneArrayPos])

    }

    open fun wechsleEbeneÖffnungszeit(name: String, aktuell: Int, neu: Int) {
        val parkhausEbeneArrayPos = this.parkhausEbenen.indexOfFirst { pe -> pe.name == name }


        this.parkhausEbenen[parkhausEbeneArrayPos].öffnungszeit = neu

        this.parkhausEbenen[parkhausEbeneArrayPos] =
            databaseGlobal.mergeUpdatedEntity(this.parkhausEbenen[parkhausEbeneArrayPos])
    }

    open fun wechsleEbeneLadenschluss(name: String, aktuell: Int, neu: Int) {
        val parkhausEbeneArrayPos = this.parkhausEbenen.indexOfFirst { pe -> pe.name == name }


        this.parkhausEbenen[parkhausEbeneArrayPos].ladenschluss = neu

        this.parkhausEbenen[parkhausEbeneArrayPos] =
            databaseGlobal.mergeUpdatedEntity(this.parkhausEbenen[parkhausEbeneArrayPos])

    }


}

