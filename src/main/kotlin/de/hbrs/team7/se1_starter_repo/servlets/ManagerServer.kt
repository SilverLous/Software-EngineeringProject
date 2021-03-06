package de.hbrs.team7.se1_starter_repo.servlets

import de.hbrs.team7.se1_starter_repo.dto.ManagerStatistikUpdateDTO
import de.hbrs.team7.se1_starter_repo.interfaces.BasicWebsocketIF
import de.hbrs.team7.se1_starter_repo.services.ParkhausServiceGlobal
import jakarta.enterprise.inject.spi.CDI
import jakarta.websocket.Session
import jakarta.websocket.server.ServerEndpoint
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.concurrent.TimeUnit

/**
 *
 * Das Websocket für die Aktualisierungen der Plots
 *
 * @author Thomas Gerlach
 */
@ServerEndpoint("/manager")
open class ManagerServer : BasicWebsocketIF {
    override val sessions = mutableSetOf<Session>()

    // https://stackoverflow.com/questions/51175990/using-cdi-injection-in-tomcat-websocket-serverendpoint
    private var parkhausServiceGlobal: ParkhausServiceGlobal =
        CDI.current().select(ParkhausServiceGlobal::class.java).get()

    @Serializable
    data class UpdatedList(val updated: List<String>)

    init {

        parkhausServiceGlobal.statisticUpdateSubj
            .buffer(1, TimeUnit.SECONDS)
            .map { it: List<List<ManagerStatistikUpdateDTO>> -> it.flatten().toSet().toList() }
            .map { it: List<ManagerStatistikUpdateDTO> -> it.map { ev -> ev.event } }
            .filter { it.isNotEmpty() }
            .subscribe { it -> val ret = UpdatedList(it); broadcastMessage(Json.encodeToString(ret)) }
    }


}