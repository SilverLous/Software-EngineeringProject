package de.hbrs.team7.se1_starter_repo


import de.hbrs.team7.se1_starter_repo.dto.ParkhausServletPostDto
import de.hbrs.team7.se1_starter_repo.dto.carData
import de.hbrs.team7.se1_starter_repo.dto.statisticsChartDto
import de.hbrs.team7.se1_starter_repo.entities.ParkhausEbene
import jakarta.inject.Inject
import jakarta.servlet.ServletConfig
import jakarta.servlet.ServletContext
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.util.ArrayList


/**
 * common superclass for all servlets
 * groups all auxiliary common methods used in all servlets
 */


public abstract class ParkhausServlet : HttpServlet() {


    /* abstract methods, to be defined in subclasses */
    abstract fun NAME(): String // each ParkhausServlet should have a name, e.g. "Level1"
    abstract fun MAX(): Int // maximum number of parking slots of a single parking level
    abstract fun config(): String? // configuration of a single parking level


    private lateinit var parkhausEbene: ParkhausEbene


    @Inject
    private lateinit var parkhausServiceGlobal: ParkhausServiceGlobal

    @Inject private lateinit var DatabaseGlobal: DatabaseServiceGlobal

    @Inject private lateinit var parkhausServiceSession: ParkhausServiceSession

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


    @Throws(ServletException::class)
    override fun init(config: ServletConfig) {
        super.init(config)

        println("Hello Parkhaus ${NAME()} Base")


        setInitContext("carsHaveLeft" + NAME(), 0)
        setInitContext("totalCarCount" + NAME(), 0)
        setInitContext("sum" + NAME(), 0f)

        println(context.getAttribute("carsHaveLeft" + NAME()))

        this.parkhausEbene = this.parkhausServiceSession.initEbene(NAME())
        print(this.parkhausEbene)


        /*
        kotlin.assert(context.getAttribute("carsHaveLeft" + NAME()) == 0)
        kotlin.assert(context.getAttribute("totalCarCount" + NAME()) == 0)
        kotlin.assert(context.getAttribute("sum" + NAME()) == 0f)

        kotlin.assert(parkhausServiceGlobal.globalCars == 0)

        // get is used for getting the one instance of the running service
        kotlin.assert(parkhausServiceSession.get().sessionCars == 0)*/

        /*
        val ticket = Ticket()
        ticket.Ausstellungsdatum = Date.from(Instant.now())
        ticket.Preisklasse = 2

        val created = this.DatabaseGlobal.persistTicket(ticket)

        val test = this.DatabaseGlobal.queryAllEntities(Ticket::class.java)

        print(created)
        print(test)

         */

    }

    public override fun destroy() {
        println("Destroyed Parkhaus ${NAME()} Base")
        super.destroy()
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

        when (cmd.lowercase()) {
            "config" ->
                // Overwrite Parkhaus config parameters
                // Max, open_from, open_to, delay, simulation_speed
                out.println(config())

            "sum" -> out.println("${parkhausServiceSession.sumOverAllCars(NAME()).format(2)}€")

            "cars" -> {
                // TODO: Send list of cars stored on the server to the client.
                // Cars are separated by comma.
                // Values of a single car are separated by slash.
                // Format: Nr, timer begin, duration, price, Ticket, color, space, client category, vehicle type, license (PKW Kennzeichen)
                // For example:
                // out.println("1/1619420863044/_/_/Ticket1/#0d1e0a/2/any/PKW/1,2/1619420863045/_/_/Ticket2/#dd10aa/3/any/PKW/2"); // TODO replace by real list of cars

            }
            "chart" -> {
                // http://json-b.net/docs/user-guide.html

                // https://github.com/Kotlin/kotlinx.serialization
                response.contentType = "application/json;charset=UTF-8"
                val carTypeSum = parkhausServiceSession.sumOverCarTypes(NAME())
                val cars = carTypeSum.keys.toList()
                val sumOver = ArrayList<Double>(carTypeSum.values)
                val tempData = carData("bar", cars as List<String>,sumOver)
                val dataList = arrayListOf(tempData)
                val jsonData = Json.encodeToJsonElement(statisticsChartDto(dataList))
                out.print(jsonData)
            }

            "average" ->  out.println("${parkhausServiceSession.average(NAME())?.format(2)} € per car")


            "total users" -> out.println(parkhausServiceSession.totalUsers(NAME()).toString() + " Users")

            else -> out.println("Invalid Command: " + request.queryString)
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
        val paramJson = body.split(",", limit = 2).toTypedArray()
        val event = params[0]
        when (event) {
            "enter" -> {
                val data = Json.decodeFromString<ParkhausServletPostDto>(paramJson[1])

                parkhausServiceSession.addCarLegacy(NAME(), data)
                //parkhausServiceSession.get().addCar(NAME(),params)
                // re-direct car to another parking lot
                // out.println( locator( newCar ) );
            }
            "leave" -> {/*
                val oldCar = cars()?.get(0)
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
                println(persistentAvg)*/
                val data = Json.decodeFromString<ParkhausServletPostDto>(paramJson[1])
                parkhausServiceSession.leaveCar(NAME(), data)
                //parkhausServiceSession.get().leaveCar(NAME(),params)

            }
            "invalid", "occupied" -> {
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
    /*internal fun locator(car: CarIF): Int {
        // numbers of parking lots start at 1, not zero
        print(car)
        return 1 + ((cars()?.size ?: 1) - 1) % MAX()
    }*/

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
    /*private fun cars(): ArrayList<CarIF>? {/*
        if (context.getAttribute("cars" + NAME()) == null) {
            context.setAttribute("cars" + NAME(), java.util.ArrayList<Car>())
        }
        return context.getAttribute("cars" + NAME()) as ArrayList<CarIF>*/
        return parkhausServiceSession.get().currentCars(NAME())
    }*/


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

    fun Double.format(digits: Int) = "%.${digits}f".format(this)

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

}


