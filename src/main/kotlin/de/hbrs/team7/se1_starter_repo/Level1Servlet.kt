package de.hbrs.team7.se1_starter_repo


import jakarta.inject.Inject
import jakarta.servlet.annotation.WebServlet


// import jakarta.inject.Inject

@WebServlet(name = "level1Servlet", value = ["/level1-servlet"])
public class Level1Servlet : ParkhausServlet()  {

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