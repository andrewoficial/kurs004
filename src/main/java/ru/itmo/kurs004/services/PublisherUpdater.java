package ru.itmo.kurs004.services;


import org.json.JSONArray;
import org.json.JSONObject;
import ru.itmo.kurs004.entity.Publication;
import ru.itmo.kurs004.handlers.DatabaseCommand;
import ru.itmo.kurs004.handlers.DatabaseHandler;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static ru.itmo.kurs004.handlers.ServerHandler.sendGetRequest;

public class PublisherUpdater implements Runnable {
    Timer timer = new Timer();
    DatabaseHandler dbh;

    public PublisherUpdater(DatabaseHandler dbh){
        this.dbh = dbh;
    }
    @Override
    public void run() {
        timer.schedule(serverPool(), 0, 150000);
        //3600000 Отправлять запрос каждый час (3600000 миллисекунд)
    }

    public TimerTask serverPool() {
        return new TimerTask() {
            @Override
            public void run() {
                System.out.println("Начата задача по работе с сервером (sendGetRequest)");
                ArrayList<Publication> publishers = new ArrayList();
                try {
                    String response = sendGetRequest("http://localhost:8080/editions");
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        // Создание массива объектов JSON
                        JSONObject[] objects = new JSONObject[jsonArray.length()];
                        for (int i = 0; i < jsonArray.length(); i++) {
                            objects[i] = jsonArray.getJSONObject(i);
                        }

                        for (JSONObject object : objects) {
                            String number = object.getString("number");
                            String title = object.getString("title");
                            String type = object.getString("type");
                            int price = object.getInt("price");
                            Publication tmp = new Publication(number, type, title, price, true);
                            publishers.add(tmp);
                        }
                        System.out.println("Получено от сервера " + response);

                    } catch (Exception e) {
                        System.out.println("Ошибка работы с данными от сервера (JSON - broken?) " + e.getMessage());
                    }
                } catch (Exception e) {
                    System.out.println("Ошибка при работе с сервером" + e.getMessage());
                    System.out.println();
                } finally {
                    System.out.println("Завершена задача по работе с сервером (GetRequest)");
                }

                for (Publication publisher : publishers) {

                    if (dbh.searchByNumber(publisher.getCode()) != null) {
                        System.out.println(publisher.getCode() + " найден и будет обновлен");
                        //dbh.addCommandToBuffer(DatabaseCommand.CommandType.BEGIN_TRANSACTION, null);
                        dbh.merge(publisher);
                        //dbh.addCommandToBuffer(DatabaseCommand.CommandType.COMMIT_TRANSACTION, null);
                        //manager.find(Publication.class, publisher.getCode()).setSubscriptionCost(publisher.getSubscriptionCost());
                    } else {
                        System.out.println(publisher.getCode() + " не найден и будет создан");
                        dbh.addCommandToBuffer(DatabaseCommand.CommandType.PRINT_LINE, "Выполняю добавление [" + publisher.getTitle() + "]");
                        dbh.addCommandToBuffer(DatabaseCommand.CommandType.BEGIN_TRANSACTION, null);
                        dbh.addCommandToBuffer(DatabaseCommand.CommandType.PERSIST_ENTITY, publisher);
                        dbh.addCommandToBuffer(DatabaseCommand.CommandType.COMMIT_TRANSACTION, null);
                        dbh.run();

                    }
                }
            }
        };
    }
}
