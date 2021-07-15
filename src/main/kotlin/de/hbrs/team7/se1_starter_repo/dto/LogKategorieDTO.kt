package de.hbrs.team7.se1_starter_repo.dto

/**
 * Eine enum um die log Einträge zu standardisieren
 * @author Thomas Gerlach
 */
enum class LogKategorieDTO(val event: String) {
    INFO("INFO"),
    WARNING("WARNING"),
    DEBUG("DEBUG"),
}