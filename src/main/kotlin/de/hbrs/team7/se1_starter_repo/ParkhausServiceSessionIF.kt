package de.hbrs.team7.se1_starter_repo

import de.hbrs.team7.se1_starter_repo.dto.ParkhausServletPostDto
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

    fun generateTicket(ParkhausEbeneID: Long, params: ParkhausServletPostDto): Ticket

    fun payForTicket(ParkhausEbeneID: Long, ticket: Ticket, timeCheckOut: Date): Long

    fun sumOverCars(ParkhausEbeneID: Long): Int

    fun averageOverCars(ParkhausEbeneID: Long): Int

    fun statsToChart(ParkhausEbeneID: Long): statisticsChartDto

    fun getLevelById(ParkhausEbeneID: Long): ParkhausEbene

    fun findTicketByPlace(ParkhausEbeneID: Long, placeNumber: Int): Ticket?

    fun getTotalUsers(ParkhausEbeneID: Long): Int

    fun getCurrenUsers(ParkhausEbeneID: Long): Int

    fun getParkhausEbenen():List<ParkhausEbene>

}