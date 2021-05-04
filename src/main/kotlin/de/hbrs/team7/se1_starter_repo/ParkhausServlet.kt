package de.hbrs.team7.se1_starter_repo

import jakarta.servlet.ServletContext
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.util.*

/**
 * common superclass for all servlets
 * groups all auxiliary common methods used in all servlets
 */
public abstract class ParkhausServlet : HttpServlet() {
    /* abstract methods, to be defined in subclasses */
    abstract fun NAME(): String // each ParkhausServlet should have a name, e.g. "Level1"
    abstract fun MAX(): Int // maximum number of parking slots of a single parking level
    abstract fun config(): String? // configuration of a single parking level

    //persistentSum: Summe der Parkgebühren aller Autos
    //totalCars: Counter, wie viele Autos jemals im Parkhaus waren

    /**
     * HTTP GET
     */
    @Throws(IOException::class)
    public override fun doGet(request: HttpServletRequest, response: HttpServletResponse) {
        response.contentType = "text/html"
        val out = response.writer
        val cmd = request.getParameter("cmd")
        println(cmd + " requested: " + request.queryString)
        when (cmd) {
            "config" ->                 // Overwrite Parkhaus config parameters
                // Max, open_from, open_to, delay, simulation_speed
                out.println(config())
            "sum" -> out.println(persistentSum)
            "cars" -> {
            }
            "chart" -> {
            }
            else -> println("Invalid Command: " + request.queryString)
        }
    }

    /**
     * HTTP POST
     */
    @Throws(ServletException::class, IOException::class)
    override fun doPost(request: HttpServletRequest, response: HttpServletResponse) {
        val body = getBody(request)
        response.contentType = "text/html"
        val out = response.writer
        println(body)
        val params = body.split(",".toRegex()).toTypedArray()
        val event = params[0]
        when (event) {
            "enter" -> {
                val newCar: CarIF = Car(params)
                cars().add(newCar)
                println("enter,$newCar")
                println("HELLO FROM KOOOOOTLIN")
                context.setAttribute("total_car_count" + NAME(), totalCars + 1)
                println(totalCars)
            }
            "leave" -> {
                val oldCar = cars()[0]
                if (params.size > 4) {
                    val priceString = params[4]
                    if ("_" != priceString) {
                        // for JSON format skip over text and proceed to next integer
                        var price = Scanner(priceString).useDelimiter("\\D+").nextInt().toFloat()
                        price /= 100.0f // like Integer.parseInt( priceString ) / 100.0f;
                        // store new sum in ServletContext
                        context.setAttribute("sum" + NAME(), price)
                        println("Der aktuelle Name ist: " + NAME())
                    }
                }
                println("leave,$oldCar")
                println(context.getAttribute("sum" + NAME()))
                println(persistentSum)
            }
            "invalid", "occupied" -> println(body)
            "Total" -> println(persistentSum)
            else -> println(body)
        }
    }
    // auxiliary methods used in HTTP request processing
    /**
     * @return the servlet context
     */
    val context: ServletContext
        get() = servletConfig.servletContext

    /**
     * TODO: replace this by your own function
     * @return the number of the free parking lot to which the next incoming car will be directed
     */

    internal fun locator(car: CarIF): Int {
        // numbers of parking lots start at 1, not zero
        print(car)
        return 1 + (cars().size - 1) % MAX()
    }

    /**
     * @return the list of all cars stored in the servlet context so far
     */
    @SuppressWarnings("unchecked")
    internal fun cars(): MutableList<CarIF> {
        if (context.getAttribute("cars" + NAME()) == null) {
            context.setAttribute("cars" + NAME(), ArrayList<Car>())
        }
        return context.getAttribute("cars" + NAME()) as MutableList<CarIF>
    }

    /**
     * TODO: replace this by your own function
     * @return the sum of parking fees of all cars stored so far
     */
    val persistentSum: Float
        get() {
            val sum = context.getAttribute("sum" + NAME()) as Float
            println("Log aus getPersistentSum: aktuelle Summe: $sum")
            return sum ?: 0.0f
        }

    val totalCars: Int
        get() {
            val total_car_count = context.getAttribute("total_car_count" + NAME()) as Int
            return total_car_count ?: 0
        }

    /**
     * @param request the HTTP POST request
     * @return the body of the request
     */
    @Throws(IOException::class)
    fun getBody(request: HttpServletRequest): String {
        val stringBuilder = StringBuilder()
        var bufferedReader: BufferedReader? = null
        try {
            val inputStream: InputStream? = request.inputStream
            if (inputStream != null) {
                bufferedReader = BufferedReader(InputStreamReader(inputStream))
                val charBuffer = CharArray(128)
                var bytesRead = -1
                while (bufferedReader.read(charBuffer).also { bytesRead = it } > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead)
                }
            } else {
                stringBuilder.append("")
            }
        } finally {
            bufferedReader?.close()
        }
        return stringBuilder.toString()
    }

    override fun destroy() {
        println("Servlet destroyed.")
    }
}