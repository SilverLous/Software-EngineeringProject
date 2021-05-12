import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class TicketIFTest {

    //globale Variablen für die Tests
    TicketIF ticket1;

    @BeforeEach
    void setUP() {
        ticket1 = new Ticket();
    }

    @Test
    void ticket_ziehen() {
        ticket1 = new Ticket();
        assertNotNull(ticket1);
    }

    @Test
    void ticket_bezahlen() {
        Date date1 = new Date();
        ticket1.ticket_bezahlen();
        Date date2 = new Date();
        assertNotEquals(ticket1.get_bezahldatum(), ticket1.get_ausstelldatum());
        assertTrue(date1.after(ticket1.get_bezahldatum()));
        assertTrue(date2.before(ticket1.get_bezahldatum()));
    }

    @Test
    void darf_ausfahren() {
        ticket1.ticket_bezahlen();
        TicketIF ticket2 = new Ticket(new Date().getTime() - 200000,0);
        TicketIF ticket3 = new Ticket(new Date().getTime() - 600000, 0);
        TicketIF ticket4 = new Ticket(new Date().getTime() + 50000, 0);
        assertTrue (ticket2.darf_ausfahren());
        assertFalse(ticket3.darf_ausfahren()); // Bezahlen ist zu lange her
        assertFalse(ticket4.darf_ausfahren()); // bezahlt erst in der Zukunft -> Betrugsversuch? technischer Fehler?
    }

    @Test
    void get_ausstelldatum() {
        long datum1 = ticket1.get_ausstelldatum_long();
        assertNotNull(datum1);
        assertTrue(datum1 < new Date().getTime());
        assertTrue(ticket1.get_ausstelldatum() instanceof Date);
    }

   /* @Test
    void get_auslaufdatum() {
        long datum1 = ticket2.get_auslaufdatum_long();
        assertNotNull(datum1);
        assertTrue(datum1 < new Date().getTime());
        assertTrue(ticket1.get_auslaufdatum() instanceof Date);
    }*/

    @Test
    void get_bezahldatum() {
        long datum1 = ticket1.get_bezahldatum_long();
        assertNotNull(datum1);
        assertTrue(datum1 < new Date().getTime());
        assertTrue(ticket1.get_bezahldatum() instanceof Date);
    }
}