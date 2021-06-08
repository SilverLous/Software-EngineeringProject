package de.hbrs.team7.se1_starter_repo.services


import de.hbrs.team7.se1_starter_repo.entities.Auto
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
// https://github.com/weld/core/blob/master/tests/src/test/java/org/jboss/weld/tests/unit/resource/TestTransactionServices.java

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

    open fun findTicketByPlace(parkhausLevelID: Long, placeNumber: Int): Ticket? {
        val query = em.createNativeQuery(
            "SELECT * FROM TICKET" +
                    " INNER JOIN AUTO au on TICKET.AUTO_AUTONUMMER = au.AUTONUMMER" +

                    " INNER JOIN PARKHAUSEBENE_TICKET pe_ti on TICKET.TICKETNUMMER = pe_ti.TICKETS_TICKETNUMMER" +

                    " INNER JOIN PARKHAUSEBENE pe on pe.ID = pe_ti.PARKHAUSEBENEN_ID" +

                    " WHERE pe.ID = ? AND au.Platznummer = ? AND au.IMPARKHAUS = TRUE"
            , Ticket::class.java)
        query.setParameter(1, parkhausLevelID)
        query.setParameter(2, placeNumber)

        val res: Ticket? = try { query.singleResult as Ticket? } catch (e: jakarta.persistence.NoResultException ) { null }
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

        val res: Long = try { query.singleResult as Long } catch (e: jakarta.persistence.NoResultException ) { 0 }
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

        val res: Long = try { query.singleResult as Long } catch (e: jakarta.persistence.NoResultException ) { 0 }
        return res.toInt()

    }

    open fun autosInParkEbene(parkhausEbeneID: Long, imParkhaus: Boolean = true): List<Auto> {
        val query = em.createNativeQuery(
            "SELECT * FROM AUTO" +

                    " INNER JOIN TICKET ti on ti.AUTO_AUTONUMMER = AUTO.AUTONUMMER" +

                    " INNER JOIN PARKHAUSEBENE_TICKET pe_ti on ti.TICKETNUMMER = pe_ti.TICKETS_TICKETNUMMER" +

                    " INNER JOIN PARKHAUSEBENE pe on pe.ID = pe_ti.PARKHAUSEBENEN_ID" +

                    " WHERE pe.ID = ? AND AUTO.IMPARKHAUS = "+imParkhaus
            , Auto::class.java)

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

                    " WHERE pe.ID = ?"
            , Auto::class.java)

        query.setParameter(1, parkhausEbeneID)

        val belegtePlaetze = query.resultList as List<Auto>
        return belegtePlaetze
    }

}