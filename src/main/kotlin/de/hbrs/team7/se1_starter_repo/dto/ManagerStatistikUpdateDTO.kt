package de.hbrs.team7.se1_starter_repo.dto


/**
 *
 * Speichert, welcher Plot aktualisiert werden soll,
 * eine Möglichkeit um die Performance für später zu verbessern.
 *
 * @param event TAGESEINNAHMEN oder WOCHENEINNAHMEN
 *
 * @author Thomas Gerlach
 */
enum class ManagerStatistikUpdateDTO(val event: String) {
    TAGESEINNAHMEN("TAGESEINNAHMEN"),
    WOCHENEINNAHMEN("WOCHENEINNAHMEN"),
}