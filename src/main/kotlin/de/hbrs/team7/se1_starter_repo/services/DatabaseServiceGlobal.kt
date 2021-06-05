package de.hbrs.team7.se1_starter_repo.services


import de.hbrs.team7.se1_starter_repo.entities.Ticket
import jakarta.annotation.PostConstruct
import jakarta.enterprise.context.ApplicationScoped
import jakarta.persistence.Persistence



/*

Functions may need to be open
open means not final
kotlin is final by default
 */

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

    open fun getSumAndCount(parkhausId: String): List<Int> {
        // Sum then Count from one level of Cars that left
        throw NotImplementedError()
    }

    open fun findTicketByPlace(parkhausEbeneID: String, placeNumber: Int): Ticket {
        throw NotImplementedError()
    }

}