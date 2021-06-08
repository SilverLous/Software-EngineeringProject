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
        Assertions.assertNotNull(parkhausServiceSession.getCity());
    }

    @Test
    public void sessionAddLevel() {
        parkhausServiceSession.initEbene("TESTEBENE1");
        List<ParkhausEbene> temp = parkhausServiceSession.getParkhausEbenen();
        Assertions.assertNotNull(temp);
        Assertions.assertEquals("TESTEBENE1", parkhausServiceSession.getLevelById(temp.get(0).getId()).getName());
        parkhausServiceSession.initEbene("TESTEBENE2");
        Assertions.assertEquals("TESTEBENE2", parkhausServiceSession.getLevelById(temp.get(1).getId()).getName());
        Assertions.assertEquals(2, parkhausServiceSession.getParkhausEbenen().size());
        Assertions.assertEquals(2, parkhausServiceGlobal.getLevelSet().size());

    }

    @Test
    public void sessionAddLevelInvalidateTest() {
        parkhausServiceSession.initEbene("TESTEBENE1");
        String stadtName = parkhausServiceSession.getCity().getName();
        List<ParkhausEbene> temp = parkhausServiceSession.getParkhausEbenen();
        parkhausServiceSession.initEbene("TESTEBENE2");
        Assertions.assertEquals(2, parkhausServiceGlobal.getLevelSet().size());
        sessionContext.invalidate();
        String zweiterStadtName = parkhausServiceSession.getCity().getName();
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
        Auto a_test = new Auto( "EchterHashEcht","REGENBOGEN",12,"y-232" );
        Ticket t_test = new Ticket();

        for (int i=0;i<wieLange;i++){
            Long timeNow = System.currentTimeMillis();
            ParkhausServletPostDto paramsErstesAuto = new ParkhausServletPostDto(2, timeNow, 0,
                    0, "echterHash"+i, "REGENBOGEN", 1, "Family", "SUV", "Y-123 456");
            t_test = parkhausServiceSession.generateTicket(parkhausName,paramsErstesAuto);
            parkhausServiceSession.payForTicket(parkhausName,t_test,new Date(timeNow + 100));
            Assertions.assertEquals((i+1)*100,parkhausServiceSession.sumOverCars(parkhausName));
        }

    }

    @Test
    public void testGetAverage() {
        String parkhausId = "Test Ebene";
        parkhausServiceSession.initEbene(parkhausId);

        Long timeNow = System.currentTimeMillis();
        ParkhausServletPostDto paramsErstesAuto = new ParkhausServletPostDto(2, timeNow, 0,
                0, "echterHash", "REGENBOGEN", 1, "Family", "SUV", "Y-123 456");
        assert parkhausServiceSession.getTotalUsers(parkhausId) == 0;
        parkhausServiceSession.generateTicket(parkhausId, paramsErstesAuto);
        assert parkhausServiceSession.getTotalUsers(parkhausId) == 1;
        parkhausServiceSession.payForTicket(parkhausId, parkhausServiceSession.findTicketByPlace(parkhausId, paramsErstesAuto.getSpace()), Date.from(Instant.now()));
    }

    @Test
    public void testGetTotalUsers() {
        parkhausServiceSession.initEbene("ForLoopsSindToll");
        int wieLange = 8;
        Auto a_test = new Auto( "EchterHashEcht","REGENBOGEN",12,"y-232" );
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
        Parkhaus parkhaus = new Parkhaus("Testparkhaus");
        ParkhausEbene ebene = new ParkhausEbene();
        parkhaus.addParkhausEbene(ebene);
        ebene.setParkhaus(parkhaus);

        Long timeNow = System.currentTimeMillis();
        ParkhausServletPostDto paramsErstesAuto = new ParkhausServletPostDto(2, timeNow, 0, 0,
                "echterHash", "REGENBOGEN", 1, "Family", "SUV", "Y-123 456");

        Assertions.assertTrue(ebene.getTickets().isEmpty());
        parkhausServiceSession.addCar(ebene.getId(), paramsErstesAuto);

        Assertions.assertFalse(ebene.getTickets().isEmpty());
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
            Assertions.assertNotNull(erstesTestTicket);
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
