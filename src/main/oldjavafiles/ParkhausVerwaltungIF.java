import java.util.Date;

public interface ParkhausVerwaltungIF {

    public int get_anzahl_freier_plaetze();

    public boolean manuelles_ausparken(TicketIF kunde);

    public TicketIF get_ticket(int ticketnummer);

    public TicketIF kaufe_ticket(Date startdatum, Date enddatum);

    public double get_ticket_preis(Date startdatum, Date enddatum);

    public double get_statistiken();

    public void set_sprache(String sprache);
}
