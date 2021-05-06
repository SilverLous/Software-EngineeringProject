package de.hbrs.team7.se1_starter_repo


import jakarta.inject.Inject
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException
import java.io.PrintWriter


@WebServlet(name = "helloServletbak", value = ["/hello-servletbak"])
class HelloServlet_bak : HttpServlet() {
    private var message: String? = null

    //@Inject
    //private var parkhausService: ParkhausService? = null
    override fun init() {
        message = "Hello World!"
    }

    @Throws(IOException::class)
    public override fun doGet(request: HttpServletRequest, response: HttpServletResponse) {
        response.contentType = "text/html"

        // Hello
        var out: PrintWriter? = null
        try {
            out = response.writer
        } catch (e: IOException) {
            e.printStackTrace()
        }
        out!!.println("<html><body>")
        //out.println("<h1>" + parkhausService!!.init + "</h1>")
        out.println("</body></html>")
    }

    override fun destroy() {}
}