package de.hbrs.team7.se1_starter_repo;

import jakarta.servlet.annotation.WebServlet;

@WebServlet(name = "level1Servlet", value = "/level1-servlet")
public class Level1ServletJava extends ParkhausServlet {

    @Override
    public String NAME(){
        return "Level1";
    }

    @Override
    public int MAX(){ // maximum number of parking slots on level 1
        return 11;
    }

    @Override
    public String config(){
        return ""; // use default config
        // Config Format is "Max, open_from, open_to, delay, simulation_speed"
        // e.g. return this.MAX() + ",5,23,100,10";  // TODO replace by your own parameters
    }



}