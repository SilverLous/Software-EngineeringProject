package de.hbrs.team7.se1_starter_repo

import jakarta.websocket.*
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import java.io.IOException

interface BasicWebsocketIF {
    val sessions: MutableSet<Session>

    @OnOpen
    fun onOpen(session: Session) {
        println("WebSocket opened: " + session.id)
        sessions.add(session)
        println("WebSockets active: " + sessions.size)
    }

    @OnMessage
    fun handleMessage(message: String): String {
        return "Got your message ($message). Thanks !"
    }

    fun broadcastMessage(message: String) {
        for (session in sessions) {
            try {
                session.basicRemote.sendText(message)
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: EncodeException) {
                e.printStackTrace()
            }
        }
    }

    @OnClose
    fun onClose(session: Session, closeReason: CloseReason) {
        println(
            "WebSocket closed for " + session.id
                    + " with reason " + closeReason.closeCode
        )
        sessions.remove(session)
    }
}