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
import kotlin.collections.ArrayList

/**
 * common superclass for all servlets
 * groups all auxiliary common methods used in all servlets
 */

public abstract class ParkhausServlet : HttpServlet() {
    /* abstract methods, to be defined in subclasses */
    abstract fun NAME(): String // each ParkhausServlet should have a name, e.g. "Level1"
    abstract fun MAX(): Int // maximum number of parking slots of a single parking level
    abstract fun config(): String? // configuration of a single parking level


    /*Kontextvariablen:
    persistentSum: Summe der Parkgebühren aller Autos
    totalCars: Counter, wie viele Autos jemals im Parkhaus waren
    carsHaveLeft: Autos, die das Parkhaus verlassen haben
    */

    private fun <T> setInitContext(value: String, init: T) {
        if (context.getAttribute(value) == null) {
            context.setAttribute(value, init)
        }
    }


    override fun init() {
        super.init()
        print("Hello from the other side\n")
        setInitContext("carsHaveLeft" + NAME(), 0)
        setInitContext("totalCarCount" + NAME(), 0)
        setInitContext("sum" + NAME(), 0f)

        println(context.getAttribute("carsHaveLeft" + NAME()))
        kotlin.assert(context.getAttribute("carsHaveLeft" + NAME()) == 0)
        kotlin.assert(context.getAttribute("totalCarCount" + NAME()) == 0)
        kotlin.assert(context.getAttribute("sum" + NAME()) == 0f)


    }

    /**
     * HTTP GET
     */
    @Throws(IOException::class)
    public override fun doGet(request: HttpServletRequest, response: HttpServletResponse) {
        response.contentType = "text/html"
        val out = response.writer
        // val == final
        // var == variable
        val cmd = request.getParameter("cmd")
        println("$cmd + requested:" + request.queryString)

        when (cmd.toLowerCase()) {
            "config" ->
                // Overwrite Parkhaus config parameters
                // Max, open_from, open_to, delay, simulation_speed
                out.println(config())
            "sum" -> out.println("$persistentSum €")
            "cars" -> {
                // TODO: Send list of cars stored on the server to the client.
                // Cars are separated by comma.
                // Values of a single car are separated by slash.
                // Format: Nr, timer begin, duration, price, Ticket, color, space, client category, vehicle type, license (PKW Kennzeichen)
                // For example:
                // out.println("1/1619420863044/_/_/Ticket1/#0d1e0a/2/any/PKW/1,2/1619420863045/_/_/Ticket2/#dd10aa/3/any/PKW/2"); // TODO replace by real list of cars

            }
            "chart" -> {
                // TODO send chart infos as JSON object to client
                // http://json-b.net/docs/user-guide.html
            }

            "average" -> {
                println("persistentAvg");
                println(persistentAvg);
                out.println("$persistentAvg € über $totalCars Autos");
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

        // toTypedArray() needed because return type is List not array as in original
        val params = body.split(",").toTypedArray()

        val event = params[0]
        when (event) {
            "enter" -> {
                val newCar: CarIF = Car(params)
                cars().add(newCar)
                println("enter,$newCar")
                println("HELLO FROM KOOOOOTLIN")
                context.setAttribute("totalCarCount" + NAME(), totalCars + 1)
                println(totalCars)
                // re-direct car to another parking lot
                // out.println( locator( newCar ) );
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
                        context.setAttribute("sum" + NAME(), persistentSum + price)
                        println("Der aktuelle Name ist: " + NAME())
                    }
                    // TODO check if this is the right bracket
                    context.setAttribute(
                        "carsHaveLeft" + NAME(),
                        context.getAttribute("carsHaveLeft" + NAME()) as Int + 1
                    )

                }
                println("leave,$oldCar")
                println(context.getAttribute("sum" + NAME()))
                println(persistentSum)
                println(persistentAvg)
            }


            "invalid", "occupied" -> {
                context.setAttribute("totalCarCount" + NAME(), totalCars - 1)
                println(body)
            }

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

    //@SuppressWarnings("unchecked")
    internal var cars: ArrayList<CarIF>
    get() {
    /*if (context.getAttribute("cars" + NAME()) == null) {
    context.setAttribute("cars" + NAME(), ArrayList<Car>())
    }*/
    return (context.getAttribute("cars" + NAME()) as ArrayList<CarIF>?) ?: ArrayList<CarIF>();
    }
    set(value) {
    //TODO possible validation
    context.setAttribute("cars" + NAME(), value);
    }
     */

    /**
     * @return the list of all cars stored in the servlet context so far
     */
    private fun cars(): ArrayList<CarIF> {
        if (context.getAttribute("cars" + NAME()) == null) {
            context.setAttribute("cars" + NAME(), java.util.ArrayList<Car>())
        }
        return context.getAttribute("cars" + NAME()) as ArrayList<CarIF>
    }


    /**
     * TODO: replace this by your own function
     * @return the sum of parking fees of all cars stored so far
     */
    val persistentSum: Float
        get() {
            val sum = context.getAttribute("sum" + NAME()) as Float?
            println("Log aus getPersistentSum: aktuelle Summe: $sum")
            // instead of return sum == null ?  0.0f : sum;
            return sum ?: 0.0f
        }

    val totalCars: Int
        get() {
            val totalCarCount = context.getAttribute("totalCarCount" + NAME()) as Int?
            return totalCarCount ?: 0
        }

    val persistentAvg: Float
        //get() = persistentSum / (totalCars - (cars().size - 1)) // Average nur über die bereits ausgefahrenen machen LUKAS!
        get() {
            return persistentSum / (context.getAttribute("carsHaveLeft" + NAME()) as Int)
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

