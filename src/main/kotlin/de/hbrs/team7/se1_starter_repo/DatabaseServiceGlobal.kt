package de.hbrs.team7.se1_starter_repo


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



    // this is the constructor for own functionality (single called)
    @PostConstruct
    open fun sessionInit() {
        print("Hello from Database Service")


    }


}