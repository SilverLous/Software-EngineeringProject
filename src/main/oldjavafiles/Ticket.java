import java.util.Date;

public class Ticket implements TicketIF{
    Date ausstelldatum;
    Date bezahldatum;

    public Ticket() {
        this.ausstelldatum = new Date();
        this.bezahldatum = new Date();
    }

    public Ticket(long ausstelldatum, long bezahldatum){
        this.ausstelldatum = new Date(ausstelldatum);
        if (bezahldatum == 0) {
            this.bezahldatum = this.ausstelldatum;
        } else
        this.bezahldatum= new Date(bezahldatum);
    }
    public Date get_ausstelldatum() {
        return ausstelldatum;
    }

    public long get_ausstelldatum_long() {
        return ausstelldatum.getTime();
    }

    public Date get_bezahldatum() {
        return bezahldatum;
    }

    public long get_bezahldatum_long() {
        return bezahldatum.getTime();
    }

    public void ticket_bezahlen() {
        this.bezahldatum = new Date();
    }

    public boolean darf_ausfahren() {
        Date now = new Date();
        return (now.getTime() - bezahldatum.getTime()) <= 300000;

    }

}
