package de.hbrs.team7.se1_starter_repo;

import de.hbrs.team7.se1_starter_repo.ParkhausServiceGlobal;
import de.hbrs.team7.se1_starter_repo.ParkhausServiceSession;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import org.jboss.weld.context.bound.BoundSessionContext;
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


}
