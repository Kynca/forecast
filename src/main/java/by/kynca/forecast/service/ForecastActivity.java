package by.kynca.forecast.service;

import com.uber.cadence.activity.ActivityMethod;

public interface ForecastActivity {
    @ActivityMethod(scheduleToCloseTimeoutSeconds = 60)
    Double getAirTemperature(String name);
    @ActivityMethod(scheduleToCloseTimeoutSeconds = 60)
    void storeInfo(double temperature, String city);
}
