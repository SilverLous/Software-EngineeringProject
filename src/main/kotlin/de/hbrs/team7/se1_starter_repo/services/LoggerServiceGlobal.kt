package de.hbrs.team7.se1_starter_repo.services


import de.hbrs.team7.se1_starter_repo.dto.LogEintragDTO
import de.hbrs.team7.se1_starter_repo.dto.LogKategorieDTO
import io.reactivex.rxjava3.subjects.ReplaySubject
import java.util.logging.*
import jakarta.annotation.PostConstruct
import jakarta.enterprise.context.ApplicationScoped


/*
Functions may need to be open
open means not final
kotlin is final by default
 */

/**
 *
 * Ein Service für das zentrale loggen von Informationen.
 * Diese werden einmal als System log ausgegeben und einmal an die Website als Websocket
 *
 * @property logSubject ein ReplaySubject um zu speichern welche Log Einträge eingekommen sind
 *
 * @author Thomas Gerlach
 */
@ApplicationScoped
open class LoggerServiceGlobal { // : ParkhausServiceIF {

    open val logSubject: ReplaySubject<LogEintragDTO> = ReplaySubject.create()

    private val logger: Logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME)

    private val logMapper = hashMapOf(
        LogKategorieDTO.INFO to Level.INFO,
        LogKategorieDTO.WARNING to Level.WARNING,
        LogKategorieDTO.DEBUG to Level.FINE,
    )

    // this is the constructor for own functionality (single called)
    @PostConstruct
    open fun sessionInit() {
        val nachricht = "Logger wurde initialisiert"
        print(nachricht)
        schreibeInfo(nachricht)

    }

    open fun schreibeInfo(message: String) {
        schreibeNachricht(kategorie = LogKategorieDTO.INFO, message)
    }

    open fun schreibeWarning(message: String) {
        schreibeNachricht(kategorie = LogKategorieDTO.WARNING, message)
    }

    open fun schreibeDebug(message: String) {
        schreibeNachricht(kategorie = LogKategorieDTO.DEBUG, message)
    }

    open fun schreibeNachricht(kategorie: LogKategorieDTO, nachricht: String) {
        logger.log(logMapper[kategorie],nachricht)
        logSubject.onNext(LogEintragDTO( kategorie = kategorie, nachricht = nachricht, zeitstempel = System.currentTimeMillis() ))

    }


}