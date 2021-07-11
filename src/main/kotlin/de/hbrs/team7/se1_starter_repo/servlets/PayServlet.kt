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
        val ebene = request.getParameter("ebenenNummer").replace("_"," ")
        val parkplatzNummer = request.getParameter("parkplatzNummer").toInt()
        val data = parkhausServiceSession.generiereKassenAusgabe(ebene,parkplatzNummer)
        out.println("<div >$data</div>")

    }



    override fun destroy() {
        println("Destroyed Pay Servlet");
        super.destroy()
    }
}