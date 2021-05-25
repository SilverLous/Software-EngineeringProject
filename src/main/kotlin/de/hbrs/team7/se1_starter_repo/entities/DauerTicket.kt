package de.hbrs.team7.se1_starter_repo.entities

import java.util.*

class DauerTicket(private var ablaufDatum: Date, private var kundennummer: Int) : Ticket() {

    fun getAblaufdatum(): Date {
        return ablaufDatum
    }

    fun getKundennummer():Int {
        return kundennummer
    }
}