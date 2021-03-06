package de.hbrs.team7.se1_starter_repo.servlets


import de.hbrs.team7.se1_starter_repo.dto.ParkhausEbeneConfigDTO
import de.hbrs.team7.se1_starter_repo.dto.ParkhausServletPostDto
import de.hbrs.team7.se1_starter_repo.entities.ParkhausEbene
import de.hbrs.team7.se1_starter_repo.services.DatabaseServiceGlobal
import de.hbrs.team7.se1_starter_repo.services.ParkhausServiceGlobal
import de.hbrs.team7.se1_starter_repo.services.ParkhausServiceSession
import jakarta.inject.Inject
import jakarta.servlet.ServletConfig
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.time.Instant
import java.util.*


/**
 * common superclass for all servlets
 * groups all auxiliary common methods used in all servlets
 */


abstract class ParkhausServlet : HttpServlet() {

    internal abstract var config: ParkhausEbeneConfigDTO // configuration of a single parking level

    private lateinit var parkhausEbene: ParkhausEbene


    @Inject
    private lateinit var parkhausServiceGlobal: ParkhausServiceGlobal

    @Inject
    private lateinit var DatabaseGlobal: DatabaseServiceGlobal

    @Inject
    private lateinit var parkhausServiceSession: ParkhausServiceSession


    @Throws(ServletException::class)
    override fun init(config: ServletConfig) {
        super.init(config)

        println("Hello Parkhaus ${this.config.ebenenNamen} Base")


        // this.parkhausEbene = this.parkhausServiceSession.initEbene(NAME())
        this.parkhausEbene = this.parkhausServiceSession.initEbene(this.config)


    }

    override fun destroy() {
        println("Destroyed Parkhaus ${this.config.ebenenNamen} Base")
        super.destroy()
    }

    /**
     * HTTP GET
     */
    @Throws(IOException::class)
    public override fun doGet(request: HttpServletRequest, response: HttpServletResponse) {
        val charset = "application/json;charset=UTF-8"
        response.contentType = "text/html"
        val out = response.writer
        // val == final
        // var == variable
        val cmd = request.getParameter("cmd")
        println("$cmd + requested:" + request.queryString)

        when (cmd.lowercase()) {
            "config" -> {
                // Overwrite Parkhaus config parameters
                // Max, open_from, open_to, delay, simulation_speed

                response.contentType = charset

                val ebeneDTO: ParkhausEbeneConfigDTO =
                    parkhausServiceSession.getParkhausEbenen().find { pe -> pe.name == config.ebenenNamen }!!
                        .toConfigDTO()
                val jsonData = Json.encodeToJsonElement(ebeneDTO)
                out.print(jsonData)

            }
            "sum" -> {
                out.print("${parkhausServiceSession.getSummeTicketpreiseUeberAutos(config.ebenenNamen) / 100} ??? insgesamt")
            }

            "cars" -> {
                val ebene = request.getParameter("name") ?: config.ebenenNamen

                out.println(parkhausServiceSession.getPrintStringAutos(ebene))

                // Format: Nr, timer begin, duration, price, Ticket, color, space, client category, vehicle type, license (PKW Kennzeichen)
                // For example:
                //out.println("1/1619420863044/_/_/Ticket1/#0d1e0a/2/any/PKW/1,2/1619420863045/_/_/Ticket2/#dd10aa/3/any/PKW/2");


            }
            "einnahmenueberautotyp" -> {
                // http://json-b.net/docs/user-guide.html

                // https://github.com/Kotlin/kotlinx.serialization
                response.contentType = charset

                val jsonData =
                    Json.encodeToJsonElement(parkhausServiceSession.erstelleStatistikenUeberFahrzeuge(config.ebenenNamen))
                out.print(jsonData)
            }
            "tageseinnahmen" -> {
                response.contentType = charset

                val jsonData = Json.encodeToJsonElement(parkhausServiceSession.getTageseinnahmen(config.ebenenNamen))
                out.print(jsonData)
            }
            "wocheneinnahmen" -> {
                response.contentType = charset

                val jsonData = Json.encodeToJsonElement(parkhausServiceSession.getWocheneinnahmen(config.ebenenNamen))
                out.print(jsonData)
            }
            "einnahmenueberbundesland" -> {
                // http://json-b.net/docs/user-guide.html

                // https://github.com/Kotlin/kotlinx.serialization
                response.contentType = charset

                val jsonData = Json.encodeToJsonElement(parkhausServiceSession.erstellePreiseFuerBundeslaender())
                out.print(jsonData)
            }
            "daueruebertyp" -> {
                // http://json-b.net/docs/user-guide.html

                // https://github.com/Kotlin/kotlinx.serialization
                response.contentType = charset

                val jsonData =
                    Json.encodeToJsonElement(parkhausServiceSession.erstelleDauerUeberFahrzeugTyp(config.ebenenNamen))
                out.print(jsonData)
            }

            "average" -> out.println("${parkhausServiceSession.getDurchschnittUeberAutos(config.ebenenNamen) / 100} ??? per car")


            "total users" -> out.println("${parkhausServiceSession.getAlleUser(config.ebenenNamen)} Users")
            "wechsleparkhaus" -> {
                println("ParkhausWechsel")
                val stadtId = request.getParameter("stadt")
                this.parkhausServiceSession.ladeParkhausStadt(stadtId.toLong())
                out.write("Ort gewechselt!")
            }

            "undo" -> {
                parkhausServiceSession.undo()
            }

            "redo" -> {
                parkhausServiceSession.redo()
            }
            "preistabelle" -> {
                response.contentType = charset
                val ebenenZahl = Integer.parseInt(request.getParameter("ebene"))
                val jsonData =
                    Json.encodeToJsonElement(parkhausServiceSession.getFahrzeugmultiplikatorenDTO(ebenenZahl))
                out.print(jsonData)
            }

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
        val paramsCSV = body.split(",").toTypedArray()
        when (paramJson[0]) {
            "enter" -> {
                val data = Json.decodeFromString<ParkhausServletPostDto>(paramJson[1])
                val ticket = parkhausServiceSession.erstelleTicket(config.ebenenNamen, data)
                parkhausServiceSession.loescheRedoList()
                out.write(Json.encodeToString(ticket.zuParkhausServletPostDto()))

            }
            "leave" -> {
                val data = Json.decodeFromString<ParkhausServletPostDto>(paramJson[1])
                val zustaendigesTicket =
                    parkhausServiceSession.findeTicketUeberParkplatz(config.ebenenNamen, data.space)!!
                parkhausServiceSession.loescheRedoList()
                val ticketPreis = parkhausServiceSession.ticketBezahlen(
                    config.ebenenNamen,
                    zustaendigesTicket,
                    Date.from(Instant.now())
                )
                out.write(ticketPreis.toString())
            }

            "change_max" -> {
                parkhausServiceSession.wechsleEbeneMaxParkplaetze(
                    config.ebenenNamen,
                    aktuell = paramsCSV[1].toInt(),
                    neu = paramsCSV[2].toInt()
                )
                parkhausServiceSession.loescheRedoList()
                println(paramJson)
            }

            "change_open_from" -> {
                parkhausServiceSession.wechsleEbeneOeffnungszeit(
                    config.ebenenNamen,
                    aktuell = paramsCSV[1].toInt(),
                    neu = paramsCSV[2].toInt()
                )
                parkhausServiceSession.loescheRedoList()
                println(paramJson)
            }

            "change_open_to" -> {
                parkhausServiceSession.wechsleEbeneLadenschluss(
                    config.ebenenNamen,
                    aktuell = paramsCSV[1].toInt(),
                    neu = paramsCSV[2].toInt()
                )
                parkhausServiceSession.loescheRedoList()
                println(paramJson)
            }

            "full" -> {
                println(paramJson)
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
                var bytesRead: Int
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


