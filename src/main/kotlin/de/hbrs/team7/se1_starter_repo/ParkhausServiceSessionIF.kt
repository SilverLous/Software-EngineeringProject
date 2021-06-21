package de.hbrs.team7.se1_starter_repo

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

    fun addCar(ParkhausEbeneID: Long, params: ParkhausServletPostDto): Auto

    fun initEbene(name: String): ParkhausEbene

    fun generateTicket(ParkhausEbeneName: String, params: ParkhausServletPostDto): Ticket

    fun payForTicket(ParkhausEbeneName: String, ticket: Ticket, timeCheckOut: Date): Long

    fun sumOverCars(ParkhausEbeneName: String): Int

    fun averageOverCars(ParkhausEbeneName: String): Int

    fun getLevelById(ParkhausEbeneID: Long): ParkhausEbene

    fun findTicketByPlace(ParkhausEbeneName: String, placeNumber: Int): Ticket?

    fun getTotalUsers(ParkhausEbeneName: String): Int

    fun getCurrenUsers(ParkhausEbeneName: String): Int

    fun getParkhausEbenen():List<ParkhausEbene>

    fun getIdByName(ParkhausEbeneName: String):Long

    fun autosInParkEbene(ParkhausEbeneName: String, ImParkhaus: Boolean): List<Auto>

    fun autosInParkEbene(ParkhausEbeneID: Long, ImParkhaus : Boolean): List<Auto>

    fun generateStatisticsOverVehicle(ParkhausEbeneName: String): statisticsChartDto

    fun showDaysTakings(ParkhausEbeneName: String): einnahmenBarDTO

    fun showDaysTakings(ParkhausEbeneName: Long): einnahmenBarDTO

    fun showWeeksTakings(ParkhausEbeneName: String): einnahmenBarDTO

    fun showWeeksTakings(ParkhausEbeneName: Long): einnahmenBarDTO

    fun getPrintStringCars(ParkhausEbeneName: String): String

    fun getPrintStringCars(ParkhausEbeneName: Long): String

    fun generatePriceByFederalState(): statisticsChartDto?
}