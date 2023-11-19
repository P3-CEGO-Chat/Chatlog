package com.cego.chatlog.util;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

class HttpClient {
    private String endpointURL;

    public HttpClient(String endpointURL) {
        this.endpointURL = endpointURL;
    }

    public void sendPostRequest(String jsonData) {
        try {
            URL url = new URL(endpointURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set the HTTP request method to POST
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Write the JSON data to the request body
            OutputStream os = connection.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
            osw.write(jsonData);
            osw.flush();
            osw.close();
            os.close();

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("Request successful for data: " + jsonData);
            } else {
                System.out.println("Request failed for data: " + jsonData);
                System.out.println("Response code: " + responseCode);
            }

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMultipleRequests(List<String> jsonDataList) {
        for (String jsonData : jsonDataList) {
            sendPostRequest(jsonData);
        }
    }
}

class Message {
    private String customerId;
    private String username;
    private String message;
    private String dateTime;

    public Message(String customerId, String username, String message, String dateTime) {
        this.customerId = customerId;
        this.username = username;
        this.message = message;
        this.dateTime = dateTime;
    }

    // Getter and setter methods for the Message class (if needed)

    @Override
    public String toString() {
        String danishUsername = getDanishUsername(username);
        String danishMessage = getDanishMessage(message);
        return "{" +
                "\"customerId\": \"" + customerId + "\"," +
                "\"username\": \"" + danishUsername + "\"," +
                "\"message\": \"" + danishMessage + "\"," +
                "\"dateTime\": \"" + dateTime + "\"" +
                "}";
    }

    private String getDanishUsername(String username) {
        String[] usernames = MessageGenerator.getUsernames();
        int index = Arrays.asList(usernames).indexOf(username);
        if (index != -1) {
            return usernames[index];
        } else {
            return "UnknownUsername";
        }
    }

    private String getDanishMessage(String message) {
        String[] messages = MessageGenerator.getMessages();
        int index = Arrays.asList(messages).indexOf(message);
        if (index != -1) {
            return messages[index];
        } else {
            return "UnknownMessage";
        }
    }
}

class MessageGenerator {
    public static final String[] usernames = {"Anders123", "Mette456", "Lars789", "Sofie234", "Emil567", "Ida890", "Frederik123", "Maria456", "Nikolaj789", "Julie234"};
    public static final String[] messages = {"Hey drenge, jeg har lige scoret kassen på spillemaskinerne!", "Er der nogen, der spiller blackjack lige nu?", "Jeg kan ikke tro, at jeg har tabt alle mine chips på roulette", "Jeg har lige ramt jackpotten på de progressive spillemaskiner!", "Hvad er dit yndlingsspil på dette site?", "Jeg er på en vindende stribe i aften!", "Jeg har brug for nogle tips til at spille craps, nogen?", "Jeg prøver det nye spil, kryds fingre for mig!", "Jeg har tabt et par hænder i poker, men jeg giver ikke op endnu!", "Lad os mødes ved high roller-bordene!", "Har du set den nye spillemaskine?", "Jeg har lige fået en bonus på min konto!", "Jeg har brug for en pause fra spillet, hvad med dig?", "Jeg har lige vundet en stor gevinst på roulette!", "Hvad er din strategi for at vinde på spillemaskinerne?", "Jeg har lige tabt på blackjack, men jeg giver ikke op endnu!", "Jeg har brug for nogle tips til at spille baccarat, nogen?", "Jeg prøver at slå min personlige rekord i gevinster i aften!", "Jeg har lige tabt på spillemaskinerne, men jeg giver ikke op endnu!", "Lad os spille sammen i aften!", "Jeg har lige vundet en stor jackpot på spillemaskinerne!", "Hvad er din yndlings jackpot?", "Jeg har brug for nogle tips til at spille poker, nogen?", "Jeg prøver at vinde tilbage mine tab i aften!", "Lad os spille noget craps sammen!"};
    public static String[] getUsernames() {
        return usernames;
    }

    public static String[] getMessages() {
        return messages;
    }
}

public class MultiPostMessage {
    public static void main(String[] args) {
        String endpointURL = "http://localhost:8080/receiveDataJSON";

        // Create an instance of the HttpClient
        HttpClient httpClient = new HttpClient(endpointURL);

        // Generate a list of Message objects with Danish text and usernames
        List<Message> messages = generateMessages();

        // Send multiple requests using the HttpClient
        for (Message message : messages) {
            httpClient.sendPostRequest(message.toString());
        }
    }

    private static List<Message> generateMessages() {
        List<Message> messages = new ArrayList<>();
        Random random = new Random();
        String[] usernames = MessageGenerator.getUsernames();
        String[] messageArray = MessageGenerator.getMessages();
        for (int i = 0; i < 25; i++) {
            String username = usernames[i % usernames.length];
            String message = messageArray[i % messageArray.length];
            int customerId = i;
            String base5CustomerId = "";
            while (customerId > 0) {
                int remainder = customerId % 5;
                base5CustomerId = remainder + base5CustomerId;
                customerId = customerId / 5;
            }
            if (base5CustomerId.isEmpty()) {
                base5CustomerId = "0";
            }
            String timestamp = "2023-11-13T" + random.nextInt(24) + ":" + random.nextInt(60) + ":" + random.nextInt(60) + "." + random.nextInt(1000) + "Z";
            Message newMessage = new Message("SN" + base5CustomerId, username, message, timestamp);
            messages.add(newMessage);
        }
        return messages;
    }
}