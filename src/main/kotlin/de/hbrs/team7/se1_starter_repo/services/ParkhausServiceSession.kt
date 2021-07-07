package de.hbrs.team7.se1_starter_repo.services

import de.hbrs.team7.se1_starter_repo.interfaces.ParkhausServiceSessionIF
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

    // this is the constructor for own functionality (called per new browser connection)
    @PostConstruct
    override fun sessionInit() {

        erstelleInitStadt()
        logGlobal.schreibeInfo("Neue Stadt: ${parkhaus.stadtname} (parkhausEbeneID: ${parkhaus.id})")

    }

    open fun erstelleInitStadt() {
        val city = parkhausServiceGlobal.cities.random()

        val pa = databaseGlobal.getParkhausüberStandtnamen(city.name)

        if (pa != null) {
            ladeParkhausStadt(pa.id)
        } else {
            val ph = Parkhaus.fromCitiesDTO(city)
            parkhaus = databaseGlobal.persistEntity(ph)!!

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

    override fun initEbene(config: ParkhausEbeneConfigDTO): ParkhausEbene {
        val parkhaus = databaseGlobal.findeUeberID(parkhaus.id, Parkhaus::class.java) !!

        config.parkhaus = parkhaus
        val pe = ParkhausEbene.ausEbenenConfig(config)

        val fahrzeugTypen = config.FahrzeugPreise.map { eintrag ->  FahrzeugTyp.ausHashMapEintrag(eintrag, pe) }.toMutableList()
        pe.fahrzeugTypen = fahrzeugTypen

        parkhaus.parkhausEbeneHinzufügen(pe)
        this.parkhaus = databaseGlobal.mergeUpdatedEntity(parkhaus)!!

        val peGespeichert = this.parkhaus.ebenen.find { pE -> pE.name == config.ebenenNamen } !!
        this.parkhausServiceGlobal.ebenenSet.add(config)
        parkhausEbenen.add(peGespeichert)
        return peGespeichert
    }

    override fun erstelleTicket(parkhausEbeneName: String, params: ParkhausServletPostDto): Ticket {
        val originalPlatz = params.space
        val parkhausEbeneID = getIdUeberName(parkhausEbeneName)
        val belegtePlaetze = getAutosInParkEbene(parkhausEbeneID, true)
            .map { auto -> auto.platznummer !! }.toSet()

        val gesamtPlaetze = (1..this.parkhausEbenen.find { pe -> pe.id == parkhausEbeneID }!!.maxPlätze).toSet()

        val verfuegbarePlaetze = gesamtPlaetze.subtract(belegtePlaetze)

        if(params.space !in verfuegbarePlaetze) {
            params.space = verfuegbarePlaetze.random()
        }

        val auto = autoHinzufuegen(parkhausEbeneID, params)

        val ticket = Ticket()

        ticket.Preisklasse = 2
        ticket.Auto = auto
        auto.ticket = ticket
        val parkhausEbeneToAdd = getParkhausEbenen().first { e -> e.id == parkhausEbeneID }
        parkhausEbeneToAdd.tickets.add(ticket)
        this.databaseGlobal.persistEntity(ticket)

        logGlobal.schreibeInfo("Auto wurde hinzugefügt ${auto.autonummer}. Gewünscht: ${originalPlatz} geparkt in: ${auto.platznummer}")
        return ticket
    }

    override fun autoHinzufuegen(parkhausEbeneID: Long, params: ParkhausServletPostDto):Auto {

        val auto = Auto(params.hash,params.color,params.space,params.license, params.vehicleType, params.clientCategory)
        // this.databaseGlobal.persistEntity(auto)
        undoList.add(auto)
        return auto
    }

    override fun ticketBezahlen(parkhausEbeneName: String, ticket: Ticket, timeCheckOut: Date): Long {
        val parkhausEbeneID = getIdUeberName(parkhausEbeneName)
        return ticketBezahlen(parkhausEbeneID, ticket, timeCheckOut)
    }

    override fun ticketBezahlen(parkhausEbeneID: Long, ticket: Ticket, timeCheckOut: Date): Long {

        ticket.Ausfahrdatum = timeCheckOut


        ticket.price = errechneTicketPreis(parkhausEbeneID, ticket, ticket.Auto !!)

        // this.databaseGlobal.mergeUpdatedEntity(ticket)
        ticket.Auto!!.imParkhaus = false
        this.databaseGlobal.mergeUpdatedEntity(ticket)

        parkhausServiceGlobal.statisticUpdateSubj
            .onNext(listOf(ManagerStatistikUpdateDTO.TAGESEINNAHMEN, ManagerStatistikUpdateDTO.WOCHENEINNAHMEN))
        if (ticket.Auto != null){
            undoList.add(ticket.Auto!!)
        }

        return ticket.price.toLong()
    }

    open fun errechneTicketPreis(parkhausEbeneID: Long, ticket: Ticket, auto: Auto  ): Int {
        val ebene = databaseGlobal.findeUeberID(parkhausEbeneID, ParkhausEbene::class.java)
        val duration = (ticket.Auto?.getParkdauer())

        val fahrzeugMultiplikator: Double = (
                ebene?.fahrzeugTypen?.find {
                entry -> entry.typ!!.lowercase() == ticket.Auto!!.typ.lowercase() }
                ?.multiplikator) ?: 1.0

        val anzahlHalberStunden = ((duration ?: 0) / 1800000 + 1)
        val multiplikator = (this.parkhaus.preisklasse!!+1) * fahrzeugMultiplikator
        return ((anzahlHalberStunden * 100) + (50 * multiplikator )).toInt()
                //Zeit in Millisekunden umrechnen in angebrochene halbe Stunden in Euro
                // dazu fest fix Preis von 50 cent mal preisklasse mal fahrzeugMultiplikator
    }

    override fun getSummeTicketpreiseUeberAutos(parkhausEbeneName: String): Int {
        val parkhausEbeneID = getIdUeberName(parkhausEbeneName)
        return databaseGlobal.getSummeDerTicketpreise(parkhausEbeneID) !!
    }

    override fun getEbeneUeberId(parkhausEbeneID: Long):ParkhausEbene{
        return parkhausEbenen.first{e-> e.id == parkhausEbeneID }
    }
    override fun getAlleUser(parkhausEbeneName: String):Int{
        val parkhausEbeneID = getIdUeberName(parkhausEbeneName)
        return databaseGlobal.getAnzahlAllerUser(parkhausEbeneID) !!
    }
    override fun getAktuelleUser(parkhausEbeneName: String): Int{
        val parkhausEbeneID = getIdUeberName(parkhausEbeneName)
        return databaseGlobal.getautosInParkEbene(parkhausEbeneID).size
    }

    override fun getDurchschnittUeberAutos(parkhausEbeneName: String): Int {
        val divisor = (getAlleUser(parkhausEbeneName) - getAktuelleUser(parkhausEbeneName))
        return if (divisor == 0) {
            0;
        } else
            (getSummeTicketpreiseUeberAutos(parkhausEbeneName) / divisor)
    }

    override fun findeTicketUeberParkplatz(parkhausEbeneName: String, placeNumber: Int): Ticket? {
        val parkhausEbeneID = getIdUeberName(parkhausEbeneName)
        return databaseGlobal.findeTicketUeberParkplatz(parkhausEbeneID, placeNumber)
    }
    override fun getIdUeberName(parkhausEbeneName: String):Long{
        return parkhausEbenen.first{e-> e.name == parkhausEbeneName }.id
    }

    override fun getParkhausEbenen():List<ParkhausEbene>{
        return parkhausEbenen
    }

    override fun getAutosInParkEbene(parkhausEbeneName: String, imParkhaus : Boolean): List<Auto>{
        val parkhausEbeneID = getIdUeberName(parkhausEbeneName)
        return getAutosInParkEbene(parkhausEbeneID, imParkhaus)
    }

    override fun getAutosInParkEbene(parkhausEbeneID: Long, imParkhaus : Boolean): List<Auto>{
        return databaseGlobal.getautosInParkEbene(parkhausEbeneID, imParkhaus)
    }

    override fun erstelleStatistikenUeberFahrzeuge(parkhausEbeneName: String): StatisticsChartDto {
        val parkhausEbeneID = getIdUeberName(parkhausEbeneName)
        val allCarsThatLeft = databaseGlobal.getautosInParkEbene(parkhausEbeneID, false)
        val allVehicleTypes = allCarsThatLeft.map { a->a.typ }.toSet().toList()
        val sumPricesOverVehicleTypes = allVehicleTypes.map { a->allCarsThatLeft.filter { a2-> a2.typ==a }.fold(0.0) { acc, i -> acc + (i.ticket!!.price)/100 } }
        val farben = setzeFarben(sumPricesOverVehicleTypes)
        return StatisticsChartDto(data = listOf(CarData("bar", allVehicleTypes, sumPricesOverVehicleTypes,farben)), Layout = setzeTitel("Auto-Typen","Preis in Euro"))
    }

    override fun getTageseinnahmen(parkhausEbeneName: String): EinnahmenBarDTO {
        val parkhausEbeneID = getIdUeberName(parkhausEbeneName)
        return getTageseinnahmen(parkhausEbeneID)
    }

    override fun getTageseinnahmen(parkhausEbeneID: Long): EinnahmenBarDTO {
        val sumOverDay = databaseGlobal.errechneTagesEinnahmen(parkhausEbeneID)
            return EinnahmenBarDTO(data = listOf(BarData("bar", listOf("Tages Einnahmen"),
                listOf((sumOverDay?.toDouble())?.div(100) ?: 0.0))))

    }

    override fun getWocheneinnahmen(parkhausEbeneName: String): EinnahmenBarDTO {
        val parkhausEbeneID = getIdUeberName(parkhausEbeneName)
        return getWocheneinnahmen(parkhausEbeneID)
    }

    override fun getWocheneinnahmen(parkhausEbeneID: Long): EinnahmenBarDTO {
        val sumOverDay = databaseGlobal.errechneWochenEinnahmen(parkhausEbeneID)
        return EinnahmenBarDTO(data = listOf(BarData("bar", listOf("Wochen Einnahmen"),
            listOf((sumOverDay?.toDouble())?.div(100) ?: 0.0))))

    }

    override fun getPrintStringAutos(parkhausEbeneName: String): String {
        val parkhausEbeneID = getIdUeberName(parkhausEbeneName)
        return getPrintStringAutos(parkhausEbeneID)
    }


    /**
     *
     * Gibt die Autos im Parkaus als CSV wieder
     * Notation: Nr/Timer/Duration/Price/Hash/Color/Space/client_category/vehicle_type/license
     *
     */
    override fun getPrintStringAutos(parkhausEbeneID: Long): String {
        val autosInParkhausEbene =  databaseGlobal.autosInParkEbeneHistoric(parkhausEbeneID)

        var autoStringListe: List<String> = autosInParkhausEbene.map {
                        "${it.autonummer}/" +
                        "${it.ticket?.Ausstellungsdatum?.time}/" +
                        "${if (it.imParkhaus) 0 else it.getParkdauer()}/" +
                        "${errechneTicketPreis(parkhausEbeneID, it.ticket!!, it)}/" +
                        "Ticket${it.ticket?.Ticketnummer}/" +
                        "${it.farbe}/" +
                        "${it.platznummer}/" +
                        "${it.typ}/" +
                        "${it.kategorie}/" +
                        "${it.kennzeichen}"
        }


        return autoStringListe.joinToString (  "," )
    }

    open fun zeigeHTMLParkhausListe(): String {
        val parkhaeuser = databaseGlobal.queryAllEntities(Parkhaus::class.java)?.filter { pa -> pa.ebenen.size != 0 && pa.stadtname != parkhaus.stadtname}
        val parkhausButtons = parkhaeuser?.map { p -> """<button type="button" onclick="wechsleStadt(this, '${p.id}')" class="btn btn-outline-primary" data-cityid="${p.id}">${p.stadtname}</button>"""
        }
        val buttonVariable = parkhausButtons?.joinToString (separator="") ?: ""
        if (buttonVariable.length < 2) {
            return "<h3>Wir sind bald an weiteren Standorten für Sie verfügbar!</h3>"
        }
        return buttonVariable
        //"""<button type="button" class="btn btn-outline-primary" data-cityid="${}">Primary</button>"""
    }

    override fun erstellePreiseFuerBundeslaender(): StatisticsChartDto? {
        val carMap = databaseGlobal.getTicketpreiseProBundesland()
        val uebersetzteNamen = carMap?.keys?.map { it-> Parkhaus.BundeslandÜbersetzung[it]?:it }
        val farben = setzeFarben(carMap)
        if (carMap != null) {
            return StatisticsChartDto(data = listOf(CarData("bar",
                uebersetzteNamen as List<String>, carMap.values.map{ a -> a.toDouble()/100},farben)), Layout = setzeTitel("Bundesländer","Preis in Euro"))
        }
        return null
    }

    override fun setzeFarben(carMap: Map<String, Int>?): Marker{
        val farbenListe = ArrayList<String>()
        val max = carMap?.values?.maxOrNull()

        for(item in carMap!!.values){
            farbenListe.add("rgba(${170-170*item/max!!},${170*item/max},0,1)")
        }
        return Marker(farbenListe)
    }

    override fun setzeFarben(values: List<Double>): Marker{
        val farbenListe = ArrayList<String>()
        val max = values.maxOrNull()

        for(item in values){
            farbenListe.add("rgba(${170-170*item/max!!},${170*item/max},0,1)")
        }
        return Marker(farbenListe)
    }

    override fun setzeTitel(xAchse:String,yAchse:String):Layout{
        return Layout(Xaxis(Title(xAchse)),Yaxis(Title(yAchse)))
    }

    override fun undo() {
        if (undoList.size>0){
            val toUndo = undoList.last()
            redoList.add(toUndo)
            if (toUndo.imParkhaus){
                toUndo.imParkhaus = false
                val parkhausEbene = databaseGlobal.findeParkhausEbeneByTicket(toUndo.ticket!!.Ticketnummer)
                deletedDatum.add(toUndo.ticket!!.Ausstellungsdatum)
                deletedReferenceToLevelName.add(databaseGlobal.findeParkhausEbeneByTicket(toUndo.ticket!!.Ticketnummer)?.name!!)
                toUndo.ticket?.Auto = null
                databaseGlobal.mergeUpdatedEntity(toUndo.ticket)
                databaseGlobal.deleteByID(toUndo.autonummer,Auto::class.java)
                databaseGlobal.deleteByID(toUndo.ticket!!.Ticketnummer,Ticket::class.java)
                databaseGlobal.mergeUpdatedEntity(parkhausEbene)
            }
            else{
                toUndo.imParkhaus = true
                toUndo.ticket?.Ausfahrdatum?.let { deletedDatum.add(it) }
                deletedReferenceToLevelName.add(databaseGlobal.findeParkhausEbeneByTicket(toUndo.ticket!!.Ticketnummer)?.name!!)
                toUndo.ticket?.Ausfahrdatum = null
                toUndo.ticket?.price = 0
                databaseGlobal.mergeUpdatedEntity(toUndo.ticket)
                databaseGlobal.mergeUpdatedEntity(toUndo)
            }
            undoList.removeLast()

        }
    }

    override fun redo() {
        if (redoList.size>0) {
            val toRedo = redoList.removeLast()
            undoList.add(toRedo)
            if (toRedo.imParkhaus) {
                toRedo.ticket?.Auto = toRedo
                ticketBezahlen(deletedReferenceToLevelName.removeLast(), toRedo.ticket!!, deletedDatum.removeLast())

            } else {
                toRedo.imParkhaus = true
                val pSPdto = ParkhausServletPostDto((1..9999).random(),deletedDatum.last().time,0,0.0,toRedo.hash!!,
                    toRedo.farbe!!,toRedo.platznummer!!,toRedo.kategorie,toRedo.typ,toRedo.kennzeichen!!, deletedDatum.last().time
                )
                val auto = erstelleTicket(deletedReferenceToLevelName.removeLast(), pSPdto).Auto
                if (redoList.last().autonummer == toRedo.autonummer) {
                    redoList.removeLast()
                    redoList.add(auto!!)
                }

            }
        }
    }

    open fun loescheRedoList(){
        redoList.clear()
    }

    open fun wechsleEbeneMaxParkplaetze(name: String, aktuell: Int, neu: Int) {
        val parkhausEbeneArrayPos = this.parkhausEbenen.indexOfFirst { pe -> pe.name == name }


        this.parkhausEbenen[parkhausEbeneArrayPos].maxPlätze = neu

        this.parkhausEbenen[parkhausEbeneArrayPos] =
            databaseGlobal.mergeUpdatedEntity(this.parkhausEbenen[parkhausEbeneArrayPos])!!

    }

    open fun wechsleEbeneOeffnungszeit(name: String, aktuell: Int, neu: Int) {
        val parkhausEbeneArrayPos = this.parkhausEbenen.indexOfFirst { pe -> pe.name == name }


        this.parkhausEbenen[parkhausEbeneArrayPos].öffnungszeit = neu

        this.parkhausEbenen[parkhausEbeneArrayPos] =
            databaseGlobal.mergeUpdatedEntity(this.parkhausEbenen[parkhausEbeneArrayPos])!!
    }

    open fun wechsleEbeneLadenschluss(name: String, aktuell: Int, neu: Int) {
        val parkhausEbeneArrayPos = this.parkhausEbenen.indexOfFirst { pe -> pe.name == name }


        this.parkhausEbenen[parkhausEbeneArrayPos].ladenschluss = neu

        this.parkhausEbenen[parkhausEbeneArrayPos] =
            databaseGlobal.mergeUpdatedEntity(this.parkhausEbenen[parkhausEbeneArrayPos])!!

    }


}

