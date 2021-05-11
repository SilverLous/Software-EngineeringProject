import de.hbrs.team7.se1_starter_repo.ParkhausServiceGlobal;
import de.hbrs.team7.se1_starter_repo.ParkhausServiceSession;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldSetup;
import org.jboss.weld.junit5.auto.ActivateScopes;
import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.jboss.weld.junit5.WeldJunit5Extension;


// https://github.com/weld/weld-junit/blob/master/junit5/README.md
@EnableAutoWeld
@ActivateScopes({ SessionScoped.class, ApplicationScoped.class })
@AddBeanClasses({ ParkhausServiceSession.class, ParkhausServiceGlobal.class})
public class ParkhausServiceSessionJavaTest {

/*    @WeldSetup
    public WeldInitiator weld = WeldInitiator.from(ParkhausServiceSession.class, ParkhausServiceGlobal.class)
            .activate(SessionScoped.class, ApplicationScoped.class ).build();*/


    @Inject
    ParkhausServiceSession parkhausServiceSession;

    @BeforeEach
    public void setup(){
        parkhausServiceSession.sessionInit();
    }

    @Test
    public void sessionInitTest() {
        assert !parkhausServiceSession.getCity().isEmpty() : "error";
    }

    @Test
    public void addCarTestNumber(){
        // Problem: auf ID kann nicht direkt zugegriffen werden
        int carsSession1 = parkhausServiceSession.getCurrentCars();
        String[] carParams = {"123", "2207", "_", "_", "_"};
        parkhausServiceSession.addCar("Bonn, Level1", carParams);
        assert (parkhausServiceSession.getCurrentCars() - carsSession1) == 1;
    }

    @Test
    public void leaveCarTestNumber(){
        // Problem: auf ID kann nicht direkt zugegriffen werden
        int carsSession1 = parkhausServiceSession.getCurrentCars();
        String[] carParams = {"123", "2207", "_", "_", "_"};
        parkhausServiceSession.addCar("Bonn, Level1", carParams);
        parkhausServiceSession.leaveCar("Bonn, Level1", carParams);
        assert (parkhausServiceSession.getCurrentCars() - carsSession1) == 0;
    }

    @Test
    public void totalUsersTest(){
        int carsSession1 = parkhausServiceSession.getSessionCars();
        String[] carParams = {"123", "2207", "_", "_", "_"};
        parkhausServiceSession.addCar("Bonn, Level1", carParams);
        parkhausServiceSession.leaveCar("Bonn, Level1", carParams);
        assert (parkhausServiceSession.getSessionCars() - carsSession1) == 1;

    }

}
