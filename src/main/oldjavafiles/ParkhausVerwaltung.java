import java.util.Date;

public class ParkhausVerwaltung implements ParkhausVerwaltungIF{

    public ParkhausVerwaltung(){

    };
    public int get_anzahl_freier_plaetze() {
        return 0;
    }

    public boolean manuelles_ausparken(TicketIF kunde) {
        return false;
    }

    public TicketIF get_ticket(int ticketnummer) {
        return null;
    }

    public TicketIF kaufe_ticket(Date startdatum, Date enddatum) {
        return null;
    }

    public double get_ticket_preis(Date startdatum, Date enddatum) {
        return 0;
    }

    public double get_statistiken() {
        return 0;
    }

    public void set_sprache(String sprache) {

    }
}
