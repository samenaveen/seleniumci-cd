package util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Utility class to fetch random jokes from JokeAPI
 * API: https://jokeapi.dev/
 */
public class JokeGenerator {

    private static final Logger logger = LoggerFactory.getLogger(JokeGenerator.class);
    private static final String JOKE_API_URL = "https://v2.jokeapi.dev/joke/Any";
    private static final int TIMEOUT_SECONDS = 5;

    /**
     * Fetches a random joke from the external JokeAPI
     * 
     * @return A string containing the random joke
     * @throws Exception if the API call fails or parsing error occurs
     */
    public static String getRandomJoke() throws Exception {
        logger.info("Fetching random joke from JokeAPI...");
        
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet getRequest = new HttpGet(JOKE_API_URL);
            getRequest.setHeader("Accept", "application/json");

            return httpClient.execute(getRequest, response -> {
                if (response.getCode() != 200) {
                    throw new RuntimeException("Failed to fetch joke. Status: " + response.getCode());
                }

                HttpEntity entity = response.getEntity();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()))) {
                    StringBuilder responseBody = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        responseBody.append(line);
                    }

                    String jsonResponse = responseBody.toString();
                    logger.debug("API Response: {}", jsonResponse);

                    JsonObject jokeObject = JsonParser.parseString(jsonResponse).getAsJsonObject();
                    
                    // Check if the response contains an error
                    if (jokeObject.has("error") && jokeObject.get("error").getAsBoolean()) {
                        throw new RuntimeException("API returned an error: " + jokeObject.get("message").getAsString());
                    }

                    String type = jokeObject.get("type").getAsString();
                    String joke;

                    if ("single".equals(type)) {
                        joke = jokeObject.get("joke").getAsString();
                    } else if ("twopart".equals(type)) {
                        String setup = jokeObject.get("setup").getAsString();
                        String delivery = jokeObject.get("delivery").getAsString();
                        joke = setup + " - " + delivery;
                    } else {
                        throw new RuntimeException("Unknown joke type: " + type);
                    }

                    logger.info("Successfully fetched joke: {}", joke);
                    return joke;
                }
            });
        } catch (Exception e) {
            logger.error("Error fetching joke from API: {}", e.getMessage(), e);
            throw new Exception("Failed to fetch random joke: " + e.getMessage(), e);
        }
    }

    /**
     * Fetches a random joke and prints it to console
     * 
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        try {
            String joke = getRandomJoke();
            System.out.println("\n=== Random Joke ===");
            System.out.println(joke);
            System.out.println("===================\n");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
