package de.hbrs.team7.se1_starter_repo;

// import com.sun.org.apache.xpath.internal.operations.Equals;
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
            Parkhaus p_test = new Parkhaus("TestStadt" );
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
            Assertions.assertEquals(testEntity.getName(), p_test.getName());
        }

        @Test
        @Order(3)
        @DisplayName("Testen der merge Funktion")
        public void mergeTest(){

            long p_id = testEntity.getId();
            Parkhaus p_test = databaseServiceGlobal.findByID(p_id, Parkhaus.class);

            String ref = p_test.getName();
            p_test.setName(ref + "Merge");

            Parkhaus p_merged = databaseServiceGlobal.mergeUpdatedEntity(p_test);

            Assertions.assertEquals(testEntity.getId(), p_merged.getId());
            Assertions.assertNotEquals(ref, p_merged.getName());
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

            Parkhaus p_test = new Parkhaus("TestStadt" );
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

        Auto a_test = new Auto( "EchterHashEcht","REGENBOGEN",12,"y-232" );
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
        Parkhaus parkhaus = new Parkhaus("TestStadt");
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

       /*
    @Test
    @DisplayName("Test n-n Relation")
    public void manyToManyTest() {
        ParkhausEbene ebene1 = new ParkhausEbene();
        ParkhausEbene ebene2 = new ParkhausEbene();
        Auto auto1 = new Auto();
        Auto auto2 = new Auto();

        databaseServiceGlobal.persistEntity(ebene1);
        databaseServiceGlobal.persistEntity(ebene2);
        databaseServiceGlobal.persistEntity(auto1);
        databaseServiceGlobal.persistEntity(auto2);

        ebene1.appendTicket(ticket1);
        ebene1.appendTicket(ticket2);

        ebene2.appendTicket(ticket1);
        ebene2.appendTicket(ticket2);

        Assertions.assertNotNull(ebene1.getTickets);

    }             */

    @Test
    @DisplayName("Testen der Ticket Create funktion")
    public void deleteObjectTest(){

        Parkhaus p = new Parkhaus("TestParkhaus");
        databaseServiceGlobal.persistEntity(p);
        Assertions.assertNotNull(p.getId());

        ParkhausEbene p_e = new ParkhausEbene("TestParkhausEbene",p);
        p.addParkhausEbene(p_e);
        databaseServiceGlobal.persistEntity(p_e);
        Assertions.assertNotNull(p_e.getId());

        Auto a_test = new Auto( "EchterHashEcht","REGENBOGEN",12,"y-232" );
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
    @DisplayName("Testen der Summe und Zählfunktion einer Parkhausebene")
    public void testGetSumAndCountOfLevel() {
        Parkhaus p = new Parkhaus("TestParkhaus");
        databaseServiceGlobal.persistEntity(p);
        Assertions.assertNotNull(p.getId());

        ParkhausEbene p_e = new ParkhausEbene("TestParkhausEbene",p);
        p.addParkhausEbene(p_e);
        databaseServiceGlobal.persistEntity(p_e);
        Assertions.assertNotNull(p_e.getId());

        Auto a_test = new Auto( "EchterHashEcht","REGENBOGEN",12,"y-232" );
        databaseServiceGlobal.persistEntity(a_test);
        Assertions.assertNotNull(a_test.getAutonummer());

        Auto a_test_2 = new Auto("GanzEchterHash", "Mausgrau", 5, "ABC-123");
        databaseServiceGlobal.persistEntity(a_test_2);
        Assertions.assertNotNull(a_test_2.getAutonummer());

        Ticket t_test = new Ticket(  );
        t_test.setAuto(a_test);
        databaseServiceGlobal.persistEntity(t_test);
        Assertions.assertNotNull(t_test.getTicketnummer());

        Ticket t_test_2 = new Ticket();
        ArrayList<ParkhausEbene> arraylistEbenen = new ArrayList<ParkhausEbene>();
        arraylistEbenen.add(p_e);
        t_test_2.setAuto(a_test_2);
        databaseServiceGlobal.persistEntity(t_test_2);
        Assertions.assertNotNull(t_test_2.getTicketnummer());

        Assertions.assertNotNull(databaseServiceGlobal.getSumAndCountOfLevel(p_e.getIdAsString()));

    }


    @Test
    @DisplayName("Finden eines Tickets anhand der Platznummer und der Ebenen-ID")
    public void testFindTicketByPlace() {
        Parkhaus p = new Parkhaus("TestParkhaus");
        databaseServiceGlobal.persistEntity(p);
        Assertions.assertNotNull(p.getId());

        ParkhausEbene p_e = new ParkhausEbene("TestParkhausEbene",p);
        p.addParkhausEbene(p_e);
        databaseServiceGlobal.persistEntity(p_e);
        Assertions.assertNotNull(p_e.getId());

        Auto a_test = new Auto( "EchterHashEcht","REGENBOGEN",12,"y-232" );
        databaseServiceGlobal.persistEntity(a_test);
        Assertions.assertNotNull(a_test.getAutonummer());

        Auto a_test_2 = new Auto("GanzEchterHash", "Mausgrau", 5, "ABC-123");
        databaseServiceGlobal.persistEntity(a_test_2);
        Assertions.assertNotNull(a_test_2.getAutonummer());

        Ticket t_test = new Ticket();
        t_test.setAuto(a_test);
        databaseServiceGlobal.persistEntity(t_test);
        Assertions.assertNotNull(t_test.getTicketnummer());

        Ticket t_test_2 = new Ticket();
        ArrayList<ParkhausEbene> arraylistEbenen = new ArrayList<ParkhausEbene>();
        arraylistEbenen.add(p_e);
        t_test_2.setParkhausEbenen(arraylistEbenen);
        t_test_2.setAuto(a_test_2);
        databaseServiceGlobal.persistEntity(t_test_2);
        Assertions.assertNotNull(t_test_2.getTicketnummer());

        Assertions.assertEquals(databaseServiceGlobal.findTicketByPlace(p_e.getIdAsString(),5),t_test_2);
    }
    @Test
    @DisplayName("Testen der Finde Parkhaus bei ParkhausID Funktion")
    public void testFindeParkhausDurchID(String ParkhausID){

        Parkhaus p = new Parkhaus("TestParkhausZuFinden");
        databaseServiceGlobal.persistEntity(p);
        // assert Equals(p,databaseServiceGlobal.findEbeneByName("TestParkhausZuFinden"));

    }








}
