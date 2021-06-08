package de.hbrs.team7.se1_starter_repo

import de.hbrs.team7.se1_starter_repo.services.ParkhausServiceGlobal
import de.hbrs.team7.se1_starter_repo.services.ParkhausServiceSession
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.context.SessionScoped
import jakarta.inject.Inject
import org.jboss.weld.junit5.auto.ActivateScopes
import org.jboss.weld.junit5.auto.AddBeanClasses
import org.jboss.weld.junit5.auto.EnableAutoWeld
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance


@EnableAutoWeld
@ActivateScopes(SessionScoped::class, ApplicationScoped::class)
@AddBeanClasses(ParkhausServiceSession::class, ParkhausServiceGlobal::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
open class ParkhausServiceSessionTest {

    @Inject
    private lateinit var parkhausServiceSession: ParkhausServiceSession

    @Test
    open fun sessionInitTest() {
        Assertions.assertNotNull(parkhausServiceSession.city) { "error" }
    }

}

