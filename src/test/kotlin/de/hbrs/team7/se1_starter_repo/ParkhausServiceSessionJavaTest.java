package de.hbrs.team7.se1_starter_repo;

import de.hbrs.team7.se1_starter_repo.services.ParkhausServiceGlobal;
import de.hbrs.team7.se1_starter_repo.services.ParkhausServiceSession;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import org.jboss.weld.context.bound.BoundSessionContext;
import org.jboss.weld.junit5.auto.ActivateScopes;
import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


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
        assert !parkhausServiceSession.getCity().isEmpty();
    }

    @Test
    public void addCarTestNumber(){
        // Problem: auf ID kann nicht direkt zugegriffen werden
        int carsSession1 = parkhausServiceSession.currentCars(etage).size();
        String[] carParams = {"123", "2207", "_", "_", "_"};
        parkhausServiceSession.addCar("Bonn, Level1", carParams);
        assert (parkhausServiceSession.currentCars(etage).size() - carsSession1) == 1;
        assert (parkhausServiceGlobal.getGlobalCars() - carsSession1) == 1;
    }

    @Test
    public void leaveCarTestNumber(){
        // Problem: auf ID kann nicht direkt zugegriffen werden
        int carsSession1 = parkhausServiceSession.currentCars(etage).size();
        String[] carParams = {"123", "2207", "_", "_", "_"};
        parkhausServiceSession.addCar("Bonn, Level1", carParams);
        parkhausServiceSession.leaveCar("Bonn, Level1", carParams);
        assert (parkhausServiceSession.currentCars(etage).size() - carsSession1) == 0;
    }

    @Test
    public void totalUsersTest(){
        int carsSession1 = parkhausServiceSession.currentCars(etage).size();
        String[] carParams = {"123", "2207", "_", "_", "_"};
        parkhausServiceSession.addCar("Bonn, Level1", carParams);
        parkhausServiceSession.leaveCar("Bonn, Level1", carParams);
        assert (parkhausServiceSession.currentCars(etage).size() - carsSession1) == 1;

    }

}
