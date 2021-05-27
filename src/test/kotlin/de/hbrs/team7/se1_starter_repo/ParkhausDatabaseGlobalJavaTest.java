package de.hbrs.team7.se1_starter_repo;

import de.hbrs.team7.se1_starter_repo.entities.Parkhaus;
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


// https://github.com/weld/weld-junit/blob/master/junit5/README.md
@EnableAutoWeld
@ActivateScopes({ SessionScoped.class, ApplicationScoped.class })
@AddBeanClasses({ ParkhausServiceSession.class, ParkhausServiceGlobal.class, DatabaseServiceGlobal.class})
public class ParkhausDatabaseGlobalJavaTest {

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

        private Parkhaus p;


        @Test
        @Order(1)
        @DisplayName("Testen der Insert Funktion")
        public void insertTest(){
            // Problem: auf ID kann nicht direkt zugegriffen werden
            Parkhaus p_test = new Parkhaus("TestStadt" );
            this.p =  databaseServiceGlobal.persistEntity(p_test);

            Assertions.assertNotNull(p.getId());
            System.out.println(p.getId());
            // assertAll
        }

        @Test
        @Order(2)
        @DisplayName("Testen der find Funktion")
        public void findTest(){

            long p_id = p.getId();
            Parkhaus p_test = databaseServiceGlobal.findByID(p_id, Parkhaus.class);

            Assertions.assertEquals(p, p_test);
        }

        @Test
        @Order(3)
        @DisplayName("Testen der delete Funktion")
        public void deleteTest(){

            long p_id = p.getId();
            databaseServiceGlobal.deleteByID(p_id, Parkhaus.class);
            Parkhaus p_test = databaseServiceGlobal.findByID(p_id, Parkhaus.class);

            Assertions.assertNull(p_test);
        }


    }






}
