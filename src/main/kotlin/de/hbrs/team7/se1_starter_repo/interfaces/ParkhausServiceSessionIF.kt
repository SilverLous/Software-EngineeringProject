package de.hbrs.team7.se1_starter_repo.interfaces

import de.hbrs.team7.se1_starter_repo.dto.*
import de.hbrs.team7.se1_starter_repo.dto.ParkhausServletPostDto
import de.hbrs.team7.se1_starter_repo.dto.einnahmenBarDTO
import de.hbrs.team7.se1_starter_repo.dto.statisticsChartDto
import de.hbrs.team7.se1_starter_repo.entities.Auto
import de.hbrs.team7.se1_starter_repo.entities.Parkhaus
import de.hbrs.team7.se1_starter_repo.entities.ParkhausEbene
import de.hbrs.team7.se1_starter_repo.entities.Ticket
import java.time.Instant
import java.util.*

interface ParkhausServiceSessionIF {
    fun sessionInit()

    fun autoHinzufügen(ParkhausEbeneID: Long, params: ParkhausServletPostDto): Auto

    fun initEbene(name: String): ParkhausEbene

    fun erstelleTicket(ParkhausEbeneName: String, params: ParkhausServletPostDto): Ticket

    fun ticketBezahlen(ParkhausEbeneName: String, ticket: Ticket, timeCheckOut: Date): Long

    fun ticketBezahlen(ParkhausEbeneId: Long, ticket: Ticket, timeCheckOut: Date): Long

    fun getSummeTicketpreiseUeberAutos(ParkhausEbeneName: String): Int

    fun getDurchschnittUeberAutos(ParkhausEbeneName: String): Int

    fun getEbeneUeberId(ParkhausEbeneID: Long): ParkhausEbene

    fun findeTicketUeberParkplatz(ParkhausEbeneName: String, placeNumber: Int): Ticket?

    fun getAlleUser(ParkhausEbeneName: String): Int

    fun getAktuelleUser(ParkhausEbeneName: String): Int

    fun getParkhausEbenen():List<ParkhausEbene>

    fun getIdUeberName(ParkhausEbeneName: String):Long

    fun getAutosInParkEbene(ParkhausEbeneName: String, ImParkhaus: Boolean): List<Auto>

    fun getAutosInParkEbene(ParkhausEbeneID: Long, ImParkhaus : Boolean): List<Auto>

    fun erstelleStatistikenUeberFahrzeuge(ParkhausEbeneName: String): statisticsChartDto

    fun getTageseinnahmen(ParkhausEbeneName: String): einnahmenBarDTO

    fun getTageseinnahmen(ParkhausEbeneId: Long): einnahmenBarDTO

    fun getWocheneinnahmen(ParkhausEbeneName: String): einnahmenBarDTO

    fun getWocheneinnahmen(ParkhausEbeneId: Long): einnahmenBarDTO

    fun getPrintStringAutos(ParkhausEbeneName: String): String

    fun getPrintStringAutos(ParkhausEbeneId: Long): String

    fun erstellePreiseFürBundesländer(): statisticsChartDto?

    fun undo()

    fun redo()

    fun setzeFarben(carMap: Map<String, Int>?): marker

    fun setzeFarben(values: List<Double>): marker
}
