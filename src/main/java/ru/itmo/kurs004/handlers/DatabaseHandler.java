package ru.itmo.kurs004.handlers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.json.JSONArray;
import org.json.JSONObject;
import ru.itmo.kurs004.dao.PublicationDao;
import ru.itmo.kurs004.dao.SubscriptionDao;
import ru.itmo.kurs004.entity.Publication;
import ru.itmo.kurs004.entity.Recipient;
import ru.itmo.kurs004.entity.Subscription;

import java.time.LocalDate;
import java.util.*;

import static ru.itmo.kurs004.handlers.ServerHandler.sendGetRequest;
import static ru.itmo.kurs004.specification.Specifications.PublicationSpecifications.*;

public class DatabaseHandler implements Runnable{

    //Тут нужно создавать очередь на задачи, метод run и пока идет какая-то транзация в б.д не трогать её
    //Так же хорошо бы запретить создание других объектов этого класса, что бы он был синглтон
    //Почему? Я часто вижу ошибку о превышении лимитов соединения на одну учетную запись.

    private static volatile DatabaseHandler instance;
    private EntityManagerFactory emf;
    private EntityManager manager;
    private ArrayDeque <DatabaseCommand> commandBuffer = new ArrayDeque<DatabaseCommand>();

    private DatabaseHandler() {
        //throw new Exception("This class is singleton");
        try {
            this.emf = Persistence.createEntityManagerFactory("library");
            this.manager = emf.createEntityManager();
            System.out.println("Созданы объекты для соединения с б.д");
        } catch (Exception e) {
            System.out.println("Ошибка инициализации соединения с б.д" + e.getMessage());
        }
    }

    public static DatabaseHandler getInstance(){
        if(instance == null){
            synchronized (DatabaseHandler.class){
                if(instance == null){
                    instance = new DatabaseHandler();
                }
            }
        }
        return instance;
    }

    @Override
    public void run() {
        while (true) {
            DatabaseCommand command;
            boolean isInTransaction = true;
            synchronized (commandBuffer) {
                if (commandBuffer.isEmpty()) {
                    continue;
                }
                EntityTransaction transaction = manager.getTransaction();
                isInTransaction = transaction.isActive();
                command = commandBuffer.removeFirst();
            }
            switch (command.getType()) {
                case BEGIN_TRANSACTION:
                    System.out.println("Выполняю " + command.getType());
                    if(isInTransaction){
                        //есть текущая транзация continue;
                        //this.manager.isJoinedToTransaction() //Запутслся с какими версиями JPA это работает
                        System.out.println("Найдена не завершенная транзакция, ожидаю");
                        continue;
                    }
                    manager.getTransaction().begin();
                    break;
                case PERSIST_ENTITY:
                    System.out.println("Выполняю " + command.getType());
                    manager.persist(command.getArgument());
                    break;
                case COMMIT_TRANSACTION:
                    System.out.println("Выполняю " + command.getType());
                    manager.getTransaction().commit();
                    break;
                case PRINT_LINE:
                    System.out.println("Выполняю " + command.getType());
                    System.out.println(command.getArgument());
                case MERGE_ENTITY:
                    //System.out.println("Выполняю " + command.getType());
                    //manager.merge((Publication)command.getArgument());
            }

            try {
                Thread.sleep(500); //Для отладки, что бы медленнее было
            } catch (InterruptedException e) {
                // Handle interruption
            }
        }

    }

    public void merge(Publication pbl){
        manager.merge(pbl);
    }
    public void addCommandToBuffer(DatabaseCommand.CommandType type, Object argument){
        synchronized (commandBuffer) {
            commandBuffer.addLast(new DatabaseCommand(type, argument));
        }
    }





    // команда [/search] (не по заданию, просто с урока переделал)
    public void searchByName(String string){
        if(string == null){
            System.out.println("Demo mod");
            string = "Хроники хроник";
        }
        //Тут я хотел сделать все красиво, но сделано убого... Надо сделать, что бы эта команда тоже вставала
        //Очередь и выполнялась не спеша. Вместо этого добавлю просто проверку, что нет активных транзакций
        //и ожидание их завершения

        //this.addCommandToBuffer(DatabaseCommand.CommandType.BEGIN_TRANSACTION, null);
        //Если развивать эту логику, то нужно было бы перегружать конструктор DatabaseCommand
        // и создавать еще один класс для передачи параметров поиска

        if(this.manager.isJoinedToTransaction()){
            System.out.println("Найдена не завершенная транзакция, ожидаю searchByName" + string);
            while(true){
                if(! this.manager.isJoinedToTransaction()){
                    System.out.println("Прекращаю ожидание searchByName");
                    break;
                }
            }
        }
        manager.getTransaction().begin();
        PublicationDao publDao = new PublicationDao(manager, Publication.class);
        List<Publication> resultList = publDao.selectAllByCondition(publicationByTitle(string));
        System.out.println("Издания с указанным названием:");
        for (Object result : resultList) {
            if (result instanceof Publication) {
                Publication publication = (Publication) result;
                System.out.print("Издание '"+publication.getTitle());
                System.out.println("' стоит "+publication.getSubscriptionCost());
            }
        }
    }

    public Publication searchByNumber(String string){
        PublicationDao publDao = new PublicationDao(manager, Publication.class);
        while(true){
            if(! this.manager.isJoinedToTransaction()){
                System.out.println("Прекращаю ожидание searchByNumber");
                break;
            }
        }
        manager.getTransaction().begin();
        List<Publication> resultList = publDao.selectAllByCondition(publicationByArticle(string));
        manager.getTransaction().commit();

        for (Object result : resultList) {
            if (result instanceof Publication) {
                Publication forReturn = (Publication) result;
                System.out.println("Издания с указанным номером найдены");
                return forReturn;
            }
        }
        return null;
    }

    public void printList(){
        // команда [/list]
        PublicationDao publDao2 = new PublicationDao(manager, Publication.class);
        while(true){
            if(! this.manager.isJoinedToTransaction()){
                System.out.println("Прекращаю ожидание printList");
                break;
            }
        }
        manager.getTransaction().begin();
        List<Publication> resultList2 = publDao2.selectAllByCondition(allAvailablePublications());
        manager.getTransaction().commit();
        System.out.println("Доступные для подписки издания:");
        for (Object result : resultList2) {
            if (result instanceof Publication) {
                Publication publication = (Publication) result;
                System.out.println(publication.getInfo());
            }
        }
    }

    public void searchUsersSubscribeByNumber(String number){
        // команда [/list/номер телефона]
        SubscriptionDao subDao1 = new SubscriptionDao(manager, Subscription.class);
        Recipient forSearch = new Recipient(number);
        while(true){
            if(! this.manager.isJoinedToTransaction()){
                System.out.println("Прекращаю ожидание printList");
                break;
            }
        }
        manager.getTransaction().begin();
        List<Subscription> resultList3 = subDao1.selectAllByCondition(subscriptionByNumber(forSearch));
        manager.getTransaction().commit();
        System.out.println("Подписки у пользователя с указанным номером телефона:");
        for (Object result : resultList3) {
            Subscription subscription = (Subscription) result;
            System.out.print("Подписка: " + subscription.getPublication().getTitle());
            System.out.println(" начало подписки: " + subscription.getDeliveryStart());
        }
    }

    public void halt(){
        commandBuffer.clear();
        System.out.println(this.getClass() + " очистил очередь команд");
        manager.close();
        System.out.println(this.getClass() + " закрыл manager");
        emf.close();
        System.out.println(this.getClass() + " закрыл emf");
    }
}
