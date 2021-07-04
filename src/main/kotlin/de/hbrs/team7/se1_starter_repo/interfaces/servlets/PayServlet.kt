package de.hbrs.team7.se1_starter_repo.interfaces.servlets

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;


@WebServlet(name = "helloServlet", value = ["/hello-servlet"])
class PayServlet : HttpServlet() {
    private lateinit var message: String

    override fun init() {
        message = "Hello World!"
    }

    public override fun doGet(request: HttpServletRequest, response: HttpServletResponse) {
        response.contentType = "text/html"

        // Hello
        val out = response.writer
        out.println("<html><body>")
        out.println("<h1>$message</h1>")
        out.println("</body></html>")
    }

    override fun destroy() {
        println("Destroyed Pay Servlet");
        super.destroy()
    }
}