package de.hbrs.team7.se1_starter_repo;

// import com.sun.org.apache.xpath.internal.operations.Equals;
import de.hbrs.team7.se1_starter_repo.dto.ParkhausServletPostDto;
import de.hbrs.team7.se1_starter_repo.entities.*;
import de.hbrs.team7.se1_starter_repo.services.DatabaseServiceGlobal;
import de.hbrs.team7.se1_starter_repo.services.ParkhausServiceGlobal;
import de.hbrs.team7.se1_starter_repo.services.ParkhausServiceSession;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import kotlin.Pair;
import kotlinx.serialization.descriptors.PrimitiveKind;
import org.jboss.weld.junit5.auto.ActivateScopes;
import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;


// https://github.com/weld/weld-junit/blob/master/junit5/README.md
@EnableAutoWeld
@ActivateScopes({ SessionScoped.class, ApplicationScoped.class })
@AddBeanClasses({ ParkhausServiceSession.class, ParkhausServiceGlobal.class, DatabaseServiceGlobal.class})
public class ParkhausDatabaseGlobalJavaTest {

    static Parkhaus testEntity;

    @Inject
    ParkhausServiceSession parkhausServiceSession;

    @Inject
    ParkhausServiceGlobal parkhausServiceGlobal;

    @Inject
    DatabaseServiceGlobal databaseServiceGlobal;


    @BeforeEach
    public void setup(){

    }

    @Test
    public void sessionInitTest() {
        assert databaseServiceGlobal != null;
    }

    @Nested
    @DisplayName("Simple test chain")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class basicFunctionalityTest {

        @Test
        @Order(1)
        @DisplayName("Testen der Insert Funktion")
        public void insertTest(){

            // Problem: auf ID kann nicht direkt zugegriffen werden
            Parkhaus p_test = new Parkhaus( "Teststadt", "Testbundesland", 0.0, 0.0, 0.0, 1  );
            testEntity =  databaseServiceGlobal.persistEntity(p_test);

            Assertions.assertNotNull(testEntity.getId());
            System.out.println(testEntity.getId());
            // assertAll
        }

        @Test
        @Order(2)
        @DisplayName("Testen der find Funktion")
        public void findTest(){

            long p_id = testEntity.getId();
            Parkhaus p_test = databaseServiceGlobal.findByID(p_id, Parkhaus.class);

            Assertions.assertEquals(testEntity.getId(), p_test.getId());
            Assertions.assertEquals(testEntity.getId(), p_test.getId());
        }

        @Test
        @Order(3)
        @DisplayName("Testen der merge Funktion")
        public void mergeTest(){

            long p_id = testEntity.getId();
            Parkhaus p_test = databaseServiceGlobal.findByID(p_id, Parkhaus.class);

            String ref = p_test.getStadtname();
            p_test.setStadtname(ref + "Merge");

            Parkhaus p_merged = databaseServiceGlobal.mergeUpdatedEntity(p_test);

            Assertions.assertEquals(testEntity.getId(), p_merged.getId());
            Assertions.assertNotEquals(ref, p_merged.getId());
        }

        @Test
        @Order(4)
        @DisplayName("Testen der delete Funktion per ID")
        public void deleteIDTest(){

            long p_id = testEntity.getId();
            databaseServiceGlobal.deleteByID(p_id, Parkhaus.class);
            Parkhaus p_test = databaseServiceGlobal.findByID(p_id, Parkhaus.class);

            Assertions.assertNull(p_test);
        }

        @Test
        @Order(5)
        @DisplayName("Testen der delete Funktion per Objekt")
        public void deleteObjectTest(){

            Parkhaus p_test = new Parkhaus( "Teststadt", "Testbundesland", 0.0, 0.0, 0.0, 1  );
            p_test =  databaseServiceGlobal.persistEntity(p_test);
            long p_id = p_test.getId();
            Assertions.assertNotNull(p_id);

            databaseServiceGlobal.deleteByObject(p_test);
            Parkhaus p_test_ref = databaseServiceGlobal.findByID(p_id, Parkhaus.class);

            Assertions.assertNull(p_test_ref);
        }


    }

    @Test
    @DisplayName("Test 1-1 Relation")
    public void oneToOneTest(){

        Auto a_test = new Auto( "EchterHashEcht","REGENBOGEN",12,"y-232","vehilkulaer","kategorisch" );
        databaseServiceGlobal.persistEntity(a_test);
        Assertions.assertNotNull(a_test.getAutonummer());

        Ticket t_test = new Ticket();

        a_test.setTicket(t_test);
        t_test.setAuto(a_test);

        databaseServiceGlobal.persistEntity(t_test);
        Assertions.assertNotNull(t_test.getTicketnummer());

        Ticket saved_ticket = databaseServiceGlobal.findByID(t_test.getTicketnummer(), Ticket.class);
        Auto saved_Auto = databaseServiceGlobal.findByID(a_test.getAutonummer(), Auto.class);

        Assertions.assertNotNull(saved_ticket.getAuto());
        Assertions.assertNotNull(saved_Auto.getTicket());

    }

    @Test
    @DisplayName("Test 1-n Relation")
    public void oneToManyTest() {
        Parkhaus parkhaus = new Parkhaus( "Teststadt", "Testbundesland", 0.0, 0.0, 0.0, 1  );
        ParkhausEbene ebene1 = new ParkhausEbene("Ebene 1", parkhaus);
        ParkhausEbene ebene2 = new ParkhausEbene("Ebene 2", parkhaus);

        parkhaus.addParkhausEbene(ebene1);
        parkhaus.addParkhausEbene(ebene2);
        ebene1.assignParkhaus(parkhaus); databaseServiceGlobal.persistEntity(parkhaus);
        ebene2.assignParkhaus(parkhaus); databaseServiceGlobal.persistEntity(ebene1);
        databaseServiceGlobal.persistEntity(ebene2);
        Assertions.assertNotNull(parkhaus.getId(), "Parkhaus wurde nicht gespeichert");
        Assertions.assertNotNull(ebene1.getId(), "Ebene 1 wurde nicht gespeichert");
        Assertions.assertNotNull(ebene2.getId(), "Ebene 2 wurde nicht gespeichert");

        Parkhaus saved_Parkhaus = databaseServiceGlobal.findByID(parkhaus.getId(), Parkhaus.class);
        ParkhausEbene saved_ebene1 = databaseServiceGlobal.findByID(ebene1.getId(), ParkhausEbene.class);
        ParkhausEbene saved_ebene2 = databaseServiceGlobal.findByID(ebene2.getId(), ParkhausEbene.class);

        Assertions.assertNotNull(saved_Parkhaus.getEbenen(), "Dem Parkhaus wurden die Ebenen nicht zugewiesen");
        Assertions.assertNotNull(saved_ebene1.getParkhaus(), "Ebene 1 hat kein Parkhaus erhalten");
        Assertions.assertNotNull(saved_ebene2.getParkhaus(), "Ebene 2 hat kein parkhaus erhalten");

    }

    @Test
    @DisplayName("Test n-n Relation")
    public void manyToManyTest() {
        Parkhaus parkhaus = new Parkhaus( "Teststadt", "Testbundesland", 0.0, 0.0, 0.0, 1  );
        ParkhausEbene ebene1 = new ParkhausEbene();
        ebene1.setName("Ebene 1");
        ebene1.setParkhaus(parkhaus);
        ParkhausEbene ebene2 = new ParkhausEbene();
        ebene2.setName("Ebene 2");
        ebene2.setParkhaus(parkhaus);


        Auto auto1 = new Auto("EchterHashEcht","REGENBOGEN",12,"y-232","vehilkulaer","kategorisch" );
        Auto auto2 = new Auto("HashWert", "bunt", 3,"ACG-398","vehilkulaer","kategorisch");

        databaseServiceGlobal.persistEntity(ebene1);
        databaseServiceGlobal.persistEntity(ebene2);
        databaseServiceGlobal.persistEntity(auto1);
        databaseServiceGlobal.persistEntity(auto2);

        Ticket ticket1 = new Ticket();
        Ticket ticket2 = new Ticket();
        databaseServiceGlobal.persistEntity(ticket1);
        databaseServiceGlobal.persistEntity(ticket2);
        ArrayList<Ticket> liste1 = new ArrayList<Ticket>();
        liste1.add(ticket1);
        liste1.add(ticket2);

        ebene1.setTickets(liste1);

        Ticket ticket3 = new Ticket();
        Ticket ticket4 = new Ticket();
        databaseServiceGlobal.persistEntity(ticket3);
        databaseServiceGlobal.persistEntity(ticket4);
        ArrayList<Ticket> liste2 = new ArrayList<Ticket>();
        liste2.add(ticket3);
        liste2.add(ticket4);

        ebene2.setTickets(liste2);

        Assertions.assertTrue(ebene1.getTickets().contains(ticket1));
        Assertions.assertTrue(ebene1.getTickets().contains(ticket2));
        Assertions.assertFalse(ebene1.getTickets().contains(ticket3));
        Assertions.assertFalse(ebene1.getTickets().contains(ticket4));

        Assertions.assertFalse(ebene2.getTickets().contains(ticket1));
        Assertions.assertFalse(ebene2.getTickets().contains(ticket2));
        Assertions.assertTrue(ebene2.getTickets().contains(ticket3));
        Assertions.assertTrue(ebene2.getTickets().contains(ticket4));
    }

    @Test
    @DisplayName("Testen der Ticket Create funktion")
    public void deleteObjectTest(){

        Parkhaus p = new Parkhaus( "Teststadt", "Testbundesland", 0.0, 0.0, 0.0, 1  );
        databaseServiceGlobal.persistEntity(p);
        Assertions.assertNotNull(p.getId());

        ParkhausEbene p_e = new ParkhausEbene("TestParkhausEbene",p);
        p.addParkhausEbene(p_e);
        databaseServiceGlobal.persistEntity(p_e);
        Assertions.assertNotNull(p_e.getId());

        Auto a_test = new Auto( "EchterHashEcht","REGENBOGEN",12,"y-232" ,"vehilkulaer","kategorisch");
        databaseServiceGlobal.persistEntity(a_test);
        Assertions.assertNotNull(a_test.getAutonummer());

        Ticket t_test = new Ticket(  );
        t_test.setAuto(a_test);
        databaseServiceGlobal.persistEntity(t_test);
        Assertions.assertNotNull(t_test.getTicketnummer());

        ArrayList<ParkhausEbene> tNewEbenen = t_test.getParkhausEbenen();
        tNewEbenen.add(p_e);
        t_test.setParkhausEbenen(tNewEbenen);

        Ticket merged_ticket = databaseServiceGlobal.mergeUpdatedEntity(t_test);


        Parkhaus saved_p = databaseServiceGlobal.findByID(p.getId(), Parkhaus.class);
        ParkhausEbene saved_pe = databaseServiceGlobal.findByID(p_e.getId(), ParkhausEbene.class);

        Assertions.assertEquals(1,saved_p.getEbenen().size());


    }

    @Test
    @DisplayName("Testen der Get Sum bei Data Base Funktion")
    public void testGetSum() {
        Parkhaus parkhaus = new Parkhaus( "Teststadt", "Testbundesland", 0.0, 0.0, 0.0, 1  );
        ParkhausEbene ebene1 = new ParkhausEbene("ForLoopsSindToll", parkhaus);
        databaseServiceGlobal.persistEntity(ebene1);
        int wieLange = 8;
        Auto a_test = new Auto( "EchterHashEcht","REGENBOGEN",12,"y-232","vehilkulaer","kategorisch" );
        Ticket t_test = new Ticket();

        for (int i=0;i<wieLange;i++){
            a_test = new Auto( "EchterHashEcht"+i,"REGENBOGEN",12,"y-232","vehilkulaer","kategorisch" );
            t_test = new Ticket();
            t_test.setAuto(a_test);
            t_test.setPrice(500);
            a_test.setTicket(t_test);
            databaseServiceGlobal.persistEntity(t_test);
            ArrayList<Ticket> ticketArray = ebene1.getTickets();
            ticketArray.add(t_test);
            ebene1.setTickets(ticketArray);
            databaseServiceGlobal.mergeUpdatedEntity(t_test);
            databaseServiceGlobal.mergeUpdatedEntity(ebene1);
            Assertions.assertEquals(500*(i+1),databaseServiceGlobal.getSumOfTicketPrices(ebene1.getId()));

        }

    }

    @Test
    @DisplayName("Testen der Total Users bei Data Base Funktion mit ner For Loop")
    public void testTotalUsersForLoop() {
        Parkhaus parkhaus = new Parkhaus( "Teststadt", "Testbundesland", 0.0, 0.0, 0.0, 1  );
        ParkhausEbene ebene1 = new ParkhausEbene("ForLoopsSindToll", parkhaus);
        databaseServiceGlobal.persistEntity(ebene1);
        int wieLange = 8;
        Auto a_test = new Auto( "EchterHashEcht","REGENBOGEN",12,"y-232" ,"vehilkulaer","kategorisch");
        Ticket t_test = new Ticket();

        for (int i=0;i<wieLange;i++){
            a_test = new Auto( "EchterHashEcht"+i,"REGENBOGEN",12,"y-232","vehilkulaer","kategorisch" );
            t_test = new Ticket();
            t_test.setAuto(a_test);
            a_test.setTicket(t_test);
            databaseServiceGlobal.persistEntity(t_test);
            ArrayList<Ticket> ticketArray = ebene1.getTickets();
            ticketArray.add(t_test);
            ebene1.setTickets(ticketArray);
            databaseServiceGlobal.mergeUpdatedEntity(t_test);
            databaseServiceGlobal.mergeUpdatedEntity(ebene1);
            Assertions.assertEquals(i+1,databaseServiceGlobal.getTotalUsersCount(ebene1.getId()));

        }

    }

    @Test
    @DisplayName("Testen der Not vailable bei Data Base Funktion")
    public void testGetAllCarsInLevel() {
        Parkhaus parkhaus = new Parkhaus( "Teststadt", "Testbundesland", 0.0, 0.0, 0.0, 1  );
        ParkhausEbene ebene1 = new ParkhausEbene("ForLoopsSindToll", parkhaus);
        databaseServiceGlobal.persistEntity(ebene1);
        int wieLange = 8;
        Auto a_test = new Auto( "EchterHashEcht","REGENBOGEN",12,"y-232" ,"vehilkulaer","kategorisch");
        Ticket t_test = new Ticket();

        for (int i=0;i<wieLange;i++){
            a_test = new Auto( "EchterHashEcht"+i,"REGENBOGEN",12,"y-232","vehilkulaer","kategorisch" );
            t_test = new Ticket();
            t_test.setAuto(a_test);
            a_test.setTicket(t_test);
            databaseServiceGlobal.persistEntity(t_test);
            ArrayList<Ticket> ticketArray = ebene1.getTickets();
            ticketArray.add(t_test);
            ebene1.setTickets(ticketArray);
            databaseServiceGlobal.mergeUpdatedEntity(t_test);
            databaseServiceGlobal.mergeUpdatedEntity(ebene1);
            Assertions.assertEquals(a_test.getHash(),databaseServiceGlobal.autosInParkEbene(ebene1.getId(), true).get(i).getHash());

        }
        a_test.setImParkhaus(false);
        databaseServiceGlobal.mergeUpdatedEntity(a_test);
        Assertions.assertEquals(wieLange-1,databaseServiceGlobal.autosInParkEbene(ebene1.getId(), true).size());
        Assertions.assertEquals(wieLange,databaseServiceGlobal.autosInParkEbeneHistoric(ebene1.getId()).size());

    }





}
