import java.util.Date;

public interface TicketIF {

    public Date get_ausstelldatum();
    public long get_ausstelldatum_long();

    public Date get_bezahldatum();
    public long get_bezahldatum_long();

    public void ticket_bezahlen();

    public boolean darf_ausfahren();

}
