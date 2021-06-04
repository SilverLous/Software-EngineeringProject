package de.hbrs.team7.se1_starter_repo.servlets


import de.hbrs.team7.se1_starter_repo.services.ParkhausServiceGlobal
import de.hbrs.team7.se1_starter_repo.services.ParkhausServiceSession
import de.hbrs.team7.se1_starter_repo.dto.ParkhausServletPostDto
import de.hbrs.team7.se1_starter_repo.dto.carData
import de.hbrs.team7.se1_starter_repo.dto.statisticsChartDto
import de.hbrs.team7.se1_starter_repo.entities.ParkhausEbene
import de.hbrs.team7.se1_starter_repo.services.DatabaseServiceGlobal
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

    @Inject private lateinit var context: ServletContext


    @Throws(ServletException::class)
    override fun init(config: ServletConfig) {
        super.init(config)

        println("Hello Parkhaus ${NAME()} Base")


        println(context.getAttribute("carsHaveLeft" + NAME()))


        this.parkhausEbene = this.parkhausServiceSession.initEbene(NAME())

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

            "sum" -> {
                //TODO implement sumOverAllCars
            }

            "cars" -> {
                // TODO: Send list of cars stored on the server to the client.
                // Cars are separated by comma.
                // Values of a single car are separated by slash.
                // Format: Nr, timer begin, duration, price, Ticket, color, space, client category, vehicle type, license (PKW Kennzeichen)
                // For example:
                // out.println("1/1619420863044/_/_/Ticket1/#0d1e0a/2/any/PKW/1,2/1619420863045/_/_/Ticket2/#dd10aa/3/any/PKW/2");
                // TODO replace by real list of cars

            }
            "chart" -> {
                // TODO: fill with data
                // http://json-b.net/docs/user-guide.html

                // https://github.com/Kotlin/kotlinx.serialization
                response.contentType = "application/json;charset=UTF-8"
                val sumOver = ArrayList<Double>()
                val tempData = carData("bar", sumOver as List<String>,sumOver)
                val dataList = arrayListOf(tempData)
                val jsonData = Json.encodeToJsonElement(statisticsChartDto(dataList))
                out.print(jsonData)
            }
            //TODO: fix when corresponding functions are implemented again
            /*"average" ->  out.println("${parkhausServiceSession.average(NAME())?.format(2)} € per car")


            "total users" -> out.println(parkhausServiceSession.totalUsers(NAME()).toString() + " Users")*/

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
        val paramJson = body.split(",", limit = 2).toTypedArray()
        val event = paramJson[0]
        when (event) {
            "enter" -> {
                // val data = Json.decodeFromString<ParkhausServletPostDto>(paramJson[1])
                //TODO: implement addCar
                // parkhausServiceSession.generateTicket(NAME(),data)
            }
            "leave" -> {
                //TODO: implement leaveCar
            }
            "invalid", "occupied" -> {
                println(body)
            }

            else -> println(body)
        }
    }

    // Hilfsfunktion zum formatieren
    fun Double.format(digits: Int) = "%.${digits}f".format(this)

    // auxiliary methods used in HTTP request processing
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


