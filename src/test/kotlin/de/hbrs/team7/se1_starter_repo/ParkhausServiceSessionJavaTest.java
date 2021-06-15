package de.hbrs.team7.se1_starter_repo;

import de.hbrs.team7.se1_starter_repo.dto.ParkhausServletPostDto;
import de.hbrs.team7.se1_starter_repo.entities.Auto;
import de.hbrs.team7.se1_starter_repo.entities.Parkhaus;
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

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;


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
        parkhausServiceSession.initEbene("TESTEBENE1");
        List<ParkhausEbene> temp = parkhausServiceSession.getParkhausEbenen();
        assertNotNull(temp);
        Assertions.assertEquals("TESTEBENE1", parkhausServiceSession.getLevelById(temp.get(0).getId()).getName());
        parkhausServiceSession.initEbene("TESTEBENE2");
        Assertions.assertEquals("TESTEBENE2", parkhausServiceSession.getLevelById(temp.get(1).getId()).getName());
        Assertions.assertEquals(2, parkhausServiceSession.getParkhausEbenen().size());
        Assertions.assertEquals(2, parkhausServiceGlobal.getLevelSet().size());

    }

    @Test
    public void sessionAddLevelInvalidateTest() {
        parkhausServiceSession.initEbene("TESTEBENE1");
        String stadtName = parkhausServiceSession.getParkhaus().getStadtname();
        List<ParkhausEbene> temp = parkhausServiceSession.getParkhausEbenen();
        parkhausServiceSession.initEbene("TESTEBENE2");
        Assertions.assertEquals(2, parkhausServiceGlobal.getLevelSet().size());
        sessionContext.invalidate();
        String zweiterStadtName = parkhausServiceSession.getParkhaus().getStadtname();
        Assertions.assertNotEquals(stadtName, zweiterStadtName);
        //Assertions.assertEquals(2, parkhausServiceGlobal.getLevelSet().size());
        //Assertions.assertEquals(2,parkhausServiceSession.getParkhausEbenen().size());
        //Assertions.assertEquals(3,parkhausServiceSession.getParkhausEbenen().size());

    }

    @Test
    public void testFindTicketByPlace() {
        String parkhausId = "Test Ebene";
        parkhausServiceSession.initEbene(parkhausId);

        Long timeNow = System.currentTimeMillis();
        ParkhausServletPostDto paramsErstesAuto = new ParkhausServletPostDto(2, timeNow, 0,
                0, "echterHash", "REGENBOGEN", 1, "Family", "SUV", "Y-123 456");
        Ticket erstesTestTicket = parkhausServiceSession.generateTicket(parkhausId, paramsErstesAuto);
        Ticket zweiterVerweisTicket = parkhausServiceSession.findTicketByPlace(parkhausId, erstesTestTicket.getAuto().getPlatznummer());
        Assertions.assertEquals(erstesTestTicket, zweiterVerweisTicket);
    }

    @Test
    public void testGetSum() {
        String parkhausName = "ForLoopsSindToll";
        parkhausServiceSession.initEbene(parkhausName);
        int wieLange = 8;
        Auto a_test = new Auto( "EchterHashEcht","REGENBOGEN",12,"y-232","vehilkulaer","kategorisch" );
        Ticket t_test = new Ticket();

        for (int i=0;i<wieLange;i++){
            Long timeNow = System.currentTimeMillis();
            ParkhausServletPostDto paramsErstesAuto = new ParkhausServletPostDto(2, timeNow, 0,
                    0, "echterHash"+i, "REGENBOGEN", 1, "Family", "SUV", "Y-123 456");
            t_test = parkhausServiceSession.generateTicket(parkhausName,paramsErstesAuto);
            parkhausServiceSession.payForTicket(parkhausName,t_test,new Date(timeNow + 100));
            Assertions.assertEquals((i+1)*100,parkhausServiceSession.sumOverCars(parkhausName),4*(i+1));
        }

    }

    @Test
    public void testGetAverage() {
        String parkhausName = "ForLoopsSindToll";
        parkhausServiceSession.initEbene(parkhausName);
        int wieLange = 8;
        Auto a_test = new Auto("EchterHashEcht", "REGENBOGEN", 12, "y-232","vehilkulaer","kategorisch");
        Ticket t_test = new Ticket();

        for (int i = 0; i < wieLange; i++) {
            Long timeNow = System.currentTimeMillis();
            ParkhausServletPostDto paramsErstesAuto = new ParkhausServletPostDto(2, timeNow, 0,
                    0, "echterHash" + i, "REGENBOGEN", 1, "Family", "SUV", "Y-123 456");
            t_test = parkhausServiceSession.generateTicket(parkhausName, paramsErstesAuto);
            parkhausServiceSession.payForTicket(parkhausName, t_test, new Date(timeNow + 100));
            Assertions.assertEquals(100, parkhausServiceSession.averageOverCars(parkhausName), 4);
            Assertions.assertEquals((int)(parkhausServiceSession.sumOverCars(parkhausName)/parkhausServiceSession.getTotalUsers(parkhausName)), parkhausServiceSession.averageOverCars(parkhausName), 4);
        }
    }


    @Test
    public void testGetTotalUsers() {
        parkhausServiceSession.initEbene("ForLoopsSindToll");
        int wieLange = 8;
        Auto a_test = new Auto( "EchterHashEcht","REGENBOGEN",12,"y-232" ,"vehilkulaer","kategorisch");
        Ticket t_test = new Ticket();

        for (int i=0;i<wieLange;i++){
            Long timeNow = System.currentTimeMillis();
            ParkhausServletPostDto paramsErstesAuto = new ParkhausServletPostDto(2, timeNow, 0,
                    0, "echterHash"+i, "REGENBOGEN", 1, "Family", "SUV", "Y-123 456");
            t_test = parkhausServiceSession.generateTicket("ForLoopsSindToll",paramsErstesAuto);

            a_test = t_test.getAuto();
            Assertions.assertEquals(i+1,parkhausServiceSession.getTotalUsers("ForLoopsSindToll"));
        }

    }

    @Test
    @DisplayName("Test der addCar-Funktion")
    public void testAddCar() {
        String parkhausName = "ForLoopsSindToll";
        parkhausServiceSession.initEbene(parkhausName);
        int wieLange = 8;
        Auto a_test = new Auto( "EchterHashEcht","REGENBOGEN",12,"y-232" ,"vehilkulaer","kategorisch");
        Ticket t_test = new Ticket();

        for (int i=0;i<wieLange;i++){
            Long timeNow = System.currentTimeMillis();
            ParkhausServletPostDto paramsErstesAuto = new ParkhausServletPostDto(2, timeNow, 0,
                    0, "echterHash"+i, "REGENBOGEN", 1, "Family", "SUV", "Y-123 456");
            t_test = parkhausServiceSession.generateTicket(parkhausName,paramsErstesAuto);
            a_test = t_test.getAuto();
            Assertions.assertEquals("echterHash"+i,a_test.getHash());
        }

    }

    @Test
    @DisplayName("Test der addCar-Funktion")
    public void testAllCars() {
        String parkhausName = "ForLoopsSindToll";
        parkhausServiceSession.initEbene(parkhausName);
        int wieLange = 8;
        Auto a_test = new Auto( "EchterHashEcht","REGENBOGEN",12,"y-232" ,"vehilkulaer","kategorisch");
        Ticket t_test = new Ticket();

        for (int i=0;i<wieLange;i++){
            Long timeNow = System.currentTimeMillis();
            ParkhausServletPostDto paramsErstesAuto = new ParkhausServletPostDto(2, timeNow, 0,
                    0, "echterHash"+i, "REGENBOGEN", 1, "Family", "SUV", "Y-123 456");
            t_test = parkhausServiceSession.generateTicket(parkhausName,paramsErstesAuto);
            a_test = t_test.getAuto();
            Assertions.assertEquals(a_test.getHash(),parkhausServiceSession.autosInParkEbene(parkhausName).get(i).getHash());
        }

    }

    @Test
    @DisplayName("Test der generateStatisticsOverVehicle-Funktion")
    public void testGenerateStatisticsOverVehicle() {
        String parkhausName = "ForLoopsSindToll";
        parkhausServiceSession.initEbene(parkhausName);
        int wieLange = 8;
        Auto a_test = new Auto( "EchterHashEcht","REGENBOGEN",12,"y-232" ,"vehilkulaer","kategorisch");
        Ticket t_test = new Ticket();

        for (int i=0;i<wieLange;i++){
            Long timeNow = System.currentTimeMillis();
            ParkhausServletPostDto paramsErstesAuto = new ParkhausServletPostDto(2, timeNow, 0,
                    0, "echterHash"+i, "REGENBOGEN", 1, "Family", "SUV", "Y-123 456");
            t_test = parkhausServiceSession.generateTicket(parkhausName,paramsErstesAuto);
            a_test = t_test.getAuto();
            Assertions.assertEquals(a_test.getHash(),parkhausServiceSession.autosInParkEbene(parkhausName).get(i).getHash());
        }
        assertNotNull(parkhausServiceSession.generateStatisticsOverVehicle(parkhausName));
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
            String parkhausId = "Test Ebene";
            parkhausServiceSession.initEbene(parkhausId);

            Ticket erstesTestTicket = parkhausServiceSession.generateTicket(parkhausId, paramsErstesAuto);
            assertNotNull(erstesTestTicket);
        }

        public void bezahleTicketTest() {
            String parkhausId = "Test Ebene";
            parkhausServiceSession.initEbene(parkhausId);

            Ticket erstesTestTicket = parkhausServiceSession.generateTicket(parkhausId, paramsErstesAuto);
            Date timeCheckOut = new Date(erstesTestTicket.getAusstellungsdatum().getTime() + 100);
            Long preisInCent = parkhausServiceSession.payForTicket(parkhausId, erstesTestTicket, timeCheckOut);
            assert preisInCent > 0;
            assert preisInCent == 1;
        }
    }


}
