package de.hbrs.team7.se1_starter_repo;

import de.hbrs.team7.se1_starter_repo.servlets.Level1Servlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

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
    public void testDoGet() throws IOException {

        when(req.getParameter("cmd")).thenReturn("Test");
        when(req.getQueryString()).thenReturn("Test");
        new Level1Servlet().doGet(req,res);
        assertEquals("Invalid Command: Test".trim(), sw.toString().trim());

    }

}

