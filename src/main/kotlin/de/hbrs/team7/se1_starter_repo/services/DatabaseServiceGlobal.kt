package de.hbrs.team7.se1_starter_repo.services


import de.hbrs.team7.se1_starter_repo.dto.oldGermanyStatisticsDTO
import de.hbrs.team7.se1_starter_repo.entities.Auto
import de.hbrs.team7.se1_starter_repo.entities.Parkhaus
import de.hbrs.team7.se1_starter_repo.entities.ParkhausEbene
import de.hbrs.team7.se1_starter_repo.entities.Ticket
import jakarta.annotation.PostConstruct
import jakarta.enterprise.context.ApplicationScoped
import jakarta.persistence.Persistence
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*


/*

Functions may need to be open
open means not final
kotlin is final by default
 */

// https://gist.github.com/jahe/18a4efe614fc73cf184d8ceef8cdc996
// https://github.com/weld/core/blob/master/tests/src/test/java/org/jboss/weld/tests/unit/resource/TestTransactionServices.java

@ApplicationScoped
//@Singleton
//@Singleton
// @Transactional
open class DatabaseServiceGlobal {

    // @Produces
    // @PersistenceContext(unitName = "hsqldb-eclipselink")
    // open var entityManager: EntityManager? = null

    // @PersistenceContext(type = EXTENDED, ) private lateinit var entityManager: EntityManager


    // @PersistenceUnit
    // open var entityManagerFactory: EntityManagerFactory? = null

    private val emf = Persistence.createEntityManagerFactory("hsqldb-eclipselink")
    private val em = emf.createEntityManager()

    // private val et = em.transaction


    open fun <T> persistEntity(e: T): T {
        val tx = em.transaction
        tx.begin()
        em.persist(e)
        tx.commit()
        return e
    }

    open fun <T> mergeUpdatedEntity(e: T): T {
        val tx = em.transaction
        tx.begin()
        val newE = em.merge(e)
        tx.commit()
        return newE
    }

    open fun <T> deleteByID(id: Long, classType: Class<T>) {
        val tx = em.transaction
        val toDelete = findByID(id, classType)
        tx.begin()
        em.remove(toDelete)
        tx.commit()
    }

    open fun <T> deleteByObject(toDelete: T) {
        val tx = em.transaction
        tx.begin()
        em.remove(toDelete)
        tx.commit()
    }

    open fun <T> queryAllEntities(classType: Class<T>): MutableList<T>? {
        val cb = em.criteriaBuilder
        val query = cb.createQuery(classType)
        query.select(query.from(classType))
        return em.createQuery(query).resultList
    }

    open fun <T> createCriteraQueryBuilder(classType: Class<T>): Pair<CriteriaBuilder, CriteriaQuery<T>> {
        val qb: CriteriaBuilder = em.criteriaBuilder
        val cq = qb.createQuery(classType)
        return Pair(qb, cq)
    }

    open fun nativeSQLQuerySample(id: Long): MutableList<Ticket> {
        val query = em.createNativeQuery("SELECT * FROM TICKET WHERE TICKETNUMMER = ?", Ticket::class.java)
        query.setParameter(1, id.toString())
        return query.resultList as MutableList<Ticket>
    }

    open fun getParkhausByCityName(name: String): Parkhaus? {
        val query = em.createNativeQuery("SELECT * FROM PARKHAUS WHERE STADTNAME = ?", Parkhaus::class.java)
        query.setParameter(1, name)
        return try {
            query.singleResult as Parkhaus
        } catch (e: jakarta.persistence.NoResultException) {
            null
        }
    }

    open fun <T> findByID(id: Long, classType: Class<T>): T? {
        return em.find(classType, id)
    }


    // this is the constructor for own functionality (single called)
    @PostConstruct
    open fun sessionInit() {
        print("Hello from Database Service")


    }

    /*
    open fun <T> joinTicketParkhausEbene(): Pair<CriteriaBuilder, CriteriaQuery<T>> {
        val (qb, cq) = createCriteraQueryBuilder(Ticket::class.java)
        val tickets: Root<Ticket> = cq.from(Ticket::class.java)
        tickets.join(Ticket.parkhausEbenen)
        return Pair(qb, cq)
    }

     */

    open fun findTicketByPlace(parkhausLevelID: Long, placeNumber: Int): Ticket? {
        val query = em.createNativeQuery(
            "SELECT * FROM TICKET" +
                    " INNER JOIN AUTO au on TICKET.AUTO_AUTONUMMER = au.AUTONUMMER" +

                    " INNER JOIN PARKHAUSEBENE_TICKET pe_ti on TICKET.TICKETNUMMER = pe_ti.TICKETS_TICKETNUMMER" +

                    " INNER JOIN PARKHAUSEBENE pe on pe.ID = pe_ti.PARKHAUSEBENEN_ID" +

                    " WHERE pe.ID = ? AND au.Platznummer = ? AND au.IMPARKHAUS = TRUE", Ticket::class.java
        )
        query.setParameter(1, parkhausLevelID)
        query.setParameter(2, placeNumber)

        val res: Ticket? = try {
            query.singleResult as Ticket?
        } catch (e: jakarta.persistence.NoResultException) {
            null
        }
        return res

    }

    open fun getSumOfTicketPrices(parkhausEbeneID: Long): Int {
        val query = em.createNativeQuery(
            "SELECT SUM(TICKET.PRICE) FROM TICKET" +

                    " INNER JOIN PARKHAUSEBENE_TICKET pe_ti on TICKET.TICKETNUMMER = pe_ti.TICKETS_TICKETNUMMER" +

                    " INNER JOIN PARKHAUSEBENE pe on pe.ID = pe_ti.PARKHAUSEBENEN_ID" +

                    " WHERE pe.ID = ?"
        )
        query.setParameter(1, parkhausEbeneID)

        val res: Long = try {
            query.singleResult as Long
        } catch (e: Exception) {
            0
        }
        return res.toInt()
    }

    open fun getTotalUsersCount(parkhausEbeneID: Long): Int {
        // returned Anzahl aller Benutzer außerhalb und innerhalb der Parkhaus Ebene

        val query = em.createNativeQuery(
            "SELECT COUNT(*) FROM TICKET" +

                    " INNER JOIN AUTO au on TICKET.AUTO_AUTONUMMER = au.AUTONUMMER" +

                    " INNER JOIN PARKHAUSEBENE_TICKET pe_ti on TICKET.TICKETNUMMER = pe_ti.TICKETS_TICKETNUMMER" +

                    " INNER JOIN PARKHAUSEBENE pe on pe.ID = pe_ti.PARKHAUSEBENEN_ID" +

                    " WHERE pe.ID = ?"
        )
        query.setParameter(1, parkhausEbeneID)

        val res: Long = try {
            query.singleResult as Long
        } catch (e: Exception) {
            0
        }
        return res.toInt()

    }

    open fun autosInParkEbene(parkhausEbeneID: Long, imParkhaus: Boolean = true): List<Auto> {
        val query = em.createNativeQuery(
            "SELECT * FROM AUTO" +

                    " INNER JOIN TICKET ti on ti.AUTO_AUTONUMMER = AUTO.AUTONUMMER" +

                    " INNER JOIN PARKHAUSEBENE_TICKET pe_ti on ti.TICKETNUMMER = pe_ti.TICKETS_TICKETNUMMER" +

                    " INNER JOIN PARKHAUSEBENE pe on pe.ID = pe_ti.PARKHAUSEBENEN_ID" +

                    " WHERE pe.ID = ? AND AUTO.IMPARKHAUS = " + imParkhaus, Auto::class.java
        )

        query.setParameter(1, parkhausEbeneID)

        val belegtePlaetze = query.resultList as List<Auto>
        return belegtePlaetze
    }

    open fun autosInParkEbeneHistoric(parkhausEbeneID: Long): List<Auto> {
        val query = em.createNativeQuery(
            "SELECT * FROM AUTO" +

                    " INNER JOIN TICKET ti on ti.AUTO_AUTONUMMER = AUTO.AUTONUMMER" +

                    " INNER JOIN PARKHAUSEBENE_TICKET pe_ti on ti.TICKETNUMMER = pe_ti.TICKETS_TICKETNUMMER" +

                    " INNER JOIN PARKHAUSEBENE pe on pe.ID = pe_ti.PARKHAUSEBENEN_ID" +

                    " WHERE pe.ID = ?", Auto::class.java
        )

        query.setParameter(1, parkhausEbeneID)

        val belegtePlaetze = query.resultList as List<Auto>
        return belegtePlaetze
    }

    open fun getOldGermanyData(): oldGermanyStatisticsDTO {

        val ddr = listOf("Saxony", "Thuringia", "Saxony-Anhalt", "Brandenburg", "Mecklenburg-Western Pomerania")
        val ddr_str = "( 'Saxony', 'Thuringia', 'Saxony-Anhalt', 'Brandenburg', 'Mecklenburg-Western Pomerania')"
        val brd = listOf(
            "Bremen",
            "Schleswig-Holstein",
            "Berlin",
            "Saarland",
            "North Rhine-Westphalia",
            "Hamburg",
            "Baden-Württemberg",
            "Lower Saxony",
            "Bavaria",
            "Hesse",
            "Rhineland-Palatinate"
        )
        val brd_str =
            "( 'Bremen', 'Schleswig-Holstein', 'Berlin', 'Saarland',  'North Rhine-Westphalia', 'Hamburg', 'Baden-Württemberg', 'Lower Saxony', 'Bavaria', 'Hesse', 'Rhineland-Palatinate')"

        val queryString = "SELECT COUNT(*), SUM(TICKET.PRICE) FROM TICKET" +

                " INNER JOIN PARKHAUSEBENE_TICKET pe_ti on TICKET.TICKETNUMMER = pe_ti.TICKETS_TICKETNUMMER" +

                " INNER JOIN PARKHAUSEBENE pe on pe.ID = pe_ti.PARKHAUSEBENEN_ID" +

                " INNER JOIN PARKHAUS pa on pa.ID = pe.PARKHAUS_ID" +

                " WHERE pa.BUNDESLAND IN "

        // val ddrQuery = em.createNativeQuery(queryString  + ddr_str, Ticket::class.java)
        val ddrQuery = em.createNativeQuery(queryString + ddr_str)

        // https://gist.github.com/arseto/f214e50917878dc31aec2ebb5af92d2e
        val ddrValuesList = ddrQuery.resultList.toList()
        val ddrResult = ddrValuesList.map { it ->
            val lst = it as Array<out Any>
            Pair(
                (lst[0] as? Long) ?: 0,
                (lst[1] as? Long) ?: 0
            )
        }.first()

        val brdQuery = em.createNativeQuery(queryString + brd_str)

        val brdValues = brdQuery.resultList // .to(Long).first.first()
        val brdResult = brdValues.map { it ->
            val lst = it as Array<out Any>
            Pair(
                (lst[0] as? Long) ?: 0,
                (lst[1] as? Long) ?: 0
            )
        }.first()

        return oldGermanyStatisticsDTO(
            if (brdResult.toList().size == 1) Pair(0, 0) else brdResult,
            if (ddrResult.toList().size == 1) Pair(0, 0) else ddrResult,
        )

    }

    open fun findeParkhausMitEbeneUeberId(id: Long): Parkhaus? {
        val query = em.createNativeQuery(
            "SELECT * FROM PARKHAUS" +

                    " INNER JOIN PARKHAUSEBENE pe on PARKHAUS.ID = pe.PARKHAUS_ID" +

                    " WHERE PARKHAUS.ID = ? ", Parkhaus::class.java
        )
        query.setParameter(1, id)

        val res: Parkhaus? = try {
            query.singleResult as Parkhaus?
        } catch (e: jakarta.persistence.NoResultException) {
            null
        }
        return res
    }

    open fun findeParkhausEbene(parkhausEbeneID: Long): ParkhausEbene? {
        val query = em.createNativeQuery(
            "SELECT * FROM PARKHAUSEBENE" +

                    " WHERE PARKHAUSEBENE.ID = ? ", ParkhausEbene::class.java
        )
        query.setParameter(1, parkhausEbeneID)

        val res: ParkhausEbene? = try {
            query.singleResult as ParkhausEbene?
        } catch (e: jakarta.persistence.NoResultException) {
            null
        }
        return res
    }

    open fun errechneTagesEinnahmen(parkhausEbeneID: Long): Int? {

        val nau = Date.from(Instant.now())
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        var timeZero = "'" + sdf.format(nau)
        timeZero += " 00:00:00.000000'"
        val query = em.createNativeQuery(
            "SELECT SUM(TICKET.PRICE) FROM TICKET" +

                    " INNER JOIN PARKHAUSEBENE_TICKET pe_ti on TICKET.TICKETNUMMER = pe_ti.TICKETS_TICKETNUMMER" +

                    " INNER JOIN PARKHAUSEBENE pe on pe.ID = pe_ti.PARKHAUSEBENEN_ID" +

                    " WHERE pe.ID = ? AND TICKET.AUSFAHRDATUM >= " + timeZero
        )
        query.setParameter(1, parkhausEbeneID)

        val res: Long = try {
            query.singleResult as Long
        } catch (e: Exception) {
            0
        }
        return res.toInt()
    }

    open fun errechneWochenEinnahmen(parkhausEbeneID: Long): Int? {

        val vorigeWoche = Date.from(Instant.now().minus(7, ChronoUnit.DAYS))
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        var timeZero = "'" + sdf.format(vorigeWoche)
        timeZero += " 00:00:00.000000'"
        val query = em.createNativeQuery(
            "SELECT SUM(TICKET.PRICE) FROM TICKET" +

                    " INNER JOIN PARKHAUSEBENE_TICKET pe_ti on TICKET.TICKETNUMMER = pe_ti.TICKETS_TICKETNUMMER" +

                    " INNER JOIN PARKHAUSEBENE pe on pe.ID = pe_ti.PARKHAUSEBENEN_ID" +

                    " WHERE pe.ID = ? AND TICKET.AUSFAHRDATUM >= " + timeZero
        )
        query.setParameter(1, parkhausEbeneID)

        val res: Long = try {
            query.singleResult as Long
        } catch (e: Exception) {
            0
        }
        return res.toInt()
    }

    open fun ticketPriceByBundesland(): Map<String, Int>? {
        val query = em.createNativeQuery(
            "" +
                    "SELECT SUM(TICKET.PRICE),BUNDESLAND FROM TICKET\n" +

                    "INNER JOIN PARKHAUSEBENE_TICKET pe_ti on TICKET.TICKETNUMMER = pe_ti.TICKETS_TICKETNUMMER\n" +

                    "INNER JOIN PARKHAUSEBENE pe on pe.ID = pe_ti.PARKHAUSEBENEN_ID\n" +

                    "INNER JOIN PARKHAUS ph on ph.ID = pe.PARKHAUS_ID\n" +

                    "GROUP BY ph.BUNDESLAND"
        )


        val ddrValuesList = query.resultList.toList()
        val resMap = HashMap<String, Int>()
        ddrValuesList.map { it ->
            val lst = it as Array<out Any>

            resMap.put(lst[1] as String , (lst[0] as Long).toInt())

        }

        return resMap
    }

    open fun bobbyTruncateTables(){

        val tx = em.transaction
        tx.begin()
        em.createNativeQuery(
            "truncate schema PUBLIC and commit" ).executeUpdate()
        tx.commit()



        //query.executeUpdate()
        //https://xkcd.com/327/
    }


}