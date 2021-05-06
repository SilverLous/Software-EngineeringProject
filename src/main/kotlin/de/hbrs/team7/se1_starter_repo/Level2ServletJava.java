package de.hbrs.team7.se1_starter_repo;

import jakarta.servlet.annotation.WebServlet;

@WebServlet(name = "level2Servlet", value = "/level2-servlet")
public class Level2ServletJava extends ParkhausServletJava {



    @Override
    public String NAME(){
        return "Level2";
    }

    @Override
    public int MAX(){ // maximum number of parking slots on level 2
        return 12;
    }

    @Override
    public String config(){
        return ""; // use default config
        // Config Format is "Max, open_from, open_to, delay, simulation_speed"
        // e.g. return this.MAX() + ",10,20,50,20";  // TODO replace by your own parameters
    }


}