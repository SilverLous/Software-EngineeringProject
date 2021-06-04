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

    fun addCar(ParkhausEbeneID: String, params: ParkhausServletPostDto): Auto

    fun initEbene(name: String): ParkhausEbene

    fun generateTicket(ParkhausEbeneID: String, params: ParkhausServletPostDto): Ticket

    fun payForTicket(ParkhausEbeneID: String, ticket: Ticket, timeCheckOut: Date): Long

    fun sumOverCars(ParkhausEbeneID: String): Int

    fun averageOverCars(ParkhausEbeneID: String): Int

    fun statsToChart(ParkhausEbeneID: String): statisticsChartDto

    fun getLevelByName(ParkhausEbeneID: String): ParkhausEbene

    fun findTicketByPlace(placeNumber: Int): Ticket


}