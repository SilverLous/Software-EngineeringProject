package de.hbrs.team7.se1_starter_repo.interfaces

import de.hbrs.team7.se1_starter_repo.dto.*
import de.hbrs.team7.se1_starter_repo.dto.ParkhausServletPostDto
import de.hbrs.team7.se1_starter_repo.dto.einnahmenBarDTO
import de.hbrs.team7.se1_starter_repo.dto.statisticsChartDto
import de.hbrs.team7.se1_starter_repo.entities.Auto
import de.hbrs.team7.se1_starter_repo.entities.ParkhausEbene
import de.hbrs.team7.se1_starter_repo.entities.Ticket
import java.util.*

interface ParkhausServiceSessionIF {
    fun sessionInit()

    fun autoHinzufuegen(parkhausEbeneId: Long, params: ParkhausServletPostDto): Auto

    fun initEbene(name: String): ParkhausEbene

    fun erstelleTicket(parkhausEbeneName: String, params: ParkhausServletPostDto): Ticket

    fun ticketBezahlen(parkhausEbeneName: String, ticket: Ticket, timeCheckOut: Date): Long

    fun ticketBezahlen(parkhausEbeneId: Long, ticket: Ticket, timeCheckOut: Date): Long

    fun getSummeTicketpreiseUeberAutos(parkhausEbeneName: String): Int

    fun getDurchschnittUeberAutos(parkhausEbeneName: String): Int

    fun getEbeneUeberId(parkhausEbeneId: Long): ParkhausEbene

    fun findeTicketUeberParkplatz(parkhausEbeneName: String, placeNumber: Int): Ticket?

    fun getAlleUser(parkhausEbeneName: String): Int

    fun getAktuelleUser(parkhausEbeneName: String): Int

    fun getParkhausEbenen():List<ParkhausEbene>

    fun getIdUeberName(parkhausEbeneName: String):Long

    fun getAutosInParkEbene(parkhausEbeneName: String, imParkhaus: Boolean): List<Auto>

    fun getAutosInParkEbene(parkhausEbeneId: Long, imParkhaus : Boolean): List<Auto>

    fun erstelleStatistikenUeberFahrzeuge(parkhausEbeneName: String): StatisticsChartDto

    fun getTageseinnahmen(parkhausEbeneName: String): EinnahmenBarDTO

    fun getTageseinnahmen(parkhausEbeneId: Long): EinnahmenBarDTO

    fun getWocheneinnahmen(parkhausEbeneName: String): EinnahmenBarDTO

    fun getWocheneinnahmen(parkhausEbeneId: Long): EinnahmenBarDTO

    fun getPrintStringAutos(parkhausEbeneName: String): String

    fun getPrintStringAutos(parkhausEbeneId: Long): String

    fun erstellePreiseFuerBundeslaender(): StatisticsChartDto?

    fun undo()

    fun redo()

    fun setzeFarben(carMap: Map<String, Int>?): Marker

    fun setzeFarben(values: List<Double>): Marker

    fun setzeTitel(xAchse:String,yAchse:String):Layout
}
