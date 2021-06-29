package de.hbrs.team7.se1_starter_repo;

import de.hbrs.team7.se1_starter_repo.dto.ParkhausEbeneConfigDTO;
import de.hbrs.team7.se1_starter_repo.dto.ParkhausServletPostDto;
import de.hbrs.team7.se1_starter_repo.entities.Auto;
import de.hbrs.team7.se1_starter_repo.entities.ParkhausEbene;
import de.hbrs.team7.se1_starter_repo.entities.Ticket;
import de.hbrs.team7.se1_starter_repo.services.DatabaseServiceGlobal;
import de.hbrs.team7.se1_starter_repo.services.LoggerServiceGlobal;
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

import java.time.Instant;
import java.util.Date;
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

    @BeforeEach
    public void setup() {
        // sessionContext.invalidate();
        // parkhausServiceSession = new ParkhausServiceSession();
    }

    @Test
    public void sessionInitTest() {
        assertNotNull(parkhausServiceSession.getParkhaus().getStadtname());
    }

    @Test
    public void sessionAddLevel() {

        ParkhausEbeneConfigDTO conf = new ParkhausEbeneConfigDTO("Generierte Ebene Nr. 1", 12, 6,24,0,5, null);
        parkhausServiceSession.initEbene(conf);
        List<ParkhausEbene> temp = parkhausServiceSession.getParkhausEbenen();
        assertNotNull(temp);
        Assertions.assertEquals("Generierte Ebene Nr. 1", parkhausServiceSession.getEbeneUeberId(temp.get(0).getId()).getName());
        ParkhausEbeneConfigDTO conf2 = new ParkhausEbeneConfigDTO("Generierte Ebene Nr. 2", 12, 6,24,0,5, null);
        parkhausServiceSession.initEbene(conf2);
        Assertions.assertEquals("Generierte Ebene Nr. 2", parkhausServiceSession.getEbeneUeberId(temp.get(1).getId()).getName());
        Assertions.assertEquals(2, parkhausServiceSession.getParkhausEbenen().size());
        Assertions.assertEquals(2, parkhausServiceGlobal.getEbenenSet().size());

    }

    @Test
    public void sessionAddLevelInvalidateTest() {

        int i = 1;
        ParkhausEbeneConfigDTO conf = new ParkhausEbeneConfigDTO("Generierte Ebene Nr. " + i++, 12, 6,24,0,5, null);

        parkhausServiceSession.initEbene(conf);
        String stadtName = parkhausServiceSession.getParkhaus().getStadtname();
        List<ParkhausEbene> temp = parkhausServiceSession.getParkhausEbenen();
        ParkhausEbeneConfigDTO conf2 = new ParkhausEbeneConfigDTO("Generierte Ebene Nr. " + i++, 12, 6,24,0,5, null);
        parkhausServiceSession.initEbene(conf2);
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
        ParkhausEbene[] ebenen = generateEbenen(1);
        Long timeNow = System.currentTimeMillis();
        ParkhausServletPostDto paramsErstesAuto = new ParkhausServletPostDto(2, timeNow, 0,
                0, "echterHash", "REGENBOGEN", 1, "Family", "SUV", "Y-123 456");
        Ticket erstesTestTicket = parkhausServiceSession.erstelleTicket(ebenen[0].getName(), paramsErstesAuto);
        Ticket zweiterVerweisTicket = parkhausServiceSession.findeTicketUeberParkplatz(ebenen[0].getName(), erstesTestTicket.getAuto().getPlatznummer());
        Assertions.assertEquals(erstesTestTicket, zweiterVerweisTicket);
    }

    @Test
    public void testGetSum() {
        ParkhausEbene[] ebenen = generateEbenen(1);
        int wieLange = 8;
        Auto a_test = new Auto( "EchterHashEcht","REGENBOGEN",12,"y-232","vehilkulaer","kategorisch" );
        Ticket t_test = new Ticket();

        for (int i=0;i<wieLange;i++){
            Long timeNow = System.currentTimeMillis();
            ParkhausServletPostDto paramsErstesAuto = new ParkhausServletPostDto(2, timeNow, 0,
                    0, "echterHash"+i, "REGENBOGEN", 1, "Family", "SUV", "Y-123 456");
            t_test = parkhausServiceSession.erstelleTicket(ebenen[0].getName(),paramsErstesAuto);
            parkhausServiceSession.ticketBezahlen(ebenen[0].getName(),t_test,new Date(timeNow + 100));
            Assertions.assertEquals((i+1)*100,parkhausServiceSession.getSummeTicketpreiseUeberAutos(ebenen[0].getName()),4*(i+1));
        }

    }

    @Test
    public void StatisticUpdateSubjTest() {

        // List<List<ManagerStatistikUpdateDTO>> l = Collections.emptyList();
        AtomicInteger events = new AtomicInteger();
        this.parkhausServiceGlobal.getStatisticUpdateSubj().subscribe(
                s -> events.getAndIncrement()
        );
        testGetSum();

        assertTrue(events.get() > 0);

    }

    @Test
    public void testGetAverage() {
        ParkhausEbene[] ebenen = generateEbenen(1);
        int wieLange = 8;
        Auto a_test = new Auto("EchterHashEcht", "REGENBOGEN", 12, "y-232","vehilkulaer","kategorisch");
        Ticket t_test = new Ticket();

        for (int i = 0; i < wieLange; i++) {
            Long timeNow = System.currentTimeMillis();
            ParkhausServletPostDto paramsErstesAuto = new ParkhausServletPostDto(2, timeNow, 0,
                    0, "echterHash" + i, "REGENBOGEN", 1, "Family", "SUV", "Y-123 456");
            t_test = parkhausServiceSession.erstelleTicket(ebenen[0].getName(), paramsErstesAuto);
            parkhausServiceSession.ticketBezahlen(ebenen[0].getName(), t_test, new Date(timeNow + 100));
            Assertions.assertEquals(100, parkhausServiceSession.getDurchschnittUeberAutos(ebenen[0].getName()), 4);
            Assertions.assertEquals((int)(parkhausServiceSession.getSummeTicketpreiseUeberAutos(ebenen[0].getName())/parkhausServiceSession.getAlleUser(ebenen[0].getName())), parkhausServiceSession.getDurchschnittUeberAutos(ebenen[0].getName()), 4);
        }
    }


    @Test
    public void testGetTotalUsers() {
        ParkhausEbene[] ebenen = generateEbenen(1);
        int wieLange = 8;
        Auto a_test = new Auto( "EchterHashEcht","REGENBOGEN",12,"y-232" ,"vehilkulaer","kategorisch");
        Ticket t_test = new Ticket();

        for (int i=0;i<wieLange;i++){
            Long timeNow = System.currentTimeMillis();
            ParkhausServletPostDto paramsErstesAuto = new ParkhausServletPostDto(2, timeNow, 0,
                    0, "echterHash"+i, "REGENBOGEN", 1, "Family", "SUV", "Y-123 456");
            t_test = parkhausServiceSession.erstelleTicket(ebenen[0].getName(),paramsErstesAuto);

            a_test = t_test.getAuto();
            Assertions.assertEquals(i+1,parkhausServiceSession.getAlleUser(ebenen[0].getName()));
        }

    }

    @Test
    @DisplayName("Test der addCar-Funktion")
    public void testAddCar() {
        ParkhausEbene[] ebenen = generateEbenen(1);
        int wieLange = 8;
        Auto a_test = new Auto( "EchterHashEcht","REGENBOGEN",12,"y-232" ,"vehilkulaer","kategorisch");
        Ticket t_test = new Ticket();

        for (int i=0;i<wieLange;i++){
            Long timeNow = System.currentTimeMillis();
            ParkhausServletPostDto paramsErstesAuto = new ParkhausServletPostDto(2, timeNow, 0,
                    0, "echterHash"+i, "REGENBOGEN", 1, "Family", "SUV", "Y-123 456");
            t_test = parkhausServiceSession.erstelleTicket(ebenen[0].getName(),paramsErstesAuto);
            a_test = t_test.getAuto();
            Assertions.assertEquals("echterHash"+i,a_test.getHash());
        }

    }

    @Test
    @DisplayName("Test der addCar-Funktion")
    public void testAllCars() {
        ParkhausEbene[] ebenen = generateEbenen(1);
        int wieLange = 8;
        Auto a_test = new Auto( "EchterHashEcht","REGENBOGEN",12,"y-232" ,"vehilkulaer","kategorisch");
        Ticket t_test = new Ticket();

        for (int i=0;i<wieLange;i++){
            Long timeNow = System.currentTimeMillis();
            ParkhausServletPostDto paramsErstesAuto = new ParkhausServletPostDto(2, timeNow, 0,
                    0, "echterHash"+i, "REGENBOGEN", 1, "Family", "SUV", "Y-123 456");
            t_test = parkhausServiceSession.erstelleTicket(ebenen[0].getName(),paramsErstesAuto);
            a_test = t_test.getAuto();
            Assertions.assertEquals(a_test.getHash(),parkhausServiceSession.getAutosInParkEbene(ebenen[0].getName(),true).get(i).getHash());
        }

    }

    @Test
    @DisplayName("Test der autosInParkEbene-Funktion")
    public void testGetCarsInEbene() {
        ParkhausEbene[] ebenen = generateEbenen(1);
        int wieLange = 8;
        Auto a_test = new Auto( "EchterHashEcht","REGENBOGEN",12,"y-232" ,"vehilkulaer","kategorisch");
        Ticket t_test = new Ticket();

        for (int i=0;i<wieLange;i++){
            Long timeNow = System.currentTimeMillis();
            ParkhausServletPostDto paramsErstesAuto = new ParkhausServletPostDto(2+i, timeNow, 0,
                    0, "echterHash"+i, "REGENBOGEN", 1+i, "Family", "SUV", "Y-123 456");
            t_test = parkhausServiceSession.erstelleTicket(ebenen[0].getName(),paramsErstesAuto);
            a_test = t_test.getAuto();
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
        ParkhausEbene[] ebenen = generateEbenen(1);
        int wieLange = 8;
        Auto a_test = new Auto( "EchterHashEcht","REGENBOGEN",12,"y-232" ,"vehilkulaer","kategorisch");
        Ticket t_test = new Ticket();

        for (int i=0;i<wieLange;i++){
            Long timeNow = System.currentTimeMillis();
            ParkhausServletPostDto paramsErstesAuto = new ParkhausServletPostDto(2, timeNow, 0,
                    0, "echterHash"+i, "REGENBOGEN", 1, "Family", "SUV", "Y-123 456");
            t_test = parkhausServiceSession.erstelleTicket(ebenen[0].getName(),paramsErstesAuto);
            a_test = t_test.getAuto();
            Assertions.assertEquals(a_test.getHash(),parkhausServiceSession.getAutosInParkEbene(ebenen[0].getName(),true).get(i).getHash());
        }
        assertNotNull(parkhausServiceSession.erstelleStatistikenUeberFahrzeuge(ebenen[0].getName()));
    }

    @Test
    @DisplayName("Test der undo Funktion")
    public void testUndo(){
        ParkhausEbene[] ebenen = generateEbenen(1);
        int wieLange = 8;
        Auto a_test = new Auto( "EchterHashEcht","REGENBOGEN",12,"y-232" ,"vehilkulaer","kategorisch");
        Ticket t_test = new Ticket();

        for (int i=0;i<wieLange;i++){
            Long timeNow = System.currentTimeMillis();
            ParkhausServletPostDto paramsErstesAuto = new ParkhausServletPostDto(2, timeNow, 0,
                    0, "echterHash"+i, "REGENBOGEN", 1, "Family", "SUV", "Y-123 456");
            t_test = parkhausServiceSession.erstelleTicket(ebenen[0].getName(),paramsErstesAuto);
            a_test = t_test.getAuto();
        }
        parkhausServiceSession.ticketBezahlen(ebenen[0].getName(),t_test,Date.from(Instant.now()));
        parkhausServiceSession.undo();
        Assertions.assertEquals(wieLange,parkhausServiceSession.getAutosInParkEbene(ebenen[0].getName(), true).size());
        parkhausServiceSession.undo();
        Assertions.assertEquals(wieLange-1,parkhausServiceSession.getAutosInParkEbene(ebenen[0].getName(), true).size());
    }
    @Test
    @DisplayName("Test der redo Funktion")
    public void testRedo(){
        testUndo();
        String ParkhausName = "Generierte Ebene Nr. 0";
        int anzahlAutosInParkEbene = parkhausServiceSession.getAutosInParkEbene(ParkhausName, true).size();
        parkhausServiceSession.redo();
        Assertions.assertEquals(anzahlAutosInParkEbene+1,parkhausServiceSession.getAutosInParkEbene(ParkhausName, true).size());
        parkhausServiceSession.redo();
        Assertions.assertEquals(anzahlAutosInParkEbene,parkhausServiceSession.getAutosInParkEbene(ParkhausName, true).size());

    }


    @Nested
    @DisplayName("Basic IO chain")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class basicIOTest {
        String parkhausId = "Test Ebene";

        Long timeNow = System.currentTimeMillis();
        ParkhausServletPostDto paramsErstesAuto = new ParkhausServletPostDto(2, timeNow, 0,
                0, "echterHash", "REGENBOGEN", 1, "Family", "SUV", "Y-123 456");

        @Test
        @Order(1)
        @DisplayName("Testen der addCar Funktion")
        public void zieheTicketTest() {
            ParkhausEbene[] ebenen = generateEbenen(1);

            Ticket erstesTestTicket = parkhausServiceSession.erstelleTicket(ebenen[0].getName(), paramsErstesAuto);
            assertNotNull(erstesTestTicket);
        }

        public void bezahleTicketTest() {
            ParkhausEbene[] ebenen = generateEbenen(1);

            Ticket erstesTestTicket = parkhausServiceSession.erstelleTicket(ebenen[0].getName(), paramsErstesAuto);
            Date timeCheckOut = new Date(erstesTestTicket.getAusstellungsdatum().getTime() + 100);
            Long preisInCent = parkhausServiceSession.ticketBezahlen(ebenen[0].getName(), erstesTestTicket, timeCheckOut);
            assert preisInCent > 0;
            assert preisInCent == 1;
        }
    }

    private ParkhausEbene[] generateEbenen(int anzahl) {
        ParkhausEbene[] ebenen = new ParkhausEbene[anzahl];
        for (int i = 0; i < anzahl; i++) {
            //ebenen[i] = parkhausServiceSession.initEbene("Generierte Ebene Nr. ".concat(String.valueOf(i)));
            ebenen[i] = parkhausServiceSession.initEbene(new ParkhausEbeneConfigDTO("Generierte Ebene Nr. "+i, 12, 6,24,0,5, null));
        }
        return ebenen;
    }

}
