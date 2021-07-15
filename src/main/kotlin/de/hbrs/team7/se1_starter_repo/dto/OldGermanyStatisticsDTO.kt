package de.hbrs.team7.se1_starter_repo.dto

/**
 * Eine Klasse um besser zwischen JSP und Session zu kommunizieren
 * @author Thomas Gerlach
 */
data class OldGermanyStatisticsDTO(
    val brd: Pair<Long, Long>,
    val ddr: Pair<Long, Long>
)
