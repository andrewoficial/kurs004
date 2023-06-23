package ru.itmo.kurs004.handlers;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONArray;
import org.json.JSONObject;

public class ServerHandler {

    public static void sendPostRequest(String POST_URL, String json) {
        try {
            URL url = new URL(POST_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Установка метода запроса и свойств
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Создание JSON-строки для отправки
            String requestBody = "{\"key\": \"value\"}"; // Замените на ваш JSON-контент

            // Отправка данных
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(requestBody.getBytes());
            outputStream.flush();

            // Получение ответа
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                System.out.println("Ответ: " + response.toString());
            } else {
                System.out.println("Ошибка при отправке запроса. Код ошибки: " + responseCode);
            }
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String sendGetRequest(String url) throws Exception {
        URL requestUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();

        // Установка метода запроса и свойств
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");

        // Получение ответа
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            connection.disconnect();
            return response.toString();
        } else {
            connection.disconnect();
            throw new Exception("Ошибка при отправке GET-запроса. Код ошибки: " + responseCode);
        }
    }
}
