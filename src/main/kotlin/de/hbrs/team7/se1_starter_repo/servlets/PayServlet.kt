package de.hbrs.team7.se1_starter_repo.servlets

import de.hbrs.team7.se1_starter_repo.services.DatabaseServiceGlobal
import de.hbrs.team7.se1_starter_repo.services.ParkhausServiceGlobal
import de.hbrs.team7.se1_starter_repo.services.ParkhausServiceSession
import jakarta.inject.Inject
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;


@WebServlet(name = "PayServlet", value = ["/PayServlet"])
class PayServlet : HttpServlet() {
    @Inject
    private lateinit var parkhausServiceGlobal: ParkhausServiceGlobal

    @Inject
    private lateinit var DatabaseGlobal: DatabaseServiceGlobal

    @Inject
    private lateinit var parkhausServiceSession: ParkhausServiceSession

    private lateinit var message: String

    override fun init() {
        message = "Hello World!"
    }

    public override fun doGet(request: HttpServletRequest, response: HttpServletResponse) {
        response.contentType = "text/html"

        // Hello
        val out = response.writer
        out.println("<html lang=\"de\"><head><link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-+0n0xVW2eSR5OomGNYDnhzAbDsOXxcvSN1TPprVMTNDbiYZCxYbOOl7+AMvyTG2x\" crossorigin=\"anonymous\">\n" +
                "   <script src=\"https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/js/bootstrap.bundle.min.js\" integrity=\"sha384-gtEjrD/SeCtmISkJkNUaaKMoLD0//ElJ19smozuHV6z3Iehds+3Ulb9Bn9Plx0x4\" crossorigin=\"anonymous\"></script>\n" +
                "   </head><body><p>Get-Befehl ausgeführt</p>")

        val ebene = request.getParameter("ebenenNummer").replace("_"," ")
        val parkplatzNummer = request.getParameter("parkplatzNummer").toInt()
        val data = parkhausServiceSession.generiereKassenAusgabe(ebene,parkplatzNummer)
        out.println("<div >$data</div>")
        out.println("<a href=\"index.jsp\" class=\"btn btn-primary my-2\">Zurück zum Parkhaus</a>")
        out.println("</body></html>")
    }



    override fun destroy() {
        println("Destroyed Pay Servlet");
        super.destroy()
    }
}