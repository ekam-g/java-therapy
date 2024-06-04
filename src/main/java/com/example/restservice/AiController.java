package com.example.restservice;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;
import java.io.OutputStream;
import org.json.JSONArray;
@RestController
public class AiController {

	@GetMapping("/bot")
	public AiReponse api(@RequestParam(value = "text", defaultValue = "none") String text) {
		if (text.isBlank()) {
			return new AiReponse("");
		}
		URL url = new URL("http://localhost:11434/api/chat");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        // Set the request method to GET
        connection.setRequestMethod("GET");

            // Convert the JSON object to a string
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("role", "user");
		jsonObject.put("role", "phi3");
		jsonObject.put("content", text);
		jsonObject.put("stream", false);
        String jsonStr = jsonObject.toString();

        // Set the request body
        connection.setDoOutput(true);
        OutputStream os = connection.getOutputStream();
        os.write(jsonStr.getBytes());
        os.close();

        // Make the request and get the response
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        StringBuilder response = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        // Parse the JSON response
        JSONObject jsonObjectos = new JSONObject(response.toString());
		JSONArray values = (JSONArray) jsonObjectos.get("reponse");
		return new AiReponse(values.toString());
	}
}