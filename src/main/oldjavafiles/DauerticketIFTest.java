import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class DauerticketIFTest {

    DauerticketIF ticket1;
    DauerticketIF ticket2;
    DauerticketIF ticket3;

    @BeforeAll
    void setUP() {
        ticket1 = new Dauerticket(0);
    }
    @Test
    void get_auslaufdatum() {
        long datum1 = ticket1.get_auslaufdatum_long();
        assertNotNull(datum1);
        assertTrue(datum1 < new Date().getTime());
        assertTrue(ticket1.get_auslaufdatum() instanceof Date);
    }
    @Test
    void darf_ausfahren() {
        ticket2 = new Dauerticket(200000);
        ticket3 = new Dauerticket(-100000);
        assertFalse(ticket1.darf_ausfahren());
        assertTrue (ticket2.darf_ausfahren());
        assertFalse(ticket3.darf_ausfahren());
    }

}