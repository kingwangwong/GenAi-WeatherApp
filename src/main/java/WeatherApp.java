import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONObject;
import org.json.JSONArray;
import java.util.HashMap;
import java.util.Map;

public class WeatherApp {
    private static final Map<String, CacheEntry> cache = new HashMap<>();
    private static final long CACHE_DURATION_MS = 60 * 60 * 1000; // 1 hour

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter city name (or type 'exit' to quit): ");
            String city = scanner.nextLine();
            if (city.equalsIgnoreCase("exit")) {
                System.out.println("Goodbye!");
                break;
            }

            try {
                String geoApiUrl = "https://nominatim.openstreetmap.org/search?q=" + city + "&format=json&limit=1";
                String geoResponse = sendHttpRequest(geoApiUrl);
                JSONArray geoArray = new JSONArray(geoResponse);
                if (geoArray.isEmpty()) {
                    System.out.println("City not found!");
                    continue;
                }
                JSONObject location = geoArray.getJSONObject(0);
                double latitude = location.getDouble("lat");
                double longitude = location.getDouble("lon");

                String weatherApiUrl = "https://api.open-meteo.com/v1/forecast?latitude=" + latitude + "&longitude=" + longitude + "&current_weather=true";
                String weatherResponse = sendHttpRequest(weatherApiUrl);
                JSONObject weatherJson = new JSONObject(weatherResponse);
                System.out.print(weatherJson.toString(2));
                double temperature = weatherJson.getJSONObject("current_weather").getDouble("temperature");

                System.out.println("The current temperature in " + city + " is: " + temperature + "°C");
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
        }
        scanner.close();
    }

    static String sendHttpRequest(String urlString) throws Exception {
        long now = System.currentTimeMillis();
        CacheEntry entry = cache.get(urlString);
        if (entry != null && (now - entry.timestamp) < CACHE_DURATION_MS) {
            System.out.println("using cached response ");
            return entry.response;
        }
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        String responseStr = response.toString();
        cache.put(urlString, new CacheEntry(responseStr, now));
        return responseStr;
    }

    static class CacheEntry {
        String response;
        long timestamp;

        CacheEntry(String response, long timestamp) {
            this.response = response;
            this.timestamp = timestamp;
        }
    }
}