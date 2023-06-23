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

import java.util.List;


public class SubscriptionDao extends AbstractDao<Subscription, String> {
    public SubscriptionDao(EntityManager entityManager, Class<Subscription> clazz) {
        super(entityManager, clazz);
        System.out.println("Run constructor SubscriptionDao " + entityManager + "" + clazz);
    }

    /*

        public Subscription selectsSubscriptionByNumber(String number){

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        // параметризуется типом, который этот запрос возвращает
        CriteriaQuery<Subscription> criteriaQuery = criteriaBuilder.createQuery(Subscription.class);

        // блок FROM - корневой объект, указывает, откуда будут браться данные
        Root<Subscription> root = criteriaQuery.from(Subscription.class);

            // блок WHERE
            Predicate condition = criteriaBuilder.equal(root.get("activeSubscription"), true);

            criteriaQuery.select(root).where(condition);

            TypedQuery<Subscription> query = entityManager.createQuery(criteriaQuery);
            Subscription subscription = query.getSingleResult();

            return subscription;

            /*

        criteriaQuery.multiselect(root.get("publication").get("title"), root.get("subscriptionTerm"));
        criteriaQuery.where(criteriaBuilder.equal(root.get("recipient").get("phoneNumber"), number));

        TypedQuery<Subscription> query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();

             */


   // }
}
