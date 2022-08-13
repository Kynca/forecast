package by.kynca.forecast.repository;

import by.kynca.forecast.bean.Weather;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeatherRepo extends JpaRepository<Weather, Long> {
}
