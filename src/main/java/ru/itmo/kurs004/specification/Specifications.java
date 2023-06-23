package ru.itmo.kurs004.specification;

//import ru.itmo.kurs004.tmp.Publication;

import jakarta.persistence.criteria.JoinType;
import ru.itmo.kurs004.entity.Publication;
import ru.itmo.kurs004.entity.Recipient;
import ru.itmo.kurs004.entity.Subscription;

public class Specifications {
    private Specifications() {}

    public static class PublicationSpecifications {
        private PublicationSpecifications() {}

        public static Specification<Publication> publicationByTitle(String title) {
            return (root, query, builder) -> builder.equal(root.get("title"), title);
        }

        public static Specification<Publication> publicationByArticle(String artile) {
            return (root, query, builder) -> builder.equal(root.get("code"), artile);
        }

        public static Specification<Publication> allAvailablePublications() {
            return (root, query, builder) -> builder.equal(root.get("activeSubscription"), true);
        }

        public static Specification<Subscription> subscriptionByNumber(Recipient number) {
            //return (root, query, builder) -> builder.equal(root.get("recipient"), number); //Проверено работает

            return (root, query, builder) -> {
                root.fetch("publication", JoinType.INNER);
                query.distinct(true);
                return builder.equal(root.get("recipient"), number);
            };
        }
        /*
        public static Specification<Book> bookByCount(int numberOfBooks) {
            return (root, query, builder) -> builder.greaterThan(root.get("numberOfBooks"), numberOfBooks);
        }

        public static Specification<Book> booksWithoutIssuance(){
            return (root, query, builder) -> {
                Join<Book, BookIssuance> bookIssuanceJoin = root.join("issuances", JoinType.LEFT);
                return builder.isNull(bookIssuanceJoin.get("user"));
            };
        }

         */
        }


        /*
        public static Specification<Publication> publicationByTitle(String title) {
            return (root, query, builder) -> builder.equal(root.get("title"), title);
        }

        public static Specification<Publication> publicationById(int numberOfBooks) {
            return (root, query, builder) -> builder.greaterThan(root.get("id"), numberOfBooks);
        }

        //Для команды list
        public static Specification<Publication> publicationByAccessibility() {
            return (root, query, builder) -> builder.isTrue(root.get("activeSubscription"));
        }

        //ToDo
        /*
        /list/номер телефона - вывести названия изданий, на которые пользователь оформил подписки и сроки подписок.
            Если пользователь не оформлял подписок, вывести информацию об этом.
        /subscribe/артикул издания/срок подписки - после данной команды пользователя нужно попросить ввести номер
            телефона. Если пользователь уже оформлял подписку, вывести сумму к оплате, после согласия пользователя,
            оформить новую подписку. Если пользователь не оформлял подписок ранее, запросить у него email и ФИО,
            вывести сумму к оплате, после согласия пользователя, зарегистрировать его в системе и оформить
            подписку.
         */

}