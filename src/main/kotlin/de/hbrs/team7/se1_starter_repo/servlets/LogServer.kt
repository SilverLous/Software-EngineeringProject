package de.hbrs.team7.se1_starter_repo.servlets

import de.hbrs.team7.se1_starter_repo.dto.LogEintragDTO
import de.hbrs.team7.se1_starter_repo.interfaces.BasicWebsocketIF
import de.hbrs.team7.se1_starter_repo.services.LoggerServiceGlobal
import jakarta.enterprise.inject.spi.CDI
import jakarta.websocket.OnOpen
import jakarta.websocket.Session
import jakarta.websocket.server.ServerEndpoint
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


/**
 *
 * Das Websocket für die Aktualisierungen des Logs im Klienten
 *
 * @author Thomas Gerlach
 */
@ServerEndpoint("/log")
open class LogServer : BasicWebsocketIF {
    override val sessions = mutableSetOf<Session>()

    // https://stackoverflow.com/questions/51175990/using-cdi-injection-in-tomcat-websocket-serverendpoint
    private var logService: LoggerServiceGlobal =
        CDI.current().select(LoggerServiceGlobal::class.java).get()


    init {
        logService.logSubject.doOnNext { it -> println(Json.encodeToString(it)) }
            .subscribe { it -> broadcastMessage(Json.encodeToString(it)) }

    }

    @OnOpen
    override fun onOpen(session: Session) {
        if (session !in sessions) {
            val missedLogs = logService.logSubject.values.toList() as List<LogEintragDTO>
            session.basicRemote.sendText(Json.encodeToString(missedLogs))
            super.onOpen(session)
        }

    }

}