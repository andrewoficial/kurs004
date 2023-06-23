package ru.itmo.kurs004.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import ru.itmo.kurs004.dao.AbstractDao;
import ru.itmo.kurs004.entity.Publication;
import ru.itmo.kurs004.entity.Subscription;
import ru.itmo.kurs004.specification.Specification;

import java.util.ArrayList;
import java.util.List;


public class PublicationDao extends AbstractDao<Publication, String> {

    public PublicationDao(EntityManager entityManager, Class<Publication> clazz) {
        super(entityManager, clazz);
        System.out.println("Run constructor PublicationDao " + entityManager + "" + clazz);
    }

    /*
    Оказывается все работает и без этого. Ну ничего себе.





    public Publication selectPublicationByTitle(String title){

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        // параметризуется типом, который этот запрос возвращает
        CriteriaQuery<Publication> criteriaQuery = criteriaBuilder.createQuery(Publication.class);

        // блок FROM - корневой объект, указывает, откуда будут браться данные
        Root<Publication> root = criteriaQuery.from(Publication.class);

        // блок WHERE
        Predicate condition = criteriaBuilder.equal(root.get("title"), title);

        criteriaQuery.select(root).where(condition);

        TypedQuery<Publication> query = entityManager.createQuery(criteriaQuery);
        Publication publication = query.getSingleResult();

        return publication;
    }

    public Publication selectAllAvailablePublications(){

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        // параметризуется типом, который этот запрос возвращает
        CriteriaQuery<Publication> criteriaQuery = criteriaBuilder.createQuery(Publication.class);

        // блок FROM - корневой объект, указывает, откуда будут браться данные
        Root<Publication> root = criteriaQuery.from(Publication.class);

        // блок WHERE
        Predicate condition = criteriaBuilder.equal(root.get("activeSubscription"), true);

        criteriaQuery.select(root).where(condition);

        TypedQuery<Publication> query = entityManager.createQuery(criteriaQuery);
        Publication publication = query.getSingleResult();

        return publication;
    }



     */

}