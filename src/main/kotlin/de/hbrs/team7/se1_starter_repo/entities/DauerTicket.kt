package de.hbrs.team7.se1_starter_repo.entities

import java.util.*

class DauerTicket(private var ablaufDatum: Date, private var kundennummer: Int) : Ticket() {

    /**
     *
     * War im Entwurf geplant, konnte aber aus Zeit- und Designgründen nicht umgesetzt werden.
     *
     * @author Alexander Bohl
     */

    fun getAblaufdatum(): Date {
        return ablaufDatum
    }

    fun getKundennummer(): Int {
        return kundennummer
    }
}