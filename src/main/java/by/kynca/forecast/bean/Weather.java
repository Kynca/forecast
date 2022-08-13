package by.kynca.forecast.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Weather {
    @Id
    @SequenceGenerator(name = "weather_sequence", sequenceName = "weather_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "weather_sequence")
    private Long id;
    private Double temperature;
    private LocalDate created_at;
    private String city;
}
