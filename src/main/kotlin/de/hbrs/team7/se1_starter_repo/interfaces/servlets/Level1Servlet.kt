package de.hbrs.team7.se1_starter_repo.interfaces.servlets

import de.hbrs.team7.se1_starter_repo.dto.ParkhausEbeneConfigDTO
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
        verzögerung = 200,
        simulationsGeschwindigkeit = 1,
        FahrzeugPreise = hashMapOf(
            "PKW" to 1.0,
            "Pickup" to 1.0,
            "SUV" to 1.5,
            "Zweirad" to 0.5,
            "Trike" to 1.3,
            "Quad" to 1.4,
        )
    )

    @Throws(ServletException::class)
    override fun init(config: ServletConfig) {
        super.init(config)

        println("Etage 1 Servlet is Initialized")
    }

}