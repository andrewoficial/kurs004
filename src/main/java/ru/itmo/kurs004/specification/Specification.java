package ru.itmo.kurs004.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public interface Specification<T> {
    Predicate makeCondition(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder);

    default Specification<T> and(Specification<T> other) {
        return ((root, query, builder) ->
                builder.and(makeCondition(root, query, builder), other.makeCondition(root, query, builder)));
    }

    default Specification<T> or(Specification<T> other) {
        return ((root, query, builder) ->
                builder.or(makeCondition(root, query, builder), other.makeCondition(root, query, builder)));
    }
}