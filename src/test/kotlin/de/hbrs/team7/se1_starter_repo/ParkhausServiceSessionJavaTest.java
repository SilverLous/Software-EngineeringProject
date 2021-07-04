package de.hbrs.team7.se1_starter_repo;

import de.hbrs.team7.se1_starter_repo.dto.ParkhausEbeneConfigDTO;
import de.hbrs.team7.se1_starter_repo.dto.ParkhausServletPostDto;
import de.hbrs.team7.se1_starter_repo.entities.Auto;
import de.hbrs.team7.se1_starter_repo.entities.ParkhausEbene;
import de.hbrs.team7.se1_starter_repo.entities.Ticket;
import de.hbrs.team7.se1_starter_repo.services.DatabaseServiceGlobal;
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

/*    @WeldSetup
    public WeldInitiator weld = WeldInitiator.from(ParkhausServiceSession.class, ParkhausServiceGlobal.class)
            .activate(SessionScoped.class, ApplicationScoped.class ).build();*/

    @Inject
    BoundSessionContext sessionContext;

    @Inject
    ParkhausServiceSession parkhausServiceSession;

    @Inject
    ParkhausServiceGlobal parkhausServiceGlobal;


    String etage = "test";
    SecureRandom zufallszahlengenerator;

    HashMap<String, Double> fahrzeugPreise = new HashMap<>();
    String eingabeHash = "echterHash";
    String eingabeFarbe = "REGENBOGEN";
    String eingabeClientCategory = "Family";
    String eingabeType = "SUV";
    String eingabeKennzeichen = "Y-123 456";


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
        //List<ParkhausEbene> temp = parkhausServiceSession.getParkhausEbenen();
        parkhausServiceSession.initEbene(conf[1]);
        Assertions.assertEquals(2, parkhausServiceGlobal.getEbenenSet().size());
        sessionContext.invalidate();
        String zweiterStadtName = parkhausServiceSession.getParkhaus().getStadtname();
        Assertions.assertNotEquals(stadtName, zweiterStadtName);
        //Assertions.assertEquals(2, parkhausServiceGlobal.getLevelSet().size());
        //Assertions.assertEquals(2,parkhausServiceSession.getParkhausEbenen().size());
        //Assertions.assertEquals(3,parkhausServiceSession.getParkhausEbenen().size());

    }

    @Test
    public void testFindTicketByPlace() {
        ParkhausEbene[] ebenen = generiereEbenen(1);
        Long timeNow = System.currentTimeMillis();
        ParkhausServletPostDto paramsErstesAuto = new ParkhausServletPostDto(2, timeNow, 0,
                0, eingabeHash, eingabeFarbe, 1, eingabeClientCategory, eingabeType, eingabeKennzeichen);
        Ticket erstesTestTicket = parkhausServiceSession.erstelleTicket(ebenen[0].getName(), paramsErstesAuto);
        Ticket zweiterVerweisTicket = parkhausServiceSession.findeTicketUeberParkplatz(ebenen[0].getName(), erstesTestTicket.getAuto().getPlatznummer());
        Assertions.assertEquals(erstesTestTicket, zweiterVerweisTicket);
    }

    @Test
    public void testGetSum() {
        ParkhausEbene[] ebenen = generiereEbenen(1);
        int wieLange = 8;
        Ticket t_test;
        int price = 0;

        for (int i=0;i<wieLange;i++){
            Long timeNow = System.currentTimeMillis();
            ParkhausServletPostDto paramsErstesAuto = new ParkhausServletPostDto(2, timeNow, 0,
                    0, eingabeHash+i, eingabeFarbe, 1, eingabeClientCategory, "kategorie1", eingabeKennzeichen);
            t_test = parkhausServiceSession.erstelleTicket(ebenen[0].getName(),paramsErstesAuto);
            parkhausServiceSession.ticketBezahlen(ebenen[0].getName(),t_test,new Date(timeNow));
            price+=t_test.getPrice();

            Assertions.assertEquals(price,parkhausServiceSession.getSummeTicketpreiseUeberAutos(ebenen[0].getName()));
        }

    }

    @Test
    public void StatisticUpdateSubjTest() {
        AtomicInteger events = new AtomicInteger();
        this.parkhausServiceGlobal.getStatisticUpdateSubj().subscribe(
                s -> events.getAndIncrement()
        );

        testGetSum();

        assertTrue(events.get() > 0);

    }

    @Test
    public void testGetAverage() {
        ParkhausEbene[] ebenen = generiereEbenen(1);
        int wieLange = 8;
        Ticket t_test;
        double price = 0.0;
        for (int i = 0; i < wieLange; i++) {
            Long timeNow = System.currentTimeMillis();
            ParkhausServletPostDto paramsErstesAuto = new ParkhausServletPostDto(2, timeNow, 0,
                    0, eingabeHash + i, eingabeFarbe, 1, eingabeClientCategory, eingabeType, eingabeKennzeichen);
            t_test = parkhausServiceSession.erstelleTicket(ebenen[0].getName(), paramsErstesAuto);
            parkhausServiceSession.ticketBezahlen(ebenen[0].getName(), t_test, new Date(timeNow + 100));
            price+=t_test.getPrice();
            Assertions.assertEquals(price/(i+1), parkhausServiceSession.getDurchschnittUeberAutos(ebenen[0].getName()), 10);

        }
    }


    @Test
    public void testGetTotalUsers() {
        ParkhausEbene[] ebenen = generiereEbenen(1);
        int wieLange = 8;
        Ticket t_test = new Ticket();

        for (int i=0;i<wieLange;i++){
            Long timeNow = System.currentTimeMillis();
            ParkhausServletPostDto paramsErstesAuto = new ParkhausServletPostDto(2, timeNow, 0,
                    0, eingabeHash+i, eingabeFarbe, 1, eingabeClientCategory, eingabeType, eingabeKennzeichen);
            parkhausServiceSession.erstelleTicket(ebenen[0].getName(),paramsErstesAuto);
            t_test.getAuto();
            Assertions.assertEquals(i+1,parkhausServiceSession.getAlleUser(ebenen[0].getName()));
        }

    }

    @Test
    @DisplayName("Test der addCar-Funktion")
    public void testAddCar() {
        ParkhausEbene[] ebenen = generiereEbenen(1);
        int wieLange = 8;
        Auto a_test;
        Ticket t_test;

        for (int i=0;i<wieLange;i++){
            Long timeNow = System.currentTimeMillis();
            ParkhausServletPostDto paramsErstesAuto = new ParkhausServletPostDto(2, timeNow, 0,
                    0, eingabeHash+i, eingabeFarbe, 1, eingabeClientCategory, eingabeType, eingabeKennzeichen);
            t_test = parkhausServiceSession.erstelleTicket(ebenen[0].getName(),paramsErstesAuto);
            a_test = t_test.getAuto();
            Assertions.assertEquals(eingabeHash+i,a_test.getHash());
        }

    }

    @Test
    @DisplayName("Test der addCar-Funktion")
    public void testAllCars() {
        ParkhausEbene[] ebenen = generiereEbenen(1);
        int wieLange = 8;
        Auto a_test;
        Ticket t_test;

        for (int i=0;i<wieLange;i++){
            Long timeNow = System.currentTimeMillis();
            ParkhausServletPostDto paramsErstesAuto = new ParkhausServletPostDto(2, timeNow, 0,
                    0, eingabeHash+i, eingabeFarbe, 1, eingabeClientCategory, eingabeType, eingabeKennzeichen);
            t_test = parkhausServiceSession.erstelleTicket(ebenen[0].getName(),paramsErstesAuto);
            a_test = t_test.getAuto();
            Assertions.assertEquals(a_test.getHash(),parkhausServiceSession.getAutosInParkEbene(ebenen[0].getName(),true).get(i).getHash());
        }

    }

    @Test
    @DisplayName("Test der autosInParkEbene-Funktion")
    public void testGetCarsInEbene() {
        ParkhausEbene[] ebenen = generiereEbenen(1);
        int wieLange = 8;
        Ticket t_test;

        for (int i=0;i<wieLange;i++){
            Long timeNow = System.currentTimeMillis();
            ParkhausServletPostDto paramsErstesAuto = new ParkhausServletPostDto(2+i, timeNow, 0,
                    0, eingabeHash+i, eingabeFarbe, 1+i, eingabeClientCategory, eingabeType, eingabeKennzeichen);
            t_test = parkhausServiceSession.erstelleTicket(ebenen[0].getName(),paramsErstesAuto);
            t_test.getAuto();
            Assertions.assertEquals(1,parkhausServiceSession.getAutosInParkEbene(ebenen[0].getName(), true).size());
            Assertions.assertEquals(i+0,parkhausServiceSession.getAutosInParkEbene(ebenen[0].getName(),false).size());
            parkhausServiceSession.ticketBezahlen(ebenen[0].getName(),t_test,Date.from(Instant.now()));
            Assertions.assertEquals(0,parkhausServiceSession.getAutosInParkEbene(ebenen[0].getName(), true).size());
            Assertions.assertEquals(i+1,parkhausServiceSession.getAutosInParkEbene(ebenen[0].getName(),false).size());
        }

    }

    @Test
    @DisplayName("Test der generateStatisticsOverVehicle-Funktion")
    public void testGenerateStatisticsOverVehicle() {
        ParkhausEbene[] ebenen = generiereEbenen(1);/*
        int wieLange = 8;
        Auto a_test;
        Ticket t_test;

        for (int i=0;i<wieLange;i++){
            Long timeNow = System.currentTimeMillis();
            ParkhausServletPostDto paramsErstesAuto = new ParkhausServletPostDto(2, timeNow, 0,
                    0, eingabeHash+i, eingabeFarbe, 1, eingabeClientCategory, eingabeType, eingabeKennzeichen);
            t_test = parkhausServiceSession.erstelleTicket(ebenen[0].getName(),paramsErstesAuto);
            a_test = t_test.getAuto();
            Assertions.assertEquals(a_test.getHash(),parkhausServiceSession.getAutosInParkEbene(ebenen[0].getName(),true).get(i).getHash());
        }
        */testAllCars();

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
        Ticket t_test = new Ticket();

        for (int i=0;i<wieLange;i++){
            Long timeNow = System.currentTimeMillis();
            ParkhausServletPostDto paramsErstesAuto = new ParkhausServletPostDto(2, timeNow, 0,
                    0, eingabeHash+i, eingabeFarbe, 1, eingabeClientCategory, eingabeType, eingabeKennzeichen);
            t_test = parkhausServiceSession.erstelleTicket(ebenen[0].getName(),paramsErstesAuto);
            t_test.getAuto();
        }
        parkhausServiceSession.ticketBezahlen(ebenen[0].getName(),t_test,Date.from(Instant.now()));
        parkhausServiceSession.undo();
        Assertions.assertEquals(wieLange,parkhausServiceSession.getAutosInParkEbene(ebenen[0].getName(), true).size());
        parkhausServiceSession.undo();
        Assertions.assertEquals(wieLange-1,parkhausServiceSession.getAutosInParkEbene(ebenen[0].getName(), true).size());
    }

    @Test
    @DisplayName("Test der redo Funktion")
    @Disabled
    public void testRedo(){
        DatabaseServiceGlobal dataBase = new DatabaseServiceGlobal();
        testUndo();
        String ParkhausName = "Generierte Ebene Nr. 0";
        Long ParkId = parkhausServiceSession.getIdUeberName(ParkhausName);
        List<Auto> autoliste = parkhausServiceSession.getAutosInParkEbene(ParkhausName, true);

        parkhausServiceSession.redo();
        Assertions.assertEquals(autoliste.size()+1,parkhausServiceSession.getAutosInParkEbene(ParkhausName, true).size());
        parkhausServiceSession.redo();
        List<Auto> autolisteAfterRedo = dataBase.autosInParkEbeneHistoric(ParkId);
        Assertions.assertEquals(autoliste.size(),parkhausServiceSession.getAutosInParkEbene(ParkhausName, true).size());
        parkhausServiceSession.undo();
        parkhausServiceSession.redo();
        Assertions.assertEquals(autoliste.size(),parkhausServiceSession.getAutosInParkEbene(ParkhausName, true).size());
        parkhausServiceSession.undo();
        Assertions.assertEquals(autoliste.size()+1,parkhausServiceSession.getAutosInParkEbene(ParkhausName, true).size());
        parkhausServiceSession.undo();
        Assertions.assertEquals(autoliste.size(),parkhausServiceSession.getAutosInParkEbene(ParkhausName, true).size());
        parkhausServiceSession.undo();
        parkhausServiceSession.redo();
        parkhausServiceSession.redo();
        parkhausServiceSession.redo();
        Assertions.assertEquals(autoliste.size(),parkhausServiceSession.getAutosInParkEbene(ParkhausName, true).size());

    }


    @Nested
    @DisplayName("Basic IO chain")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class basicIOTest {
        String parkhausId = "Test Ebene";

        Long timeNow = System.currentTimeMillis();
        ParkhausServletPostDto paramsErstesAuto = new ParkhausServletPostDto(2, timeNow, 0,
                0, eingabeHash, eingabeFarbe, 1, eingabeClientCategory, eingabeType, eingabeClientCategory);

        @Test
        @Order(1)
        @DisplayName("Testen der addCar Funktion")
        public void zieheTicketTest() {
            ParkhausEbene[] ebenen = generiereEbenen(1);

            Ticket erstesTestTicket = parkhausServiceSession.erstelleTicket(ebenen[0].getName(), paramsErstesAuto);
            assertNotNull(erstesTestTicket);
        }

        public void bezahleTicketTest() {
            ParkhausEbene[] ebenen = generiereEbenen(1);

            Ticket erstesTestTicket = parkhausServiceSession.erstelleTicket(ebenen[0].getName(), paramsErstesAuto);
            Date timeCheckOut = new Date(erstesTestTicket.getAusstellungsdatum().getTime() + 100);
            Long preisInCent = parkhausServiceSession.ticketBezahlen(ebenen[0].getName(), erstesTestTicket, timeCheckOut);
            assert preisInCent > 0;
            assert preisInCent == 1;
        }
    }

    @Test
    @DisplayName("Testen der wechsleEbeneMaxParkplätze-Funktion")
    public void testWechsleEbeneMaxParkplätze() {
        ParkhausEbene[] ebenen = generiereEbenen(2);
        ParkhausEbene ebene = ebenen[0];
        ParkhausEbene ebene2 = ebenen[1];
        Assertions.assertEquals(120, ebene.getMaxPlätze());
        int neuePlaetze = zufallszahlengenerator.nextInt(19);
        neuePlaetze++;
        parkhausServiceSession.wechsleEbeneMaxParkplätze(ebene.getName(),12,neuePlaetze);
        Assertions.assertEquals(neuePlaetze, ebene.getMaxPlätze());
        Assertions.assertEquals(120, ebene2.getMaxPlätze());
    }

    @Test
    @DisplayName("Testen der wechsleEbeneÖffnungszeit-Funktion")
    public void testWechsleEbeneÖffnungszeit() {
        ParkhausEbene[] ebenen = generiereEbenen(2);
        ParkhausEbene ebene = ebenen[0];
        ParkhausEbene ebene2 = ebenen[1];
        Assertions.assertEquals(6,ebene.getöffnungszeit());
        Assertions.assertEquals(6,ebene2.getöffnungszeit());
        int neueOeffnungszeit = zufallszahlengenerator.nextInt(22);
        neueOeffnungszeit++;
        parkhausServiceSession.wechsleEbeneÖffnungszeit(ebene.getName(),6,neueOeffnungszeit);
        Assertions.assertEquals(neueOeffnungszeit,ebene.getöffnungszeit());
        Assertions.assertEquals(6,ebene2.getöffnungszeit());
    }

    @Test
    @DisplayName("Test von vernuenftigen Preis Funktion")
    public void testNeuePreisFunktion() {
        ParkhausEbene ebene = generiereEbenen(1)[0];
        int wieOft = 8;
        Auto a_test;
        Ticket t_test;
        Long timeNow = System.currentTimeMillis();
        ParkhausServletPostDto paramsErstesAuto = new ParkhausServletPostDto(2, timeNow, 0,
                0, eingabeHash, eingabeFarbe, 1, eingabeClientCategory, "bitteMult1", eingabeKennzeichen);
        t_test = parkhausServiceSession.erstelleTicket(ebene.getName(),paramsErstesAuto);

        for (int i=0;i<wieOft;i++){
            parkhausServiceSession.ticketBezahlen(ebene.getName(),t_test,Date.from(Instant.ofEpochMilli(timeNow+1800000*(1+i))));
            Assertions.assertEquals(100*(i+1) + 50 * ebene.getParkhaus().getPreisklasse(),t_test.getPrice());
            parkhausServiceSession.undo();
        }
    }

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

    private ParkhausEbene[] generiereEbenen(int anzahl) {
        ParkhausEbene[] ebenen = new ParkhausEbene[anzahl];
        ParkhausEbeneConfigDTO[] configs = generiereConfigDTOs(anzahl);
        for (int i = 0; i < anzahl; i++) {
            ebenen[i] = parkhausServiceSession.initEbene(configs[i]);
        }
        return ebenen;
    }

    private ParkhausEbeneConfigDTO[] generiereConfigDTOs(int anzahl) {
        ParkhausEbeneConfigDTO[] configs = new ParkhausEbeneConfigDTO[anzahl];
        for (int i = 0; i < anzahl; i++) {
            configs[i] = new ParkhausEbeneConfigDTO("Generierte Ebene Nr. "+i, 120, 6,24,0,5, fahrzeugPreise, null);
        }
        return configs;
    }

    private Auto generiereDefaultAuto() {
        return new Auto( "EchterHashEcht","REGENBOGEN",12,"y-232" ,"vehilkulaer","kategorisch");
    }

}
