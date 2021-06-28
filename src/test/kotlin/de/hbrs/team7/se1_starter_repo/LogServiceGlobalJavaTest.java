package de.hbrs.team7.se1_starter_repo;

import de.hbrs.team7.se1_starter_repo.dto.LogEintragDTO;
import de.hbrs.team7.se1_starter_repo.services.DatabaseServiceGlobal;
import de.hbrs.team7.se1_starter_repo.services.LoggerServiceGlobal;
import de.hbrs.team7.se1_starter_repo.services.ParkhausServiceGlobal;
import de.hbrs.team7.se1_starter_repo.services.ParkhausServiceSession;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import org.jboss.weld.junit5.auto.ActivateScopes;
import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertTrue;


// https://github.com/weld/weld-junit/blob/master/junit5/README.md
@EnableAutoWeld
@ActivateScopes({ SessionScoped.class, ApplicationScoped.class })
@AddBeanClasses({ ParkhausServiceSession.class, ParkhausServiceGlobal.class, LoggerServiceGlobal.class})
public class LogServiceGlobalJavaTest {


    @Inject
    ParkhausServiceSession parkhausServiceSession;

    @Inject
    ParkhausServiceGlobal parkhausServiceGlobal;

    @Inject
    LoggerServiceGlobal loggerServiceGlobal;



    @BeforeEach
    public void setup() {

    }

    @Test
    public void sessionInitTest() {
        assert loggerServiceGlobal != null;
    }

    @Test
    public void LoggerInitTest() {

        // List<List<ManagerStatistikUpdateDTO>> l = Collections.emptyList();
        List<LogEintragDTO> events = new ArrayList<>(Collections.emptyList());
        this.loggerServiceGlobal.getLogSubject().subscribe(
                s -> events.add(s)
        );

        assertTrue(events.size() > 0);

    }


}
