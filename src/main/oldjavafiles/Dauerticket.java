import java.util.Date;

public class Dauerticket extends Ticket implements DauerticketIF {

    Date auslaufdatum;
    public Dauerticket(long auslaufdatum){
        this.ausstelldatum = new Date();
        this.bezahldatum = new Date();
        this.auslaufdatum = new Date(ausstelldatum.getTime() + auslaufdatum);
    }

    public Dauerticket(long ausstelldatum, long bezahldatum, long auslaufdatum){
        this.ausstelldatum = new Date(ausstelldatum);
        if (bezahldatum == 0) {
            this.bezahldatum = this.ausstelldatum;
        } else
            this.bezahldatum= new Date(bezahldatum);
        this.auslaufdatum = new Date(auslaufdatum);
    }

    public Date get_auslaufdatum() {
        return auslaufdatum;
    }

    public long get_auslaufdatum_long() {
        return auslaufdatum.getTime();
    }

    @Override
    public boolean darf_ausfahren() {
        Date now = new Date();
        return (now.getTime() < auslaufdatum.getTime()) && (now.getTime() > ausstelldatum.getTime());

    }
}
