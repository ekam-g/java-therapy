package com.example.restservice;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONObject;
import java.io.OutputStream;

import org.apache.logging.log4j.message.Message;
import org.json.JSONArray;

@RestController
public class AiController {
    private static HashMap<String, JSONObject> jsonRequester = new HashMap<>();

    @GetMapping("/bot")
    public AiReponse api(@RequestParam(value = "text", defaultValue = "none") String text,
            @RequestParam(value = "id", defaultValue = "none") String id) {
        if (text.isBlank()) {
            return new AiReponse("");
        }
        try {
            try {
                JSONObject newMessage = new JSONObject();
                newMessage.put("role", "user");
                newMessage.put("content", text);
                jsonRequester.get(id).getJSONArray("messages").put(newMessage);
            } catch (Exception e) {
                JSONObject root = new JSONObject();
                root.put("model", "llama3-instruct");
                root.put("stream", false);
                JSONObject newMessage = new JSONObject();
                newMessage.put("role", "user");
                newMessage.put("content", text);
                root.put("messages", newMessage);
                jsonRequester.put(id, root);
            }
            // Create a URI object
            URI uri = URI.create("http://localhost:11434/api/generate");

            // Create an HttpRequest object
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers
                            .ofString(jsonRequester.get(id).toString()))
                    .build();

            // Send the request and get the response
            HttpResponse<String> response = HttpClient.newHttpClient().send(request,
                    HttpResponse.BodyHandlers.ofString());

            // Print the response code and content
            System.out.println("Response Code: " + response.statusCode());
            System.out.println("Response: " + response.body());
            // Parse the JSON response
            JSONObject jsonObjectos = new JSONObject(response.body());
            String values = jsonObjectos.getString("response");
            return new AiReponse(values.toString());
        } catch (Exception e) {
            return new AiReponse(e.toString());
        }
    }
}