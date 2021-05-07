package de.hbrs.team7.se1_starter_repo;

import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import java.io.IOException;
import java.io.PrintWriter;

// @ManagedBean
@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    private String message;

    @Inject Instance<ParkhausServiceSession> parkhausServiceSession;
    @Inject ParkhausServiceGlobal parkhausServiceGlobal;

    // public void setParkhausService ( ParkhausService pS) { this.parkhausService = pS; }

    public void init() {
        message = "Hello World!";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        // Hello
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.println("<html><body>");
        try {
            out.println("<h1>" + parkhausServiceGlobal.getGlobalCars() + "</h1>");
            out.println("<h1>" + parkhausServiceSession.get().getSessionCars() + "</h1>");
        } catch (Exception e) {
            e.printStackTrace();
        }
        out.println("</body></html>");
    }

    public void destroy() {
    }
}