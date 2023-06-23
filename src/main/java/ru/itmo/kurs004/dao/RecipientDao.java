package ru.itmo.kurs004.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import ru.itmo.kurs004.entity.Recipient;

import java.util.List;

public class RecipientDao extends AbstractDao<Recipient, String> {

    public RecipientDao(EntityManager entityManager, Class<Recipient> clazz) {
        super(entityManager, clazz);
    }

    @Override
    public List<Recipient> selectAll() {
        List<Recipient> libraryUsers = null;
        /* 1. NAMED QUERY */
        TypedQuery<Recipient> namedQuery = entityManager.createNamedQuery("all", Recipient.class);
        libraryUsers = namedQuery.getResultList();

        /* 2. JPQL QUERY */
        TypedQuery<Recipient> jpqlQuery = entityManager.createQuery("SELECT lu FROM LibraryUser lu", Recipient.class);
        libraryUsers = jpqlQuery.getResultList();

        /* 3. NATIVE QUERY */
        Query nativeQuery = entityManager.createNativeQuery("SELECT * FROM tb_users", Recipient.class);
        libraryUsers = (List<Recipient>) nativeQuery.getResultList();

        /* 4. Criteria API */
        // criteriaBuilder - формирует SQL запросы / criteriaQuery - SQL запрос (аналог SQL строки)

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        // тип данных результата
        CriteriaQuery<Recipient> criteriaQuery = criteriaBuilder.createQuery(Recipient.class);

        // откуда извлекаются данные
        Root<Recipient> root = criteriaQuery.from(Recipient.class); // FROM таблица

        criteriaQuery.select(root); // SELECT

        TypedQuery<Recipient> query = entityManager.createQuery(criteriaQuery);
        libraryUsers = query.getResultList();

        return libraryUsers;
    }
}