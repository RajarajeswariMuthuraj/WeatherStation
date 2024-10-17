package city.WeatherStation.Model;


import java.io.*;
import java.net.*;
import java.util.*;
import org.json.*;

class Result {

    public static List<String> weatherStation(String keyword) {
        List<String> result = new ArrayList<>();
        int page = 1;
        int totalPages = 1;

        try {
            // Iterate through all pages
            while (page <= totalPages) {
                // Prepare the URL with the keyword and page number
                String apiUrl = "https://jsonmock.hackerrank.com/api/weather/search?name=" + URLEncoder.encode(keyword, "UTF-8") + "&page=" + page;
                URL url = new URL(apiUrl);
                
                // Open connection and get the input stream
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }

                // Close the connections
                in.close();
                conn.disconnect();

                // Parse the JSON response
                JSONObject response = new JSONObject(content.toString());
                JSONArray data = response.getJSONArray("data");
                totalPages = response.getInt("total_pages");

                // Process each city's weather data
                for (int i = 0; i < data.length(); i++) {
                    JSONObject cityWeather = data.getJSONObject(i);
                    String cityName = cityWeather.getString("name");
                    String weather = cityWeather.getString("weather");
                    
                    // Extract wind and humidity from status array
                    JSONArray status = cityWeather.getJSONArray("status");
                    String wind = status.getString(0).replace("Wind: ", "").replace("Kmph", "").trim();
                    String humidity = status.getString(1).replace("Humidity: ", "").replace("%", "").trim();

                    // Create the result string
                    result.add(cityName + ", " + weather.replace(" degree", "").trim() + ", " + wind + ", " + humidity);
                }
                page++; // Move to the next page
            }

        } catch (Exception e) {
            e.printStackTrace();
        } 

        // Sort the result alphabetically by city name
        Collections.sort(result);
        return result;
    }
}

public class WeatherStation {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String keyword = bufferedReader.readLine();
        List<String> result = Result.weatherStation(keyword);
        for (String cityWeather : result) {
            System.out.println(cityWeather);
        }
        bufferedReader.close();
    }
}
