package de.hbrs.team7.se1_starter_repo;

// import com.sun.org.apache.xpath.internal.operations.Equals;
import de.hbrs.team7.se1_starter_repo.dto.ParkhausEbeneConfigDTO;
import de.hbrs.team7.se1_starter_repo.entities.*;
import de.hbrs.team7.se1_starter_repo.services.DatabaseServiceGlobal;
import de.hbrs.team7.se1_starter_repo.services.ParkhausServiceGlobal;
import de.hbrs.team7.se1_starter_repo.services.ParkhausServiceSession;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import org.jboss.weld.junit5.auto.ActivateScopes;
import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;


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

    @AfterEach
    public void dropTables(){
        //databaseServiceGlobal.bobbyTruncateTables();
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
            Parkhaus p_test = databaseServiceGlobal.findeUeberID(p_id, Parkhaus.class);

            Assertions.assertEquals(testEntity.getId(), p_test.getId());
            Assertions.assertEquals(testEntity.getId(), p_test.getId());
        }

        @Test
        @Order(3)
        @DisplayName("Testen der merge Funktion")
        public void mergeTest(){

            long p_id = testEntity.getId();
            Parkhaus p_test = databaseServiceGlobal.findeUeberID(p_id, Parkhaus.class);

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
            Parkhaus p_test = databaseServiceGlobal.findeUeberID(p_id, Parkhaus.class);

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
            Parkhaus p_test_ref = databaseServiceGlobal.findeUeberID(p_id, Parkhaus.class);

            Assertions.assertNull(p_test_ref);
        }


    }

    @Test
    @DisplayName("Test 1-1 Relation")
    public void oneToOneTest(){

        Auto a_test = generateDefaultAuto();
        databaseServiceGlobal.persistEntity(a_test);
        Assertions.assertNotNull(a_test.getAutonummer());

        Ticket t_test = new Ticket();

        a_test.setTicket(t_test);
        t_test.setAuto(a_test);

        databaseServiceGlobal.persistEntity(t_test);
        Assertions.assertNotNull(t_test.getTicketnummer());

        Ticket saved_ticket = databaseServiceGlobal.findeUeberID(t_test.getTicketnummer(), Ticket.class);
        Auto saved_Auto = databaseServiceGlobal.findeUeberID(a_test.getAutonummer(), Auto.class);

        Assertions.assertNotNull(saved_ticket.getAuto());
        Assertions.assertNotNull(saved_Auto.getTicket());

    }

    @Test
    @DisplayName("Test 1-n Relation")
    public void oneToManyTest() {
        Parkhaus parkhaus = generateDefaultParkhaus();
        //ParkhausEbene ebene1 = new ParkhausEbene("Ebene 1", parkhaus);
        ParkhausEbene[] ebenen = generateEbenen(2,parkhaus);
        ParkhausEbene ebene1 = ebenen[0];
        ParkhausEbene ebene2 = ebenen[1];

        parkhaus.parkhausEbeneHinzufügen(ebene1);
        parkhaus.parkhausEbeneHinzufügen(ebene2);
        ebene1.parkhausZuweisen(parkhaus); databaseServiceGlobal.persistEntity(parkhaus);
        ebene2.parkhausZuweisen(parkhaus); databaseServiceGlobal.persistEntity(ebene1);
        databaseServiceGlobal.persistEntity(ebene2);
        Assertions.assertNotNull(parkhaus.getId(), "Parkhaus wurde nicht gespeichert");
        Assertions.assertNotNull(ebene1.getId(), "Ebene 1 wurde nicht gespeichert");
        Assertions.assertNotNull(ebene2.getId(), "Ebene 2 wurde nicht gespeichert");

        Parkhaus saved_Parkhaus = databaseServiceGlobal.findeUeberID(parkhaus.getId(), Parkhaus.class);
        ParkhausEbene saved_ebene1 = databaseServiceGlobal.findeUeberID(ebene1.getId(), ParkhausEbene.class);
        ParkhausEbene saved_ebene2 = databaseServiceGlobal.findeUeberID(ebene2.getId(), ParkhausEbene.class);

        Assertions.assertNotNull(saved_Parkhaus.getEbenen(), "Dem Parkhaus wurden die Ebenen nicht zugewiesen");
        Assertions.assertNotNull(saved_ebene1.getParkhaus(), "Ebene 1 hat kein Parkhaus erhalten");
        Assertions.assertNotNull(saved_ebene2.getParkhaus(), "Ebene 2 hat kein parkhaus erhalten");

    }

    @Test
    @DisplayName("Test n-n Relation")
    public void manyToManyTest() {
        Parkhaus parkhaus = generateDefaultParkhaus();

        ParkhausEbene[] ebenen = generateEbenen(2,parkhaus);
        ParkhausEbene ebene1 = ebenen[0];
        ebene1.setName("Ebene 1");
        ebene1.setParkhaus(parkhaus);
        ParkhausEbene ebene2 = ebenen[1];
        ebene2.setName("Ebene 2");
        ebene2.setParkhaus(parkhaus);


        Auto auto1 = generateDefaultAuto();
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

        Parkhaus p = generateDefaultParkhaus();
        databaseServiceGlobal.persistEntity(p);
        Assertions.assertNotNull(p.getId());

        ParkhausEbene p_e = generateEbenen(1,p)[0];
        p.parkhausEbeneHinzufügen(p_e);
        databaseServiceGlobal.persistEntity(p_e);
        Assertions.assertNotNull(p_e.getId());

        Auto a_test = generateDefaultAuto();
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


        Parkhaus saved_p = databaseServiceGlobal.findeUeberID(p.getId(), Parkhaus.class);
        ParkhausEbene saved_pe = databaseServiceGlobal.findeUeberID(p_e.getId(), ParkhausEbene.class);

        Assertions.assertEquals(1,saved_p.getEbenen().size());


    }

    @Test
    @DisplayName("Test der findeParkhausMitEbeneUeberId-Funktion")
    public void testFindeParkhausMitEbeneUeberId() {
        Parkhaus p1 = new Parkhaus("EineStadt", "einBundesland", 0.0, 1.0, 0.0,  1);
        Parkhaus p2 = new Parkhaus("ZweiteStadt", "einAnderesBundesland", 2.0, 3.0, 0.0,  1);

        ParkhausEbene[] ebenen = generateEbenen(2,p1);
        ParkhausEbene[] ebenen2 = generateEbenen(2,p2);
        ParkhausEbene e11 = ebenen[0];
        ParkhausEbene e12 = ebenen[1];
        ParkhausEbene e21 = ebenen2[0];
        ParkhausEbene e22 = ebenen2[1];

        p1.parkhausEbeneHinzufügen(ebenen[0]);
        p1.parkhausEbeneHinzufügen(ebenen[1]);

        p2.parkhausEbeneHinzufügen(ebenen2[0]);
        p2.parkhausEbeneHinzufügen(ebenen2[1]);

        // e11.setParkhaus(p1);
        // e12.setParkhaus(p1);
        // e21.setParkhaus(p2);
        // e22.setParkhaus(p2);


        // p1.setEbenen(Arrays.asList(ebenen) );

        // List<ParkhausEbene> ebene2List = new LinkedList<ParkhausEbene>(ebenen2);
        // p2.setEbenen(Arrays.asList(ebenen2) );

        databaseServiceGlobal.persistEntity(p1);
        databaseServiceGlobal.persistEntity(p2);

        Assertions.assertNotNull(databaseServiceGlobal.findeParkhausMitEbeneUeberId(p1.getId()));
        Assertions.assertNotNull(databaseServiceGlobal.findeParkhausMitEbeneUeberId(p2.getId()));
        Assertions.assertEquals(databaseServiceGlobal.findeParkhausMitEbeneUeberId(p1.getId()), p1);
        Assertions.assertEquals(databaseServiceGlobal.findeParkhausMitEbeneUeberId(p2.getId()), p2);

        Assertions.assertEquals(databaseServiceGlobal.findeParkhausMitEbeneUeberId(p1.getId()).getEbenen().size(), 2);
        Assertions.assertEquals(databaseServiceGlobal.findeParkhausMitEbeneUeberId(p2.getId()).getEbenen().size(), 2);

    }

    @Test
    @DisplayName("Testen der Get Sum bei Data Base Funktion")
    public void testGetSum() {
        Parkhaus parkhaus = generateDefaultParkhaus();

        ParkhausEbene ebene1 = generateEbenen(1,parkhaus)[0];
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
            Assertions.assertEquals(500*(i+1),databaseServiceGlobal.getSummeDerTicketpreise(ebene1.getId()));

        }

    }

    @Test
    @DisplayName("Testen der Total Users bei Data Base Funktion mit ner For Loop")
    public void testTotalUsersForLoop() {
        Parkhaus parkhaus = generateDefaultParkhaus();
        ParkhausEbene ebene1 = generateEbenen(1,parkhaus)[0];
        databaseServiceGlobal.persistEntity(ebene1);
        int wieLange = 8;
        Auto a_test;
        Ticket t_test;

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
            Assertions.assertEquals(i+1,databaseServiceGlobal.getAnzahlAllerUser(ebene1.getId()));

        }

    }

    @Test
    @DisplayName("Testen der Not vailable bei Data Base Funktion")
    public void testGetAllCarsInLevel() {
        Parkhaus parkhaus = generateDefaultParkhaus();
        ParkhausEbene ebene1 = generateEbenen(1,parkhaus)[0];
        databaseServiceGlobal.persistEntity(ebene1);
        int wieLange = 8;
        Auto a_test = generateDefaultAuto();
        Ticket t_test;

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
            Assertions.assertEquals(a_test.getHash(),databaseServiceGlobal.getautosInParkEbene(ebene1.getId(), true).get(i).getHash());

        }
        a_test.setImParkhaus(false);
        databaseServiceGlobal.mergeUpdatedEntity(a_test);
        Assertions.assertEquals(wieLange-1,databaseServiceGlobal.getautosInParkEbene(ebene1.getId(), true).size());
        Assertions.assertEquals(wieLange,databaseServiceGlobal.autosInParkEbeneHistoric(ebene1.getId()).size());

    }

    @Test
    @DisplayName("Testen der Tages Einnahmen Funktion")
    public void testTagesEinnahmen() {
        LocalTime nau = LocalDateTime.now().toLocalTime();
        System.out.println(LocalDateTime.now().minusSeconds(nau.toSecondOfDay()).toLocalDate());

        //Date nau2 = Date.from(Instant.now());
        System.out.print((Date.from(Instant.parse(Instant.now().toString().substring(0, 10) + "T00:00:00.00Z"))).getTime());
        Parkhaus parkhaus = generateDefaultParkhaus();
        ParkhausEbene ebene1 = generateEbenen(1,parkhaus)[0];
        databaseServiceGlobal.persistEntity(ebene1);
        int wieLange = 8;
        Auto a_test;
        Ticket t_test;

        for (int i = 0; i < wieLange; i++) {
            a_test = new Auto("EchterHashEcht" + i, "REGENBOGEN", 12, "y-232", "vehilkulaer", "kategorisch");
            t_test = new Ticket();
            t_test.setAuto(a_test);
            t_test.setPrice(500);
            t_test.setAusfahrdatum(Date.from(Instant.now()));
            a_test.setTicket(t_test);
            databaseServiceGlobal.persistEntity(t_test);
            ArrayList<Ticket> ticketArray = ebene1.getTickets();
            ticketArray.add(t_test);
            ebene1.setTickets(ticketArray);
            databaseServiceGlobal.mergeUpdatedEntity(t_test);
            databaseServiceGlobal.mergeUpdatedEntity(ebene1);
            Assertions.assertEquals(500 * (i + 1), databaseServiceGlobal.errechneTagesEinnahmen(ebene1.getId()));
        }
        a_test = new Auto("EchterHashEcht" + wieLange+1, "REGENBOGEN", 12, "y-232", "vehilkulaer", "kategorisch");
        t_test = new Ticket();
        t_test.setAuto(a_test);
        t_test.setPrice(500);
        t_test.setAusfahrdatum(Date.from(Instant.parse("2018-11-30T18:35:24.00Z")));
        a_test.setTicket(t_test);
        databaseServiceGlobal.persistEntity(t_test);
        ArrayList<Ticket> ticketArray = ebene1.getTickets();
        ticketArray.add(t_test);
        ebene1.setTickets(ticketArray);
        databaseServiceGlobal.mergeUpdatedEntity(t_test);
        databaseServiceGlobal.mergeUpdatedEntity(ebene1);
        Assertions.assertEquals(500 * (wieLange), databaseServiceGlobal.errechneTagesEinnahmen(ebene1.getId()));
    }

    @Test
    @DisplayName("Testen der Wochen Einnahmen Funktion")
    public void testWochenEinnahmen() {
        LocalTime nau = LocalDateTime.now().toLocalTime();
        System.out.println(LocalDateTime.now().minusSeconds(nau.toSecondOfDay()).toLocalDate());

        //Date nau2 = Date.from(Instant.now());
        System.out.print((Date.from(Instant.parse(Instant.now().toString().substring(0, 10) + "T00:00:00.00Z"))).getTime());
        Parkhaus parkhaus = generateDefaultParkhaus();
        ParkhausEbene ebene1 = generateEbenen(1,parkhaus)[0];
        databaseServiceGlobal.persistEntity(ebene1);
        int wieLange = 8;
        Auto a_test = new Auto("EchterHashEcht", "REGENBOGEN", 12, "y-232", "vehilkulaer", "kategorisch");
        Ticket t_test = new Ticket();

        for (int i = 0; i < wieLange; i++) {
            a_test = new Auto("EchterHashEcht" + i, "REGENBOGEN", 12, "y-232", "vehilkulaer", "kategorisch");
            t_test = new Ticket();
            t_test.setAuto(a_test);
            t_test.setPrice(500);
            t_test.setAusfahrdatum(Date.from(Instant.now()));
            a_test.setTicket(t_test);
            databaseServiceGlobal.persistEntity(t_test);
            ArrayList<Ticket> ticketArray = ebene1.getTickets();
            ticketArray.add(t_test);
            ebene1.setTickets(ticketArray);
            databaseServiceGlobal.mergeUpdatedEntity(t_test);
            databaseServiceGlobal.mergeUpdatedEntity(ebene1);
            Assertions.assertEquals(500 * (i + 1), databaseServiceGlobal.errechneWochenEinnahmen(ebene1.getId()));
        }
        a_test = new Auto("EchterHashEcht" + wieLange+1, "REGENBOGEN", 12, "y-232", "vehilkulaer", "kategorisch");
        t_test = new Ticket();
        t_test.setAuto(a_test);
        t_test.setPrice(500);
        t_test.setAusfahrdatum(Date.from(Instant.parse("2018-11-30T18:35:24.00Z")));
        a_test.setTicket(t_test);
        databaseServiceGlobal.persistEntity(t_test);
        ArrayList<Ticket> ticketArray = ebene1.getTickets();
        ticketArray.add(t_test);
        ebene1.setTickets(ticketArray);
        databaseServiceGlobal.mergeUpdatedEntity(t_test);
        databaseServiceGlobal.mergeUpdatedEntity(ebene1);
        Assertions.assertEquals(500 * (wieLange), databaseServiceGlobal.errechneWochenEinnahmen(ebene1.getId()));
        a_test = new Auto("EchterHashEcht" + wieLange+2, "REGENBOGEN", 12, "y-232", "vehilkulaer", "kategorisch");
        t_test = new Ticket();
        t_test.setAuto(a_test);
        t_test.setPrice(500);
        t_test.setAusfahrdatum(Date.from(Instant.now().minus(1, ChronoUnit.DAYS)));
        a_test.setTicket(t_test);
        databaseServiceGlobal.persistEntity(t_test);
        ticketArray = ebene1.getTickets();
        ticketArray.add(t_test);
        ebene1.setTickets(ticketArray);
        databaseServiceGlobal.mergeUpdatedEntity(t_test);
        databaseServiceGlobal.mergeUpdatedEntity(ebene1);
        Assertions.assertEquals(500 * (wieLange + 1), databaseServiceGlobal.errechneWochenEinnahmen(ebene1.getId()));
    }




    @Test
    @DisplayName("Testen der autosInParkEbeneHistoric Funktion")
    public void testGetCarInLevel() {
        Parkhaus parkhaus = generateDefaultParkhaus();
        ParkhausEbene ebene1 = generateEbenen(1,parkhaus)[0];
        databaseServiceGlobal.persistEntity(ebene1);
        int wieLange = 80;
        Auto a_test;
        Ticket t_test;

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
            Assertions.assertEquals(1,databaseServiceGlobal.getautosInParkEbene(ebene1.getId(),true).size());
            Assertions.assertEquals(i, databaseServiceGlobal.getautosInParkEbene(ebene1.getId(),false).size());
            a_test.setImParkhaus(false);
            databaseServiceGlobal.mergeUpdatedEntity(a_test);
            Assertions.assertEquals(0,databaseServiceGlobal.getautosInParkEbene(ebene1.getId(),true).size());
            Assertions.assertEquals(i+1, databaseServiceGlobal.getautosInParkEbene(ebene1.getId(),false).size());

        }

    }
    /*
        @Test
        @DisplayName("dropTableTest")
        public void testDropTable() {
            Parkhaus testP = new Parkhaus( "Teststadt3", "Testbundesland2", 0.0, 0.0, 0.0, 1  );
            //Parkhaus testP = generateDefaultParkhaus();

            ParkhausEbene p_e = new ParkhausEbene("TestParkhausEbene",testP);
            testP.parkhausEbeneHinzufügen(p_e);
            databaseServiceGlobal.persistEntity(p_e);
            databaseServiceGlobal.persistEntity(testP);
            databaseServiceGlobal.bobbyTruncateTables();
            Assertions.assertNull(databaseServiceGlobal.findeParkhausEbene(testP.getEbenen().get(0).getId()));
        }
    */
    @Test
    @DisplayName("Testen der autosInParkEbeneHistoric Funktion")
    public void testGetCarsByFederalState(){
        Parkhaus parkhaus = generateDefaultParkhaus();
        Parkhaus parkhausBundesland2 = new Parkhaus( "Teststadt", "Testbundesland2", 0.0, 0.0, 0.0, 1  );

        ParkhausEbene[] ebenen = generateEbenen(2,parkhaus);
        ParkhausEbene ebene1 = ebenen[0];
        ParkhausEbene ebene2 = ebenen[1];

        databaseServiceGlobal.persistEntity(parkhaus);
        databaseServiceGlobal.persistEntity(ebene1);
        int wieLange = 8;
        Auto a_test;
        Ticket t_test;

        for (int i=0;i<wieLange;i++){
            a_test = new Auto( "EchterHashEcht"+i,"REGENBOGEN",12,"y-232","vehilkulaer","kategorisch" );
            t_test = new Ticket();
            t_test.setAuto(a_test);
            a_test.setTicket(t_test);
            databaseServiceGlobal.persistEntity(t_test);

            ArrayList<Ticket> ticketArray = i%2== 0 ? ebene1.getTickets() : ebene2.getTickets();

            t_test.setPrice(500);
            databaseServiceGlobal.mergeUpdatedEntity(t_test);
            ticketArray.add(t_test);
            if ( i%2 == 0){
                ebene1.setTickets(ticketArray);
                databaseServiceGlobal.mergeUpdatedEntity(ebene1);
            }
            else{
                ebene2.setTickets(ticketArray);
                databaseServiceGlobal.mergeUpdatedEntity(ebene2);
            }
            a_test.setImParkhaus(false);
            databaseServiceGlobal.mergeUpdatedEntity(a_test);

        }
        Map<String, Integer> map = databaseServiceGlobal.getTicketpreiseProBundesland();
        Assertions.assertEquals(2, map.size());
    }

    private Parkhaus generateDefaultParkhaus() {
        return new Parkhaus( "Teststadt", "Testbundesland", 0.0, 0.0, 0.0, 1  );
    }

    private Auto generateDefaultAuto() {
        return new Auto( "EchterHashEcht","REGENBOGEN",12,"y-232" ,"vehilkulaer","kategorisch");
    }

    private ParkhausEbene[] generateEbenen(int anzahl, Parkhaus parkhaus) {
        ParkhausEbene[] ebenen = new ParkhausEbene[anzahl];
        HashMap<String, Double> preise = new HashMap<>();
        preise.put("Auto", 1.0);
        for (int i = 0; i < anzahl; i++) {
            //ebenen[i] = parkhausServiceSession.initEbene("Generierte Ebene Nr. ".concat(String.valueOf(i)));
            ebenen[i] = parkhausServiceSession.initEbene(new ParkhausEbeneConfigDTO("Generierte Ebene Nr. "+i, 12, 6,24,0,5, preise, parkhaus));
        }
        return ebenen;
    }
}
