package de.hbrs.team7.se1_starter_repo;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "level1Servlet", value = "/level1-servlet")
public class Level1Servlet extends ParkhausServlet {

    @Override
    String NAME(){
        return "Level1";
    }

    @Override
    int MAX(){ // maximum number of parking slots on level 1
        return 11;
    }

    @Override
    String config(){
        return ""; // use default config
        // Config Format is "Max, open_from, open_to, delay, simulation_speed"
        // e.g. return this.MAX() + ",5,23,100,10";  // TODO replace by your own parameters
    }



}