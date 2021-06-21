package de.hbrs.team7.se1_starter_repo.servlets

import de.hbrs.team7.se1_starter_repo.BasicWebsocketIF
import jakarta.websocket.*
import jakarta.websocket.server.ServerEndpoint
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import java.io.IOException
import java.util.*

@Serializable
data class Test(val name: String, val language: String)

@ServerEndpoint("/manager" )
class ManagerServer : BasicWebsocketIF {
    override val sessions = mutableSetOf<Session>()

    init {
        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {

                val ret = Test("Test", "de")
                broadcastMessage(Json.encodeToString(ret))

            }
        }, 0, 10000)
    }



}