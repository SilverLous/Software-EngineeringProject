package de.hbrs.team7.se1_starter_repo.services


import de.hbrs.team7.se1_starter_repo.dto.LogEintragDTO
import de.hbrs.team7.se1_starter_repo.dto.LogKategorieDTO
import de.hbrs.team7.se1_starter_repo.dto.ManagerStatistikUpdateDTO
import de.hbrs.team7.se1_starter_repo.dto.citiesDTO
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.ReplaySubject
import jakarta.annotation.PostConstruct
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.inject.Named
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json


/*
Functions may need to be open
open means not final
kotlin is final by default
 */

// @Named
@ApplicationScoped
//@Singleton
open class LoggerServiceGlobal { // : ParkhausServiceIF {

    open val logSubject: ReplaySubject<LogEintragDTO> = ReplaySubject.create()

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
        logSubject.onNext(LogEintragDTO( kategorie = kategorie, nachricht = nachricht, zeitstempel = System.currentTimeMillis() ))

    }


}