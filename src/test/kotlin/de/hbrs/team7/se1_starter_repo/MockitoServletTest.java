package de.hbrs.team7.se1_starter_repo;

import de.hbrs.team7.se1_starter_repo.dto.ParkhausEbeneConfigDTO;
import de.hbrs.team7.se1_starter_repo.servlets.Level1Servlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class MockitoServletTest {

    HttpServletRequest req;
    HttpServletResponse res;
    StringWriter sw;

    @BeforeEach
    public void init() throws IOException {
        req = Mockito.mock(HttpServletRequest.class);
        res = Mockito.mock(HttpServletResponse.class);
        sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        when(res.getWriter()).thenReturn(pw);

    }
    @Test
    @DisplayName("Generelle Funktionalität der Get-Requests testen")
    public void testDoGet() throws IOException {

        when(req.getParameter("cmd")).thenReturn("Test");
        when(req.getQueryString()).thenReturn("Test");
        new Level1Servlet().doGet(req,res);
        assertEquals("Invalid Command: Test".trim(), sw.toString().trim());

    }

    @Test
    @DisplayName("Test der Config-Funktion")
    public void configTest() {
        when(req.getParameter("cmd")).thenReturn("Config");
        when(req.getQueryString()).thenReturn("Config");
        try {
            new Level1Servlet().doGet(req,res);
        } catch (IOException e) {
            System.out.println("IOException aufgetreten!");
        }
        HashMap map = new HashMap();
        map.put("max",12);
        map.put("open_from",6);
        map.put("open_to",24);
        map.put("delay",200);
        map.put("time_shift",0);
        map.put("simulation_speed",1);

      String formattedMap = map.toString();
      formattedMap.replace("{","{\"");
      formattedMap.replace(":","\":");
      formattedMap.replace(",",",\"");
      System.out.println(formattedMap);
      assertEquals(formattedMap.trim(),sw.toString().trim());
    }

}

