package de.hbrs.team7.se1_starter_repo;

import de.hbrs.team7.se1_starter_repo.dto.ParkhausEbeneConfigDTO;
import de.hbrs.team7.se1_starter_repo.dto.OldGermanyStatisticsDTO;
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
import java.time.temporal.ChronoUnit;
import java.util.*;


// https://github.com/weld/weld-junit/blob/master/junit5/README.md
@EnableAutoWeld
@ActivateScopes({ SessionScoped.class, ApplicationScoped.class })
@AddBeanClasses({ ParkhausServiceSession.class, ParkhausServiceGlobal.class, DatabaseServiceGlobal.class})
public class ParkhausDatabaseGlobalJavaTest {

    String eingabeVehikel = "vehikulaer";
    String eingabeKategorie = "kategorisch";
    String eingabeKennzeichen = "y-232";
    String eingabeFarbe = "REGENBOGEN";
    String eingabeHash = "EchterHashEcht";
    String testStadt = "Teststadt";
    String testBundesLand = "Testbundesland";

    @Inject
    ParkhausServiceSession parkhausServiceSession;

    @Inject
    DatabaseServiceGlobal databaseServiceGlobal;


    @Test
    public void sessionInitTest() {
        assert databaseServiceGlobal != null;
    }

    @Nested
    @DisplayName("Simple test chain")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class BasicFunctionalityTest {
        private Parkhaus testEntity;

        @Test
        @Order(1)
        @DisplayName("Testen der Insert Funktion")
        public void insertTest(){

            // Problem: auf ID kann nicht direkt zugegriffen werden
            Parkhaus pTest = new Parkhaus( testStadt, testBundesLand, 0.0, 0.0, 0.0, 1  );
            testEntity =  databaseServiceGlobal.persistEntity(pTest);

            Assertions.assertNotNull(testEntity.getId());
            // assertAll
        }

        @Test
        @Order(2)
        @DisplayName("Testen der find Funktion")
        public void findTest(){

            long pId = testEntity.getId();
            Parkhaus pTest = databaseServiceGlobal.findeUeberID(pId, Parkhaus.class);

            Assertions.assertEquals(testEntity.getId(), pTest.getId());
            Assertions.assertEquals(testEntity.getId(), pTest.getId());
        }

        @Test
        @Order(3)
        @DisplayName("Testen der merge Funktion")
        public void mergeTest(){

            long pId = testEntity.getId();
            Parkhaus pTest = databaseServiceGlobal.findeUeberID(pId, Parkhaus.class);

            String ref = pTest.getStadtname();
            pTest.setStadtname(ref + "Merge");

            Parkhaus pMerged = databaseServiceGlobal.mergeUpdatedEntity(pTest);

            Assertions.assertEquals(testEntity.getId(), pMerged.getId());
            Assertions.assertNotEquals(ref, pMerged.getId());
        }

        @Test
        @Order(4)
        @DisplayName("Testen der delete Funktion per ID")
        public void deleteIDTest(){

            long pId = testEntity.getId();
            databaseServiceGlobal.deleteByID(pId, Parkhaus.class);
            Parkhaus pTest = databaseServiceGlobal.findeUeberID(pId, Parkhaus.class);

            Assertions.assertNull(pTest);
        }

        @Test
        @Order(5)
        @DisplayName("Testen der delete Funktion per Objekt")
        public void deleteObjectTest(){

            Parkhaus pTest = new Parkhaus( testStadt, testBundesLand, 0.0, 0.0, 0.0, 1  );
            pTest =  databaseServiceGlobal.persistEntity(pTest);
            long pId = pTest.getId();
            Assertions.assertNotNull(pId);

            databaseServiceGlobal.deleteByObject(pTest);
            Parkhaus pTestRef = databaseServiceGlobal.findeUeberID(pId, Parkhaus.class);

            Assertions.assertNull(pTestRef);
        }


    }

    @Test
    @DisplayName("Test 1-1 Relation")
    public void oneToOneTest(){

        Auto aTest = generiereDefaultAuto();
        databaseServiceGlobal.persistEntity(aTest);
        Assertions.assertNotNull(aTest.getAutonummer());

        Ticket tTest = new Ticket();

        aTest.setTicket(tTest);
        tTest.setAuto(aTest);

        databaseServiceGlobal.persistEntity(tTest);
        Assertions.assertNotNull(tTest.getTicketnummer());

        Ticket savedTicket = databaseServiceGlobal.findeUeberID(tTest.getTicketnummer(), Ticket.class);
        Auto savedAuto = databaseServiceGlobal.findeUeberID(aTest.getAutonummer(), Auto.class);

        Assertions.assertNotNull(savedTicket.getAuto());
        Assertions.assertNotNull(savedAuto.getTicket());

    }

    @Test
    @DisplayName("Test 1-n Relation")
    public void oneToManyTest() {
        Parkhaus parkhaus = generiereDefaultParkhaus();
        ParkhausEbene[] ebenen = generiereEbenen(2,parkhaus);
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

        Parkhaus savedParkhaus = databaseServiceGlobal.findeUeberID(parkhaus.getId(), Parkhaus.class);
        ParkhausEbene savedEbene1 = databaseServiceGlobal.findeUeberID(ebene1.getId(), ParkhausEbene.class);
        ParkhausEbene savedEbene2 = databaseServiceGlobal.findeUeberID(ebene2.getId(), ParkhausEbene.class);

        Assertions.assertNotNull(savedParkhaus.getEbenen(), "Dem Parkhaus wurden die Ebenen nicht zugewiesen");
        Assertions.assertNotNull(savedEbene1.getParkhaus(), "Ebene 1 hat kein Parkhaus erhalten");
        Assertions.assertNotNull(savedEbene2.getParkhaus(), "Ebene 2 hat kein parkhaus erhalten");

    }

    @Test
    @DisplayName("Test n-n Relation")
    public void manyToManyTest() {
        Parkhaus parkhaus = generiereDefaultParkhaus();

        ParkhausEbene[] ebenen = generiereEbenen(2,parkhaus);
        ParkhausEbene ebene1 = ebenen[0];
        ebene1.setName("Ebene 1");
        ebene1.setParkhaus(parkhaus);
        ParkhausEbene ebene2 = ebenen[1];
        ebene2.setName("Ebene 2");
        ebene2.setParkhaus(parkhaus);


        Auto auto1 = generiereDefaultAuto();
        Auto auto2 = new Auto(eingabeHash, eingabeFarbe, 3,eingabeKennzeichen,eingabeVehikel,eingabeKategorie);

        databaseServiceGlobal.persistEntity(ebene1);
        databaseServiceGlobal.persistEntity(ebene2);
        databaseServiceGlobal.persistEntity(auto1);
        databaseServiceGlobal.persistEntity(auto2);

        Ticket ticket1 = new Ticket();
        Ticket ticket2 = new Ticket();
        databaseServiceGlobal.persistEntity(ticket1);
        databaseServiceGlobal.persistEntity(ticket2);
        ArrayList<Ticket> liste1 = new ArrayList<>();
        liste1.add(ticket1);
        liste1.add(ticket2);

        ebene1.setTickets(liste1);

        Ticket ticket3 = new Ticket();
        Ticket ticket4 = new Ticket();
        databaseServiceGlobal.persistEntity(ticket3);
        databaseServiceGlobal.persistEntity(ticket4);
        ArrayList<Ticket> liste2 = new ArrayList<>();
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

        Parkhaus p = generiereDefaultParkhaus();
        databaseServiceGlobal.persistEntity(p);
        Assertions.assertNotNull(p.getId());

        ParkhausEbene pEbene = generiereEbenen(1,p)[0];
        p.parkhausEbeneHinzufügen(pEbene);
        databaseServiceGlobal.persistEntity(pEbene);
        Assertions.assertNotNull(pEbene.getId());

        Auto aTest = generiereDefaultAuto();
        databaseServiceGlobal.persistEntity(aTest);
        Assertions.assertNotNull(aTest.getAutonummer());

        Ticket tTest = new Ticket(  );
        tTest.setAuto(aTest);
        databaseServiceGlobal.persistEntity(tTest);
        Assertions.assertNotNull(tTest.getTicketnummer());

        ArrayList<ParkhausEbene> tNewEbenen = tTest.getParkhausEbenen();
        tNewEbenen.add(pEbene);
        tTest.setParkhausEbenen(tNewEbenen);

        databaseServiceGlobal.mergeUpdatedEntity(tTest);
        Parkhaus savedP = databaseServiceGlobal.findeUeberID(p.getId(), Parkhaus.class);
        databaseServiceGlobal.findeUeberID(pEbene.getId(), ParkhausEbene.class);

        Assertions.assertEquals(1,savedP.getEbenen().size());
    }

    @Test
    @DisplayName("Testen der query All Entities Funktion")
    public void findeAlleEntitiesTest() {

        Ticket ticket = new Ticket();
        databaseServiceGlobal.persistEntity(ticket);
        List<Ticket> tickets =  this.databaseServiceGlobal.queryAllEntities(Ticket.class);

        assert (tickets.size() > 0);
    }

    @Test
    @DisplayName("Testen der finde Parkhaus Ebene Funktion")
    public void findeParkhausEbeneTest() {

        List<ParkhausEbene> parkhausEbenen = this.databaseServiceGlobal.queryAllEntities(ParkhausEbene.class);
        ParkhausEbene parkhausEbene =  this.databaseServiceGlobal.findeParkhausEbene(parkhausEbenen.get(0).getId());
        ParkhausEbene parkhausEbeneNotExisting =  this.databaseServiceGlobal.findeParkhausEbene(99999999);

        Assertions.assertNotNull(parkhausEbene);
        Assertions.assertNull(parkhausEbeneNotExisting);

    }


    @Test
    @DisplayName("Test der findeParkhausMitEbeneUeberId-Funktion")
    public void testFindeParkhausMitEbeneUeberId() {
        Parkhaus p1 = new Parkhaus("EineStadt", "einBundesland", 0.0, 1.0, 0.0,  1);
        Parkhaus p2 = new Parkhaus("ZweiteStadt", "einAnderesBundesland", 2.0, 3.0, 0.0,  1);

        ParkhausEbene[] ebenen = generiereEbenen(2,p1);
        ParkhausEbene[] ebenen2 = generiereEbenen(2,p2);

        p1.parkhausEbeneHinzufügen(ebenen[0]);
        p1.parkhausEbeneHinzufügen(ebenen[1]);

        p2.parkhausEbeneHinzufügen(ebenen2[0]);
        p2.parkhausEbeneHinzufügen(ebenen2[1]);


        databaseServiceGlobal.persistEntity(p1);
        databaseServiceGlobal.persistEntity(p2);

        Assertions.assertNotNull(databaseServiceGlobal.findeParkhausMitEbeneUeberId(p1.getId()));
        Assertions.assertNotNull(databaseServiceGlobal.findeParkhausMitEbeneUeberId(p2.getId()));
        Assertions.assertEquals(databaseServiceGlobal.findeParkhausMitEbeneUeberId(p1.getId()), p1);
        Assertions.assertEquals(databaseServiceGlobal.findeParkhausMitEbeneUeberId(p2.getId()), p2);

        Assertions.assertEquals(databaseServiceGlobal.findeParkhausMitEbeneUeberId(p1.getId()).getEbenen().size(), 2);
        Assertions.assertEquals(databaseServiceGlobal.findeParkhausMitEbeneUeberId(p2.getId()).getEbenen().size(), 2);

        Assertions.assertEquals(databaseServiceGlobal.findeParkhausMitEbeneUeberId(p1.getId()).getEbenen().get(0).getFahrzeugTypen().size(), 1);
        Assertions.assertEquals(databaseServiceGlobal.findeParkhausMitEbeneUeberId(p2.getId()).getEbenen().get(1).getFahrzeugTypen().size(), 1);

    }

    @Test
    @DisplayName("Testen der Get Sum bei Data Base Funktion")
    public void testGetSumme() {
        int wieLange = 8;
        List<Ticket> ticketArray = generateDefaultTickets(wieLange);
        ParkhausEbene ebene1 = ticketArray.get(0).getParkhausEbenen().get(0);
        int sum = ticketArray.stream().map(x->x.getPrice()).reduce(0,Integer::sum);
        Assertions.assertEquals(sum,databaseServiceGlobal.getSummeDerTicketpreise(ebene1.getId()));
        for (int i = 0;i<8;i++){
            ticketArray.get(7-i).getAuto().setImParkhaus(false);
            ticketArray.get(7-i).setPrice(300);
            databaseServiceGlobal.mergeUpdatedEntity(ticketArray.get(7-i).getAuto());
        }
        sum = ticketArray.stream().map(x->x.getPrice()).reduce(0,Integer::sum);
        Assertions.assertEquals(sum,databaseServiceGlobal.getSummeDerTicketpreise(ebene1.getId()));

    }

    @Test
    @DisplayName("Testen der TAnzahlAllerUser Funktion")
    public void testAnzahlAllerUser() {
        int wieLange = 8;
        List<Ticket> ticketArray = generateDefaultTickets(8);
        ParkhausEbene ebene1 = ticketArray.get(0).getParkhausEbenen().get(0);
        Assertions.assertEquals(wieLange,databaseServiceGlobal.getAnzahlAllerUser(ebene1.getId()));

    }

    @Test
    @DisplayName("Testen der autosInParkEbene Funktion")
    public void testAutosInParkEbene() {
        int wieLange = 8;
        List<Ticket> ticketArray = generateDefaultTickets(8);
        ParkhausEbene ebene1 = ticketArray.get(0).getParkhausEbenen().get(0);
        Auto aTest = ticketArray.get(0).getAuto();
        aTest.setImParkhaus(false);
        databaseServiceGlobal.mergeUpdatedEntity(aTest);
        Assertions.assertEquals(wieLange-1,databaseServiceGlobal.getautosInParkEbene(ebene1.getId(), true).size());
        Assertions.assertEquals(wieLange,databaseServiceGlobal.autosInParkEbeneHistoric(ebene1.getId()).size());

    }


    public List<Ticket> tagesEinnahmenDupliCode(int wieLange, int ticketPreis){
        List<Ticket> ticketArray = generateDefaultTickets(wieLange);
        ParkhausEbene ebene1 = ticketArray.get(0).getParkhausEbenen().get(0);
        for (int i = 0;i<wieLange;i++){
            ticketArray.get(wieLange-1-i).getAuto().setImParkhaus(false);
            ticketArray.get(wieLange-1-i).setPrice(ticketPreis);
            ticketArray.get(wieLange-1-i).setAusfahrdatum(Date.from(Instant.now()));
            databaseServiceGlobal.mergeUpdatedEntity(ticketArray.get(wieLange-1-i).getAuto());
        }
        Auto aTest = new Auto(eingabeHash + wieLange+1, eingabeFarbe, 12, eingabeKennzeichen, eingabeVehikel, eingabeKategorie);
        Ticket tTest = new Ticket();
        tTest.setAuto(aTest);
        tTest.setPrice(ticketPreis);
        tTest.setAusfahrdatum(Date.from(Instant.parse("2018-11-30T18:35:24.00Z")));
        aTest.setTicket(tTest);
        databaseServiceGlobal.persistEntity(tTest);
        ticketArray = ebene1.getTickets();
        ticketArray.add(tTest);
        ebene1.setTickets((ArrayList<Ticket>) ticketArray);
        databaseServiceGlobal.mergeUpdatedEntity(tTest);
        databaseServiceGlobal.mergeUpdatedEntity(ebene1);
        return ticketArray;

    }
    @Test
    @DisplayName("Testen der Tages Einnahmen Funktion")
    public void testTagesEinnahmen() {
        int wieLange = 8;
        int ticketPreis = 500;
        List<Ticket> ticketArray = tagesEinnahmenDupliCode(wieLange,ticketPreis);
        Assertions.assertEquals(ticketPreis * (wieLange), databaseServiceGlobal.errechneTagesEinnahmen(ticketArray.get(0).getParkhausEbenen().get(0).getId()));
    }

    @Test
    @DisplayName("Testen der Wochen Einnahmen Funktion")
    public void testWochenEinnahmen() {
        int wieLange = 4;
        int ticketPreis = 200;
        List<Ticket> ticketArray = tagesEinnahmenDupliCode(wieLange,ticketPreis);
        ParkhausEbene ebene1 = ticketArray.get(0).getParkhausEbenen().get(0);
        Assertions.assertEquals(ticketPreis * (wieLange), databaseServiceGlobal.errechneWochenEinnahmen(ebene1.getId()));
        Auto aTest = new Auto(eingabeHash + wieLange+2, eingabeFarbe, 12, eingabeKennzeichen, eingabeVehikel, eingabeKategorie);
        Ticket tTest = new Ticket();
        tTest.setAuto(aTest);
        tTest.setPrice(ticketPreis);
        tTest.setAusfahrdatum(Date.from(Instant.now().minus(1, ChronoUnit.DAYS)));
        aTest.setTicket(tTest);
        databaseServiceGlobal.persistEntity(tTest);
        ticketArray = ebene1.getTickets();
        ticketArray.add(tTest);
        ebene1.setTickets((ArrayList<Ticket>) ticketArray);
        databaseServiceGlobal.mergeUpdatedEntity(tTest);
        databaseServiceGlobal.mergeUpdatedEntity(ebene1);
        Assertions.assertEquals(ticketPreis * (wieLange + 1), databaseServiceGlobal.errechneWochenEinnahmen(ebene1.getId()));
    }


    @Test
    @DisplayName("Testen der autosInParkEbeneHistoric Funktion")
    public void testGetAutosInEbene() {

        List<Ticket> ticketArray = generateDefaultTickets(8);
        ParkhausEbene ebene1 = ticketArray.get(0).getParkhausEbenen().get(0);
        Assertions.assertEquals(8,databaseServiceGlobal.getautosInParkEbene(ebene1.getId(),true).size());
        for (int i = 0;i<8;i++){
            ticketArray.get(7-i).getAuto().setImParkhaus(false);
            databaseServiceGlobal.mergeUpdatedEntity(ticketArray.get(7-i).getAuto());
            Assertions.assertEquals(7-i,databaseServiceGlobal.getautosInParkEbene(ebene1.getId(),true).size());
        }

    }

    public List<Ticket> generateDefaultTickets(int wieViele) {
        Parkhaus parkhaus = generiereDefaultParkhaus();
        ArrayList<Ticket> ticketArray = new ArrayList<>();
        ArrayList<ParkhausEbene> ebene1 = new ArrayList<>();
        ebene1.add(generiereEbenen(1,parkhaus)[0]);
        Auto aTest;
        Ticket tTest;

        for (int i=0;i<wieViele;i++){
            aTest = new Auto( eingabeHash+i,eingabeFarbe,12,eingabeKennzeichen,eingabeVehikel,eingabeKategorie);
            tTest = new Ticket();
            tTest.setAuto(aTest);
            tTest.setPrice(500);
            tTest.setParkhausEbenen(ebene1);
            aTest.setTicket(tTest);
            databaseServiceGlobal.persistEntity(tTest);
            ticketArray.add(tTest);
            ebene1.get(0).setTickets(ticketArray);
            databaseServiceGlobal.mergeUpdatedEntity(tTest);
            databaseServiceGlobal.mergeUpdatedEntity(ebene1.get(0));
        }
        return ticketArray;
    }


        @Disabled
        @Test
        @DisplayName("dropTableTest")
        public void testDropTable() {
            Parkhaus testP = new Parkhaus( "Teststadt3", "Testbundesland2", 0.0, 0.0, 0.0, 1  );


            ParkhausEbene[] ebenen = generiereEbenen(2,testP);
            ParkhausEbene pEbene = ebenen[0];
            testP.parkhausEbeneHinzufügen(pEbene);
            databaseServiceGlobal.persistEntity(pEbene);
            databaseServiceGlobal.persistEntity(testP);
            databaseServiceGlobal.bobbyTruncateTables();
            Assertions.assertNull(databaseServiceGlobal.findeParkhausEbene(testP.getEbenen().get(0).getId()));
        }

    @Test
    @DisplayName("Testen Autos nach Alte Deutschland Daten Funktion")
    public void getAlteDeutschlandDaten(){
        OldGermanyStatisticsDTO statisitk =  databaseServiceGlobal.getAlteDeutschlandDaten();

        // Für genauere Abfragen müsste die Datenbank gelöscht werden
        Assertions.assertNotNull(statisitk.getBrd().getFirst());
        Assertions.assertNotNull(statisitk.getBrd().getSecond());
        Assertions.assertNotNull(statisitk.getDdr().getFirst());
        Assertions.assertNotNull(statisitk.getDdr().getSecond());
    }



    @Test
    @DisplayName("Testen Autos nach Bundesland Funktion")
    public void testGetAutosByBundesLand(){

        Parkhaus parkhaus = generiereDefaultParkhaus();

        ParkhausEbene[] ebenen = generiereEbenen(2,parkhaus);
        ParkhausEbene ebene1 = ebenen[0];
        ParkhausEbene ebene2 = ebenen[1];

        databaseServiceGlobal.persistEntity(parkhaus);
        databaseServiceGlobal.persistEntity(ebene1);
        int wieLange = 8;
        Auto aTest;
        Ticket tTest;

        for (int i=0;i<wieLange;i++){
            aTest = new Auto( eingabeHash + i,eingabeFarbe,12,eingabeKennzeichen,eingabeVehikel,eingabeKategorie);
            tTest = new Ticket();
            tTest.setAuto(aTest);
            aTest.setTicket(tTest);
            databaseServiceGlobal.persistEntity(tTest);

            ArrayList<Ticket> ticketArray = i%2== 0 ? ebene1.getTickets() : ebene2.getTickets();

            tTest.setPrice(500);
            databaseServiceGlobal.mergeUpdatedEntity(tTest);
            ticketArray.add(tTest);
            if ( i%2 == 0){
                ebene1.setTickets(ticketArray);
                databaseServiceGlobal.mergeUpdatedEntity(ebene1);
            }
            else{
                ebene2.setTickets(ticketArray);
                databaseServiceGlobal.mergeUpdatedEntity(ebene2);
            }
            aTest.setImParkhaus(false);
            databaseServiceGlobal.mergeUpdatedEntity(aTest);

        }
        Map<String, Integer> map = databaseServiceGlobal.getTicketpreiseProBundesland();
        Assertions.assertEquals(2, map.size());
    }

    private Parkhaus generiereDefaultParkhaus() {
        return new Parkhaus( testStadt, testBundesLand, 0.0, 0.0, 0.0, 1  );
    }

    private Auto generiereDefaultAuto() {
        return new Auto( eingabeHash,eingabeFarbe,12,eingabeKennzeichen ,eingabeVehikel,eingabeKategorie);
    }

    private ParkhausEbene[] generiereEbenen(int anzahl, Parkhaus parkhaus) {
        ParkhausEbene[] ebenen = new ParkhausEbene[anzahl];
        HashMap<String, Double> preise = new HashMap<>();
        preise.put("Auto", 1.0);
        for (int i = 0; i < anzahl; i++) {
            ebenen[i] = parkhausServiceSession.initEbene(new ParkhausEbeneConfigDTO("Generierte Ebene Nr. "+i, 12, 6,24,0,5, preise, parkhaus));
        }
        return ebenen;
    }
}
