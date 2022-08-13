package by.kynca.forecast.service;

import by.kynca.forecast.bean.Weather;
import by.kynca.forecast.repository.WeatherRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;

@Service
public class ForecastActivityImpl implements ForecastActivity {

    @Value("${api.key}")
    private String key;

    private final WeatherRepo repository;

    @Autowired
    public ForecastActivityImpl(WeatherRepo repository) {
        this.repository = repository;
    }

    @Override
    public Double getAirTemperature(String city) {
        double[] coordinates = getCoordinates(city);

        if (coordinates == null || coordinates.length < 1) {
            return null;
        }
        Double temperature = getTemperature(coordinates);
        return temperature;
    }

    @Override
    public void storeInfo(double temperature, String city) {
        Weather weather = new Weather();
        weather.setCity(city);
        weather.setTemperature(temperature);
        weather.setCreated_at(LocalDate.now());
        repository.save(weather);
    }

    private double[] getCoordinates(String name) {
        try {
            URL url = new URL("http://api.openweathermap.org/geo/1.0/direct?q=" + name + "&limit=1&appid=" + key);
            HttpURLConnection connection = openConnection(url);
            String data = readInfo(connection);
            if (data.length() < 5) {
                return null;
            }

            double[] coordinates = parseCoordinates(data);
            return coordinates;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private double getTemperature(double[] coordinates) {
        URL url = null;
        try {
            url = new URL("https://api.openweathermap.org/data/2.5/weather?lat=" + coordinates[0] + "&lon=" + coordinates[1] + "&appid=" + key);
            HttpURLConnection connection = openConnection(url);
            String data = readInfo(connection);
            double result = parseTemperature(data);
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private HttpURLConnection openConnection(URL url) {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int responseCode = connection.getResponseCode();

            if (responseCode != 200) {
                throw new RuntimeException("HttpResponseCode" + responseCode);
            }
            return connection;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String readInfo(HttpURLConnection connection) {
        BufferedReader reader;
        StringBuilder data = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                data.append(line);
            }
            reader.close();
            return data.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private double[] parseCoordinates(String data) {
        int latIndex = data.indexOf("lat");
        data = data.substring(latIndex);
        data = data.replaceAll("[a-zA-z\":,}]", " ");
        data = data.replaceAll("\s{2,}", " ");
        if(data.charAt(0) == ' '){
            data = data.substring(1);
        }
        String[] strings = data.split(" ");
        double[] coordinates = new double[2];
        coordinates[0] = Double.parseDouble(strings[0]);
        coordinates[1] = Double.parseDouble(strings[1]);
        return coordinates;
    }

    private double parseTemperature(String data){
        int tempIndex = data.indexOf("temp");
        int fellsLikeIndex = data.indexOf("feels_like");
        data = data.substring(tempIndex, fellsLikeIndex);
        data = data.replaceAll("[a-zA-z\":,}]","");
        double result = Double.parseDouble(data);
        return result;
    }


}


