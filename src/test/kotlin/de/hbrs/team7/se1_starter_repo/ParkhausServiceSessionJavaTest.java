package de.hbrs.team7.se1_starter_repo;

import de.hbrs.team7.se1_starter_repo.dto.ParkhausServletPostDto;
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


// https://github.com/weld/weld-junit/blob/master/junit5/README.md
@EnableAutoWeld
@ActivateScopes({ SessionScoped.class, ApplicationScoped.class })
@AddBeanClasses({ ParkhausServiceSession.class, ParkhausServiceGlobal.class})
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
    public void setup(){
        sessionContext.invalidate();
        // parkhausServiceSession = new ParkhausServiceSession();
    }

    @Test
    public void sessionInitTest() {
        Assertions.assertNotNull(parkhausServiceSession.getCity());
    }

    @Nested
    @DisplayName("Basic IO chain")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class basicIOTest{
        String ID = "Test Ebene";
        Long timeNow = System.currentTimeMillis();
        ParkhausServletPostDto paramsErstesAuto = new ParkhausServletPostDto(2, timeNow ,0,
                0,"echterHash","REGENBOGEN",1,"Family","SUV","Y-123 456");

        @Test
        @Order(1)
        @DisplayName("Testen der addCar Funktion")
        public void zieheTicketTest(){
            Ticket erstesTestTicket = parkhausServiceSession.zieheTicket(ID,paramsErstesAuto);
            Assertions.assertNotNull(erstesTestTicket);
        }
        public void bezahleTicketTest(){
            Long timeCheckOut = timeNow + 100;
            int preisInCent = parkhausServiceSession.bezahleTicket(ID,paramsErstesAuto.getHash(),timeCheckOut);
            assert preisInCent>0;
        }
    }

}
