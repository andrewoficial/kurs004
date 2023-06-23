package ru.itmo.kurs004;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.json.JSONArray;
import org.json.JSONObject;
import ru.itmo.kurs004.dao.PublicationDao;
import ru.itmo.kurs004.dao.SubscriptionDao;
import ru.itmo.kurs004.entity.*;
import ru.itmo.kurs004.handlers.DatabaseCommand;
import ru.itmo.kurs004.handlers.DatabaseHandler;
import ru.itmo.kurs004.services.PublisherUpdater;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static ru.itmo.kurs004.handlers.ServerHandler.sendGetRequest;
import static ru.itmo.kurs004.handlers.ServerHandler.sendPostRequest;
import static ru.itmo.kurs004.specification.Specifications.PublicationSpecifications.*;

public class Main {
    public static Set <Publication> publicationPool = new HashSet<>(); //Не используется, но не уверен что и не будет
    public static void main(String[] args) throws InterruptedException {
        LocalDate date = LocalDate.now();
        Recipient recipient01 = new Recipient("Карпатов А. Ю.", "+79991112233", "email@mail.mail");
        Recipient recipient02 = new Recipient("Карпатов А. Ю.", "+79991112244", "email2@mail.mail");
        Recipient recipient03 = new Recipient("Unsubs Dude", "+79992175007", "email3@mail.mail");
        Publication publusher01 = new Publication("AB001", "NEWSPAPER", "Хроники хроник", 9.99, true);
        Publication publusher02 = new Publication("AB002", "NEWSPAPER", "новости", 9.99, true);
        Publication publusher03 = new Publication("AB003", "NEWSPAPER", "Хроники хроник (заметки редакторов)", 9.99, true);
        Publication publusher04 = new Publication("AB004", "NEWSPAPER", "Рыжий Ап", 9.99, false);
        Publication publusher05 = new Publication("34776", "MAGAZINE", "Забытый журнал", 5, true);
        //"number":"34776","title":"Забытый журнал","type":"MAGAZINE","price":420
        Subscription sub01 = new Subscription(3, date, recipient01, publusher01);
        Subscription sub02 = new Subscription(3, date, recipient01, publusher02);


        System.out.println("Завершено создание данных для тестирования");

        DatabaseHandler dbh = DatabaseHandler.getInstance();
        Thread thread1 = new Thread(dbh);
        //Что бы не возникло проблем при первичной инициализации
        //thread.setDaemon(true); //Что бы когда пользователь введет /exit сразу выйти. По хорошему надо давать время на
        //завершение транзакции и уточнять прервать сейчас или нет

        dbh.addCommandToBuffer(DatabaseCommand.CommandType.PRINT_LINE, "First part");
        dbh.addCommandToBuffer(DatabaseCommand.CommandType.BEGIN_TRANSACTION, null);
        dbh.addCommandToBuffer(DatabaseCommand.CommandType.PERSIST_ENTITY, recipient01);
        dbh.addCommandToBuffer(DatabaseCommand.CommandType.PERSIST_ENTITY, recipient02);
        dbh.addCommandToBuffer(DatabaseCommand.CommandType.PERSIST_ENTITY, recipient03);
        dbh.addCommandToBuffer(DatabaseCommand.CommandType.PERSIST_ENTITY, publusher05);//Проверить будет ли обновлен
        dbh.addCommandToBuffer(DatabaseCommand.CommandType.PERSIST_ENTITY, publusher02);
        dbh.addCommandToBuffer(DatabaseCommand.CommandType.PERSIST_ENTITY, publusher03);
        dbh.addCommandToBuffer(DatabaseCommand.CommandType.PERSIST_ENTITY, publusher04);
        dbh.addCommandToBuffer(DatabaseCommand.CommandType.PERSIST_ENTITY, publusher01);
        dbh.addCommandToBuffer(DatabaseCommand.CommandType.COMMIT_TRANSACTION, null);

        dbh.addCommandToBuffer(DatabaseCommand.CommandType.PRINT_LINE, "Second part");
        dbh.addCommandToBuffer(DatabaseCommand.CommandType.BEGIN_TRANSACTION, null);
        dbh.addCommandToBuffer(DatabaseCommand.CommandType.PERSIST_ENTITY, sub01);
        dbh.addCommandToBuffer(DatabaseCommand.CommandType.PERSIST_ENTITY, sub02);
        dbh.addCommandToBuffer(DatabaseCommand.CommandType.COMMIT_TRANSACTION, null);
        thread1.start();
        thread1.join();


        PublisherUpdater pbupdt = new PublisherUpdater(dbh);
        Thread thread2 = new Thread(pbupdt);
        Thread thread3 = new Thread(dbh);


        thread2.start();
        thread3.start();

        Scanner sc = new Scanner(System.in);
        String input = "";
        while(true){
            System.out.println("Введите команду:");
            input = sc.nextLine();
            if(input == null || input.length() < 2){
                System.out.println("Неверный ввод");
            }else if("/exit".equalsIgnoreCase(input)){
                System.out.println("Завершение приложения");
                //timer.cancel();
                sc.close();
                dbh.halt(); //Минуя очередь
                System.exit(0);
            }else if("/search".equalsIgnoreCase(input)){
                System.out.println("Введите номер для поиска:");
                input = sc.nextLine();
                System.out.println(dbh.searchByNumber(input).getInfo());
            }else if("/list".equalsIgnoreCase(input)){
                dbh.printList();
            }else if(input.contains("/list/")){
                input = input.replace("/list/", "");
                System.out.println("Вывод подписок для пользователя с номером телефона " + input + " : ");
                dbh.searchUsersSubscribeByNumber(input);
            }else if("/help".equalsIgnoreCase(input)){
                System.out.println("/exit - выход из приложения \n" +
                        "/search - поиск по номеру \n/list - вывод доступных по подписке \n" +
                        "/list/+7xxxXXXxxXX - поиск подписок по номеру телефона");
            }else if("/runTH2".equalsIgnoreCase(input)){
                System.out.println("Нужно дождаться активного завершения первичной настройки и запустить поток");

            }
        }
    }
}
