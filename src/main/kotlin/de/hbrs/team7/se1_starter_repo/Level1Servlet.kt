package de.hbrs.team7.se1_starter_repo

import jakarta.servlet.annotation.WebServlet
import javax.inject.Inject

@WebServlet(name = "level1Servlet", value = ["/level1-servlet"])
public class Level1Servlet constructor(
    // https://jakarta.ee/specifications/cdi/2.0/cdi-spec-2.0.html#part_3
    @Inject
    override val parkhausService: ParkhausService
) : ParkhausServlet()  {

    override fun NAME(): String {
        return "Level1 Kt"
    }

    override fun MAX(): Int { // maximum number of parking slots on level 1
        return 11
    }

    override fun config(): String {
        return "" // use default config
        // Config Format is "Max, open_from, open_to, delay, simulation_speed"
        // e.g. return this.MAX() + ",5,23,100,10";  // TODO replace by your own parameters
    }
}