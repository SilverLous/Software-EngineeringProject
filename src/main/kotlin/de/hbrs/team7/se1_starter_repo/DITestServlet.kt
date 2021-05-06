package de.hbrs.team7.se1_starter_repo


import jakarta.inject.Inject
import jakarta.servlet.annotation.WebServlet
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import java.io.IOException
import java.io.PrintWriter


@WebServlet(name = "DITestServlet", value = ["/DITestServlet"])
public class DITestServlet : HttpServlet()  {

    //@Inject
    //private lateinit var parkhausService: ParkhausService

    @Throws(IOException::class)
    override fun doGet(request: HttpServletRequest?, response: HttpServletResponse) {
        response.contentType = "text/html"

        // Hello
        var out: PrintWriter? = null
        try {
            out = response.writer
        } catch (e: IOException) {
            e.printStackTrace()
        }
        out?.println("<html><body>")
        // out?.println("<h1>${parkhausService.init}</h1>")
        out?.println("</body></html>")
    }
}


