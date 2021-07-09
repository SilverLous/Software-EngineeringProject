package de.hbrs.team7.se1_starter_repo;
import java.net.URL;

import de.hbrs.team7.se1_starter_repo.servlets.Level1Servlet;
import de.hbrs.team7.se1_starter_repo.servlets.ParkhausServlet;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(ArquillianExtension.class)
@RunAsClient
public class ServletTestClassOne {
    @Deployment(testable = false)
    public static WebArchive getTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "team7Parkhaus.war").addClass(ParkhausServlet.class);
    }

    @ArquillianResource
    URL deploymentUrl;

    @Test
    public void shouldBeAbleToInvokeServletInDeployedWebApp() throws Exception {
        deploymentUrl = new URL( "http","localhost",8084,"team7Parkhaus");
        String requestUrl = deploymentUrl + "level1-servlet?" + "cmd" + "=einnahmenueberbundesland";
        String body = (new URL(requestUrl).getContent()).toString();
        System.out.println(body);
        //Assert.assertEquals("Verify that the servlet was deployed and returns expected result", "hello", body);
    }
}