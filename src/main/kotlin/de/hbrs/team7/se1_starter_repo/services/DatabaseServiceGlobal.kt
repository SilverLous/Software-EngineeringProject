package de.hbrs.team7.se1_starter_repo.services


import de.hbrs.team7.se1_starter_repo.entities.Auto
import de.hbrs.team7.se1_starter_repo.entities.ParkhausEbene
import de.hbrs.team7.se1_starter_repo.entities.Ticket
import jakarta.annotation.PostConstruct
import jakarta.enterprise.context.ApplicationScoped
import jakarta.persistence.Persistence


/*

Functions may need to be open
open means not final
kotlin is final by default
 */

// https://gist.github.com/jahe/18a4efe614fc73cf184d8ceef8cdc996

@ApplicationScoped
//@Singleton
//@Singleton
// @Transactional
open class DatabaseServiceGlobal {

    // @Produces
    // @PersistenceContext(unitName = "hsqldb-eclipselink")
    // open var entityManager: EntityManager? = null

    // @PersistenceUnit
    // open var entityManagerFactory: EntityManagerFactory? = null

    private val emf = Persistence.createEntityManagerFactory("hsqldb-eclipselink")
    private val em = emf.createEntityManager()
    private val et = em.transaction


    open fun <T> persistEntity(e: T): T {
        et.begin()
        em.persist(e)
        et.commit()
        return e
    }

    open fun <T> mergeUpdatedEntity(e: T): T {
        et.begin()
        val newE = em.merge(e)
        et.commit()
        return newE
    }

    open fun <T> queryAllEntities(classType: Class<T>): MutableList<T>? {
        val cb = em.criteriaBuilder
        val query = cb.createQuery(classType)
        query.select(query.from(classType))
        return em.createQuery(query).resultList
    }

    open fun nativeSQLQuerySample(id: Long): MutableList<Ticket> {
        val query = em.createNativeQuery("SELECT * FROM TICKET WHERE TICKETNUMMER = ?", Ticket::class.java)
        query.setParameter(1, id.toString())
        return query.resultList as MutableList<Ticket>

    }

    open fun <T> findByID(id: Long, classType: Class<T>): T? {
        return em.find(classType, id)
    }

    open fun <T> deleteByID(id: Long, classType: Class<T>) {
        val toDelete = findByID(id, classType)
        et.begin()
        em.remove(toDelete)
        et.commit()
    }

    open fun <T> deleteByObject(toDelete: T) {
        et.begin()
        em.remove(toDelete)
        et.commit()
    }


    // this is the constructor for own functionality (single called)
    @PostConstruct
    open fun sessionInit() {
        print("Hello from Database Service")


    }

    open fun getSumAndCountOfLevel(parkhausLevelID: String): Pair<Int, Int> {
        // Sum then Count from one level of Cars that left
        // TODO specify what is SUM?

        val query = em.createNativeQuery(
            "SELECT COUNT(*) FROM TICKET" +
                    " INNER JOIN AUTO au on TICKET.AUTO_AUTONUMMER = au.AUTONUMMER" +

                    " INNER JOIN TICKET_PARKHAUSEBENE ti_pe on TICKET.TICKETNUMMER = ti_pe.TICKET_TICKETNUMMER" +

                    " INNER JOIN PARKHAUSEBENE pe on pe.ID = ti_pe.PARKHAUSEBENEN_ID" +

                    " WHERE pe.ID = ? AND au.IMPARKHAUS = FALSE"
            )
        query.setParameter(1, parkhausLevelID.toLong())

        val test = query.singleResult as Long
        return Pair(test.toInt(), test.toInt())
    }

    open fun findTicketByPlace(parkhausLevelID: String, placeNumber: Int): Ticket? {
        val query = em.createNativeQuery(
            "SELECT * FROM TICKET" +
            " INNER JOIN AUTO au on TICKET.AUTO_AUTONUMMER = au.AUTONUMMER" +

            " INNER JOIN TICKET_PARKHAUSEBENE ti_pe on TICKET.TICKETNUMMER = ti_pe.TICKET_TICKETNUMMER" +

            " INNER JOIN PARKHAUSEBENE pe on pe.ID = ti_pe.PARKHAUSEBENEN_ID" +

            " WHERE pe.ID = ? AND au.Platznummer = ? AND au.IMPARKHAUS = TRUE"
            , Ticket::class.java)
        query.setParameter(1, parkhausLevelID.toLong())
        query.setParameter(2, placeNumber)

        return query.singleResult as Ticket?

    }

    open fun getSumOfTicketPrices(parkhausEbeneID: String): Int {
        val query = em.createNativeQuery(
            "SELECT SUM(TICKET.PRICE) FROM TICKET" +

                    " INNER JOIN TICKET_PARKHAUSEBENE ti_pe on TICKET.TICKETNUMMER = ti_pe.TICKET_TICKETNUMMER" +

                    " INNER JOIN PARKHAUSEBENE pe on pe.ID = ti_pe.PARKHAUSEBENEN_ID" +

                    " WHERE pe.ID = ?"
        )
        query.setParameter(1, parkhausEbeneID.toLong())

        val result = query.singleResult as Long
        return result.toInt()
    }

    open fun getTotalUsersCount(parkhausEbeneID: String): Int {
        // returned Anzahl aller Benutzer außerhalb und innerhalb der Parkhaus Ebene

        val query = em.createNativeQuery(
            "SELECT COUNT(*) FROM TICKET" +

                    " INNER JOIN AUTO au on TICKET.AUTO_AUTONUMMER = au.AUTONUMMER" +

                    " INNER JOIN TICKET_PARKHAUSEBENE ti_pe on TICKET.TICKETNUMMER = ti_pe.TICKET_TICKETNUMMER" +

                    " INNER JOIN PARKHAUSEBENE pe on pe.ID = ti_pe.PARKHAUSEBENEN_ID" +

                    " WHERE pe.ID = ?"
        )
        query.setParameter(1, parkhausEbeneID.toLong())

        val result = query.singleResult as Long
        return result.toInt()
    }

    open fun getNotAvailableParkingSpaces(parkhausEbeneID: String): Int {
        //TODO lieber atomarisieren
        // returned Anzahl aller belegter Plätze

        // val ebene = findByID(parkhausEbeneID.toLong(),ParkhausEbene::class.java )

        val query = em.createNativeQuery(
            "SELECT Platznummer FROM AUTO" +

                    " INNER JOIN TICKET ti on ti.AUTO_AUTONUMMER = AUTO.AUTONUMMER" +

                    " INNER JOIN TICKET_PARKHAUSEBENE ti_pe on ti.TICKETNUMMER = ti_pe.TICKET_TICKETNUMMER" +

                    " INNER JOIN PARKHAUSEBENE pe on pe.ID = ti_pe.PARKHAUSEBENEN_ID" +

                    " WHERE pe.ID = ? AND AUTO.IMPARKHAUS = TRUE"
        )

        query.setParameter(1, parkhausEbeneID.toLong())

        val belegtePlaetze = query.resultList as List<Int>

        // val ebenenSet = (1 .. ebene!!.gesamtPlaetze).toSet()

        // val ebenenSet.minus(  autos.map { a -> a.Platznummer!! } )
        // autos.map { a -> a.Platznummer!! }

        return belegtePlaetze.count()
    }

}