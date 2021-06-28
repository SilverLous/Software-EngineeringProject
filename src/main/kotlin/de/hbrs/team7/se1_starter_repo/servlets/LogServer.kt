package de.hbrs.team7.se1_starter_repo.servlets

import de.hbrs.team7.se1_starter_repo.BasicWebsocketIF
import de.hbrs.team7.se1_starter_repo.dto.LogEintragDTO
import de.hbrs.team7.se1_starter_repo.dto.ManagerStatistikUpdateDTO
import de.hbrs.team7.se1_starter_repo.services.LoggerServiceGlobal
import de.hbrs.team7.se1_starter_repo.services.ParkhausServiceGlobal
import jakarta.enterprise.inject.spi.CDI
import jakarta.websocket.*
import jakarta.websocket.server.ServerEndpoint
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.concurrent.TimeUnit


@ServerEndpoint("/log" )
public open class LogServer : BasicWebsocketIF {
    override val sessions = mutableSetOf<Session>()

    // https://stackoverflow.com/questions/51175990/using-cdi-injection-in-tomcat-websocket-serverendpoint
    private var logService: LoggerServiceGlobal =
        CDI.current().select(LoggerServiceGlobal::class.java).get()



    init {
        logService.logSubject.
            subscribe { it -> broadcastMessage(Json.encodeToString(it)) }
    }



}