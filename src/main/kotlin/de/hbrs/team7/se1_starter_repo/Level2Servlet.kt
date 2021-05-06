package de.hbrs.team7.se1_starter_repo

import jakarta.servlet.annotation.WebServlet
import jakarta.inject.Inject


@WebServlet(name = "level2Servlet", value = ["/level2-servlet"])
class Level2Servlet : ParkhausServlet() {

    override fun NAME(): String {
        return "Level2 Kt"
    }

    override fun MAX(): Int { // maximum number of parking slots on level 2
        return 12
    }

    override fun config(): String {
        return "" // use default config
        // Config Format is "Max, open_from, open_to, delay, simulation_speed"
        // e.g. return this.MAX() + ",10,20,50,20";  // TODO replace by your own parameters
    }
}