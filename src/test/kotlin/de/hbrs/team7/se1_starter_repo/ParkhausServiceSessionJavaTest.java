package de.hbrs.team7.se1_starter_repo;

import de.hbrs.team7.se1_starter_repo.dto.ParkhausEbeneConfigDTO;
import de.hbrs.team7.se1_starter_repo.dto.ParkhausServletPostDto;
import de.hbrs.team7.se1_starter_repo.dto.PreistabelleDTO;
import de.hbrs.team7.se1_starter_repo.entities.Auto;
import de.hbrs.team7.se1_starter_repo.entities.ParkhausEbene;
import de.hbrs.team7.se1_starter_repo.entities.Ticket;
import de.hbrs.team7.se1_starter_repo.services.ParkhausServiceGlobal;
import de.hbrs.team7.se1_starter_repo.services.ParkhausServiceSession;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import org.jboss.weld.context.bound.BoundSessionContext;
import org.jboss.weld.junit5.auto.ActivateScopes;
import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.*;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


// https://github.com/weld/weld-junit/blob/master/junit5/README.md
@EnableAutoWeld
@ActivateScopes({SessionScoped.class, ApplicationScoped.class})
@AddBeanClasses({ParkhausServiceSession.class, ParkhausServiceGlobal.class})
public class ParkhausServiceSessionJavaTest {

    @Inject
    BoundSessionContext sessionContext;

    @Inject
    ParkhausServiceSession parkhausServiceSession;

    @Inject
    ParkhausServiceGlobal parkhausServiceGlobal;


    SecureRandom zufallszahlengenerator;

    HashMap<String, Double> fahrzeugPreise = new HashMap<>();
    String eingabeHash = "echterHash";
    String eingabeFarbe = "REGENBOGEN";
    String eingabeClientCategory = "Family";
    String eingabeType = "SUV";
    String eingabeKennzeichen = "Y-123 456";


    /**
     *
     * Vor allen Tests die Fahrzeugpreise festlegen. Die werden nicht mehr verändert, können aber zentral gesteuert
     * werden
     *
     * @author Alexander Bohl
     */
    @BeforeEach
    public void setup() {
        zufallszahlengenerator = new SecureRandom();
        fahrzeugPreise.put("PKW", 1.0);
        fahrzeugPreise.put("Pickup", 1.2);
        fahrzeugPreise.put("SUV", 1.5);
    }

    @AfterEach
    public void tearDown(){
        List<Auto> x = parkhausServiceSession.getUndoList() ;
        while(x != null && x.size()>0){
            parkhausServiceSession.undo();
        }
    }

    @Test
    public void sessionInitTest() {
        assertNotNull(parkhausServiceSession.getParkhaus().getStadtname());
    }

    @Test
    public void sessionAddLevel() {

        ParkhausEbeneConfigDTO[] configs = generiereConfigDTOs(2);
        parkhausServiceSession.initEbene(configs[0]);
        List<ParkhausEbene> temp = parkhausServiceSession.getParkhausEbenen();
        assertNotNull(temp);
        Assertions.assertEquals("Generierte Ebene Nr. 0", parkhausServiceSession.getEbeneUeberId(temp.get(0).getId()).getName());
        parkhausServiceSession.initEbene(configs[1]);
        Assertions.assertEquals("Generierte Ebene Nr. 1", parkhausServiceSession.getEbeneUeberId(temp.get(1).getId()).getName());
        Assertions.assertEquals(2, parkhausServiceSession.getParkhausEbenen().size());
        Assertions.assertEquals(2, parkhausServiceGlobal.getEbenenSet().size());

    }

    @Test
    public void sessionAddLevelInvalidateTest() {

        ParkhausEbeneConfigDTO[] conf = generiereConfigDTOs(2);
        parkhausServiceSession.initEbene(conf[0]);
        String stadtName = parkhausServiceSession.getParkhaus().getStadtname();

        parkhausServiceSession.initEbene(conf[1]);
        Assertions.assertEquals(2, parkhausServiceGlobal.getEbenenSet().size());
        sessionContext.invalidate();
        String zweiterStadtName = parkhausServiceSession.getParkhaus().getStadtname();
        Assertions.assertNotEquals(stadtName, zweiterStadtName);

    }

    @Test
    public void testFindTicketByPlace() {
        ParkhausEbene[] ebenen = generiereEbenen(1);
        Long timeNow = System.currentTimeMillis();
        ParkhausServletPostDto paramsErstesAuto = new ParkhausServletPostDto(2, timeNow, 0,
                0, eingabeHash, eingabeFarbe, 1, eingabeClientCategory, eingabeType, eingabeKennzeichen,timeNow);
        Ticket erstesTestTicket = parkhausServiceSession.erstelleTicket(ebenen[0].getName(), paramsErstesAuto);
        Ticket zweiterVerweisTicket = parkhausServiceSession.findeTicketUeberParkplatz(ebenen[0].getName(), erstesTestTicket.getAuto().getPlatznummer());
        Assertions.assertEquals(erstesTestTicket, zweiterVerweisTicket);
    }

    @Test
    public void testGetSumme() {
        ParkhausEbene ebene = generiereEbenen(1)[0];
        int wieLange = 8;
        Ticket tTest;
        int preis = 0;

        for (int i=0;i<wieLange;i++){
            Long timeNow = System.currentTimeMillis();
            ParkhausServletPostDto paramsErstesAuto = new ParkhausServletPostDto(2, timeNow, 0,
                    0, eingabeHash+i, eingabeFarbe, 1, eingabeClientCategory, "kategorie1", eingabeKennzeichen,timeNow);
            tTest = parkhausServiceSession.erstelleTicket(ebene.getName(),paramsErstesAuto);
            parkhausServiceSession.ticketBezahlen(ebene.getName(),tTest,new Date(timeNow));
            preis+=tTest.getPrice();

            Assertions.assertEquals(preis,parkhausServiceSession.getSummeTicketpreiseUeberAutos(ebene.getName()));
        }

    }

    @Test
    public void statisticUpdateSubjTest() {
        AtomicInteger events = new AtomicInteger();
        this.parkhausServiceGlobal.getStatisticUpdateSubj().subscribe(
                s -> events.getAndIncrement()
        );

        testGetSumme();

        assertTrue(events.get() > 0);

    }

    @Test
    public void testGetDurchschnitt() {
        ParkhausEbene[] ebenen = generiereEbenen(1);
        int wieLange = 8;
        Ticket tTest;
        double preis = 0.0;
        for (int i = 0; i < wieLange; i++) {
            Long timeNow = System.currentTimeMillis();
            ParkhausServletPostDto paramsErstesAuto = new ParkhausServletPostDto(2, timeNow, 0,
                    0, eingabeHash + i, eingabeFarbe, 1, eingabeClientCategory, eingabeType, eingabeKennzeichen,timeNow);
            tTest = parkhausServiceSession.erstelleTicket(ebenen[0].getName(), paramsErstesAuto);
            parkhausServiceSession.ticketBezahlen(ebenen[0].getName(), tTest, new Date(timeNow + 100));
            preis+=tTest.getPrice();
            Assertions.assertEquals(preis/(i+1), parkhausServiceSession.getDurchschnittUeberAutos(ebenen[0].getName()), 10);

        }
    }


    @Test
    public void testGetAlleNutzer() {
        ParkhausEbene[] ebenen = generiereEbenen(1);
        int wieLange = 8;
        Ticket tTest = new Ticket();

        for (int i=0;i<wieLange;i++){
            Long timeNow = System.currentTimeMillis();
            ParkhausServletPostDto paramsErstesAuto = new ParkhausServletPostDto(2, timeNow, 0,
                    0, eingabeHash+i, eingabeFarbe, 1, eingabeClientCategory, eingabeType, eingabeKennzeichen,timeNow);
            parkhausServiceSession.erstelleTicket(ebenen[0].getName(),paramsErstesAuto);
            tTest.getAuto();
            Assertions.assertEquals(i+1,parkhausServiceSession.getAlleUser(ebenen[0].getName()));
        }

    }

    @Test
    @DisplayName("Test der erstelleTicket-Funktion")
    public void testErstelleTicket() {
        ParkhausEbene[] ebenen = generiereEbenen(1);
        int wieLange = 8;
        Auto aTest;
        Ticket tTest;

        for (int i=0;i<wieLange;i++){
            Long timeNow = System.currentTimeMillis();
            ParkhausServletPostDto paramsErstesAuto = new ParkhausServletPostDto(2, timeNow, 0,
                    0, eingabeHash+i, eingabeFarbe, 1, eingabeClientCategory, eingabeType, eingabeKennzeichen,timeNow);
            tTest = parkhausServiceSession.erstelleTicket(ebenen[0].getName(),paramsErstesAuto);
            aTest = tTest.getAuto();
            Assertions.assertEquals(eingabeHash+i,aTest.getHash());

            ParkhausServletPostDto tDtoTest = tTest.zuParkhausServletPostDto();

            Assertions.assertNotNull(tDtoTest);
        }

    }


    @Test
    @DisplayName("Test der addCar-Funktion")
    public void testAllCars() {
        ParkhausEbene[] ebenen = generiereEbenen(1);
        int wieLange = 8;
        Auto aTest;
        Ticket tTest;

        for (int i=0;i<wieLange;i++){
            Long timeNow = System.currentTimeMillis();
            ParkhausServletPostDto paramsErstesAuto = new ParkhausServletPostDto(2, timeNow, 0,
                    0, eingabeHash+i, eingabeFarbe, 1, eingabeClientCategory, eingabeType, eingabeKennzeichen,timeNow);
            tTest = parkhausServiceSession.erstelleTicket(ebenen[0].getName(),paramsErstesAuto);
            aTest = tTest.getAuto();
            Assertions.assertEquals(aTest.getHash(),parkhausServiceSession.getAutosInParkEbene(ebenen[0].getName(),true).get(i).getHash());
        }

    }

    @Test
    @DisplayName("Test der autosInParkEbene-Funktion")
    public void testGetCarsInEbene() {
        ParkhausEbene[] ebenen = generiereEbenen(1);
        int wieLange = 8;
        Ticket tTest;

        for (int i=0;i<wieLange;i++){
            Long timeNow = System.currentTimeMillis();
            ParkhausServletPostDto paramsErstesAuto = new ParkhausServletPostDto(2+i, timeNow, 0,
                    0, eingabeHash+i, eingabeFarbe, 1+i, eingabeClientCategory, eingabeType, eingabeKennzeichen,timeNow);
            tTest = parkhausServiceSession.erstelleTicket(ebenen[0].getName(),paramsErstesAuto);
            tTest.getAuto();
            Assertions.assertEquals(1,parkhausServiceSession.getAutosInParkEbene(ebenen[0].getName(), true).size());
            Assertions.assertEquals(i+0,parkhausServiceSession.getAutosInParkEbene(ebenen[0].getName(),false).size());
            parkhausServiceSession.ticketBezahlen(ebenen[0].getName(),tTest,Date.from(Instant.now()));
            Assertions.assertEquals(0,parkhausServiceSession.getAutosInParkEbene(ebenen[0].getName(), true).size());
            Assertions.assertEquals(i+1,parkhausServiceSession.getAutosInParkEbene(ebenen[0].getName(),false).size());
        }

    }

    @Test
    @DisplayName("Test der generateStatisticsOverVehicle-Funktion")
    public void testGenerateStatisticsOverVehicle() {
        ParkhausEbene[] ebenen = generiereEbenen(1);
        testAllCars();

        assertNotNull(parkhausServiceSession.erstelleStatistikenUeberFahrzeuge(ebenen[0].getName()));
    }

    @Test
    @DisplayName("Test der undo Funktion")
    public void testOftestUndo() {
        testUndo();
    }

    public void testUndo(){
        ParkhausEbene[] ebenen = generiereEbenen(1);
        int wieLange = 8;
        Ticket tTest = new Ticket();

        for (int i=0;i<wieLange;i++){
            Long timeNow = System.currentTimeMillis();
            ParkhausServletPostDto paramsErstesAuto = new ParkhausServletPostDto(2, timeNow, 0,
                    0, eingabeHash+i, eingabeFarbe, 1, eingabeClientCategory, eingabeType, eingabeKennzeichen,timeNow);
            tTest = parkhausServiceSession.erstelleTicket(ebenen[0].getName(),paramsErstesAuto);
            tTest.getAuto();
        }
        parkhausServiceSession.ticketBezahlen(ebenen[0].getName(),tTest,Date.from(Instant.now()));
        parkhausServiceSession.undo();
        Assertions.assertEquals(wieLange,parkhausServiceSession.getAutosInParkEbene(ebenen[0].getName(), true).size());
        parkhausServiceSession.undo();
        Assertions.assertEquals(wieLange-1,parkhausServiceSession.getAutosInParkEbene(ebenen[0].getName(), true).size());
    }

    @Test
    @DisplayName("Test der redo Funktion")
    @Disabled
    public void testRedo(){
        testUndo();
        String parkhausName = "Generierte Ebene Nr. 0";
        List<Auto> autoliste = parkhausServiceSession.getAutosInParkEbene(parkhausName, true);

        parkhausServiceSession.redo();
        Assertions.assertEquals(autoliste.size()+1,parkhausServiceSession.getAutosInParkEbene(parkhausName, true).size());
        parkhausServiceSession.redo();
        Assertions.assertEquals(autoliste.size(),parkhausServiceSession.getAutosInParkEbene(parkhausName, true).size());
        parkhausServiceSession.undo();
        parkhausServiceSession.redo();
        Assertions.assertEquals(autoliste.size(),parkhausServiceSession.getAutosInParkEbene(parkhausName, true).size());
        parkhausServiceSession.undo();
        Assertions.assertEquals(autoliste.size()+1,parkhausServiceSession.getAutosInParkEbene(parkhausName, true).size());
        parkhausServiceSession.undo();
        Assertions.assertEquals(autoliste.size(),parkhausServiceSession.getAutosInParkEbene(parkhausName, true).size());
        parkhausServiceSession.undo();
        parkhausServiceSession.redo();
        parkhausServiceSession.redo();
        parkhausServiceSession.redo();
        Assertions.assertEquals(autoliste.size(),parkhausServiceSession.getAutosInParkEbene(parkhausName, true).size());

    }


    @Nested
    @DisplayName("Basic IO chain")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class BasicIOTest {
        Long timeNow = System.currentTimeMillis();
        ParkhausServletPostDto paramsErstesAuto = new ParkhausServletPostDto(2, timeNow, 0,
                0, eingabeHash, eingabeFarbe, 1, eingabeClientCategory, eingabeType, eingabeClientCategory,timeNow);

        @Test
        @Order(1)
        @DisplayName("Test der ziehe Ticket Funktion")
        public void zieheTicketTest() {
            ParkhausEbene[] ebenen = generiereEbenen(1);

            Ticket erstesTestTicket = parkhausServiceSession.erstelleTicket(ebenen[0].getName(), paramsErstesAuto);
            assertNotNull(erstesTestTicket);
        }
    }

    /**
     *
     * @author: Alexander Bohl
     */
    @Test
    @DisplayName("Testen der wechsleEbeneMaxParkplätze-Funktion")
    public void testWechsleEbeneMaxParkplaetze() {
        ParkhausEbene[] ebenen = generiereEbenen(2);
        ParkhausEbene ebene = ebenen[0];
        ParkhausEbene ebene2 = ebenen[1];
        Assertions.assertEquals(120, ebene.getMaxPlätze());
        int neuePlaetze = zufallszahlengenerator.nextInt(19);
        neuePlaetze++;
        parkhausServiceSession.wechsleEbeneMaxParkplaetze(ebene.getName(),12,neuePlaetze);
        Assertions.assertEquals(neuePlaetze, ebene.getMaxPlätze());
        Assertions.assertEquals(120, ebene2.getMaxPlätze());
    }

    /**
     *
     * @author: Alexander Bohl
     */
    @Test
    @DisplayName("Testen der wechsleEbeneÖffnungszeit-Funktion")
    public void testWechsleEbeneOeffnungszeit() {
        ParkhausEbene[] ebenen = generiereEbenen(2);
        ParkhausEbene ebene = ebenen[0];
        ParkhausEbene ebene2 = ebenen[1];
        Assertions.assertEquals(6,ebene.getöffnungszeit());
        Assertions.assertEquals(6,ebene2.getöffnungszeit());
        int neueOeffnungszeit = zufallszahlengenerator.nextInt(22);
        neueOeffnungszeit++;
        parkhausServiceSession.wechsleEbeneOeffnungszeit(ebene.getName(),6,neueOeffnungszeit);
        Assertions.assertEquals(neueOeffnungszeit,ebene.getöffnungszeit());
        Assertions.assertEquals(6,ebene2.getöffnungszeit());
    }

    @Test
    @DisplayName("Test von vernuenftigen Preis Funktion")
    public void testNeuePreisFunktion() {
        ParkhausEbene ebene = generiereEbenen(1)[0];
        int wieOft = 8;
        Ticket tTest;
        Long timeNow = System.currentTimeMillis();
        ParkhausServletPostDto paramsErstesAuto = new ParkhausServletPostDto(2, timeNow, 0,
                0, eingabeHash, eingabeFarbe, 1, eingabeClientCategory, "bitteMult1", eingabeKennzeichen,timeNow);
        tTest = parkhausServiceSession.erstelleTicket(ebene.getName(),paramsErstesAuto);

        for (int i=0;i<wieOft;i++){
            parkhausServiceSession.ticketBezahlen(ebene.getName(),tTest,Date.from(Instant.ofEpochMilli(timeNow+1800000*(1+i))));
            Assertions.assertEquals(100*(i+1) + 50 * (ebene.getParkhaus().getPreisklasse()+1),tTest.getPrice());
            parkhausServiceSession.undo();
        }
    }


    /**
     *
     * @author: Alexander Bohl
     */
    @Test
    @DisplayName("Testen der wechsleEbeneLadenschluss-Funktion")
    public void testWechsleEbeneLadenschluss() {
        ParkhausEbene[] ebenen = generiereEbenen(2);
        ParkhausEbene ebene = ebenen[0];
        ParkhausEbene ebene2 = ebenen[1];
        Assertions.assertEquals(24,ebene.getLadenschluss());
        Assertions.assertEquals(24,ebene2.getLadenschluss());
        int neuerLadenschluss = zufallszahlengenerator.nextInt(18);
        neuerLadenschluss += 6; // verhindern, dass Ladenschluss vor Ladenöffnung ist
        parkhausServiceSession.wechsleEbeneLadenschluss(ebene.getName(), 24, neuerLadenschluss);
        Assertions.assertEquals(neuerLadenschluss,ebene.getLadenschluss());
        Assertions.assertEquals(24,ebene2.getLadenschluss());
    }

    /**
     *
     * @author: Alexander Bohl
     */
    @Test
    @DisplayName("Testen der Kassenfunktion generiereKassenAusgabe(String,Int)")
    public void testKasse() {
        ParkhausEbene ebene = generiereEbenen(1)[0];
        int iterationen = 10;
        Ticket[] ticket = new Ticket[12];

        for (int i=1;i<iterationen;i++){
            Long timeNow = System.currentTimeMillis();
            ParkhausServletPostDto paramsErstesAuto = new ParkhausServletPostDto(2, timeNow, 0,
                    0, eingabeHash+i, eingabeFarbe, i, eingabeClientCategory, "kategorie1", eingabeKennzeichen,timeNow);
            ticket[i] = parkhausServiceSession.erstelleTicket(ebene.getName(),paramsErstesAuto);
        }

        Assertions.assertEquals("<h2>Ihr Parkplatz konnte leider nicht gefunden werden</h2>",parkhausServiceSession.generiereKassenAusgabe(ebene.getName(),50));
        for (int i=1;i<iterationen;i++) {
            Double multiplikator = parkhausServiceSession.errechneTicketPreis(ebene.getId(), ticket[i], ticket[i].getAuto())/100.0;
            String compareString1 = "<h3>Ihre Parkplatznummer: " + i + "</h3>\nIhre Parkgebühren : " + multiplikator + "€<br>Ihr Fahrzeugklassen-Multiplikator: 1.0<br>";
            Assertions.assertEquals(compareString1,parkhausServiceSession.generiereKassenAusgabe(ebene.getName(),i));
            parkhausServiceSession.ticketBezahlen(ebene.getName(),ticket[i],Date.from(Instant.now()));
        }
    }


    /**
     *
     * @author: Alexander Bohl
     */
    @Test
    @DisplayName("Test der getFahrzeugmultiplikatorenDTO()")
    public void testFahrzeugmultiplikatorenDTO() {
        generiereEbenen(1);
        String pickup = "Pickup";
        String pkw = "PKW";
        String suv = "SUV";
        PreistabelleDTO tabelle = parkhausServiceSession.getFahrzeugmultiplikatorenDTO(0);
        String testFahrzeugKlasse = tabelle.getFahrzeugKlassen().get(0);
        testFahrzeugKlasse += tabelle.getFahrzeugKlassen().get(1);
        testFahrzeugKlasse += tabelle.getFahrzeugKlassen().get(2);
        Assertions.assertTrue(testFahrzeugKlasse.contains(pkw));
        Assertions.assertTrue(testFahrzeugKlasse.contains(pickup));
        Assertions.assertTrue(testFahrzeugKlasse.contains(suv));
        Assertions.assertEquals(0.5,tabelle.getFestpreis());
        Assertions.assertEquals("0.50€ mal Fahrzeugmultiplikator mal (Preisklasse + 1)",tabelle.getFestpreisString());
        List<Double> testPreise = tabelle.getPreise();
        double ortsmultiplikator = parkhausServiceSession.getPreisklasse()+1;
        Assertions.assertTrue(testPreise.contains(1.0*ortsmultiplikator));
        Assertions.assertTrue(testPreise.contains(1.2*ortsmultiplikator));
        Assertions.assertTrue(testPreise.contains(1.5*ortsmultiplikator));
        Assertions.assertEquals(parkhausServiceSession.getPreisklasse()+1,tabelle.getOrtsmultiplikator());

    }


    /**
     *
     * Erzeugt im aktuellen Parkhaus eine variable Anzahl an Ebenen mit Default-Parametern. Die Ebenen werden
     * mit "Generierte Ebene Nr. x" bezeichnet
     * ruft private ParkhausEbeneConfigDTO[] generiereConfigDTOs(int anzahl) auf
     *
     * @params anzahl: Die Anzahl der generierten Ebenen
     *
     * @author Alexander Bohl
     */
    private ParkhausEbene[] generiereEbenen(int anzahl) {
        ParkhausEbene[] ebenen = new ParkhausEbene[anzahl];
        ParkhausEbeneConfigDTO[] configs = generiereConfigDTOs(anzahl);
        for (int i = 0; i < anzahl; i++) {
            ebenen[i] = parkhausServiceSession.initEbene(configs[i]);
        }
        return ebenen;
    }

    /**
     *
     * Erzeugt im aktuellen Parkhaus eine variable Anzahl an Ebenen mit Default-Parametern. Die Ebenen werden
     * mit "Generierte Ebene Nr. x" x Element aus N0 bezeichnet
     *
     * @param anzahl: Die Anzahl der zu generierenden Ebenen
     *
     * @return configs: ein Array an Configs
     */
    private ParkhausEbeneConfigDTO[] generiereConfigDTOs(int anzahl) {
        ParkhausEbeneConfigDTO[] configs = new ParkhausEbeneConfigDTO[anzahl];
        for (int i = 0; i < anzahl; i++) {
            configs[i] = new ParkhausEbeneConfigDTO("Generierte Ebene Nr. "+i, 120, 6,24,0,0, 5, fahrzeugPreise, null);
        }
        return configs;
    }

}
