package de.hbrs.team7.se1_starter_repo.servlets

import de.hbrs.team7.se1_starter_repo.dto.ParkhausEbeneConfigDTO
import de.hbrs.team7.se1_starter_repo.servlets.ParkhausServlet
import jakarta.servlet.ServletConfig
import jakarta.servlet.ServletException
import jakarta.servlet.annotation.WebServlet

@WebServlet(name = "level1Servlet", value = ["/level1-servlet"])
public class Level1Servlet : ParkhausServlet()  {

    override var config = ParkhausEbeneConfigDTO(
        ebenenNamen = "Etage1",
        maxPlätze = 12,
        öffnungszeit = 6,
        ladenschluss = 24,
        verzögerung = 100,
        simulationsGeschwindigkeit = 5
    )

    @Throws(ServletException::class)
    override fun init(config: ServletConfig) {
        super.init(config)

        println("Etage 1 Servlet is Initialized")
    }

}