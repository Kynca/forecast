package by.kynca.forecast.controller;

import by.kynca.forecast.service.ForecastWorkflow;
import com.uber.cadence.WorkflowIdReusePolicy;
import com.uber.cadence.client.WorkflowClient;
import com.uber.cadence.client.WorkflowClientOptions;
import com.uber.cadence.client.WorkflowOptions;
import com.uber.cadence.serviceclient.ClientOptions;
import com.uber.cadence.serviceclient.WorkflowServiceTChannel;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ForecastController {

    @RequestMapping("/forecast/{city}")
    public String getWeather(@PathVariable String city){
        final WorkflowClient workflowClient =
                WorkflowClient.newInstance(
                        new WorkflowServiceTChannel(ClientOptions.defaultInstance()),
                        WorkflowClientOptions.newBuilder().setDomain("forecast").build());

        WorkflowOptions workflowOptions =
                new WorkflowOptions.Builder()
                        .setTaskList("ForecastActivities")
                        .setWorkflowIdReusePolicy(WorkflowIdReusePolicy.AllowDuplicate)
                        .build();
        ForecastWorkflow weatherForecast =
                workflowClient.newWorkflowStub(ForecastWorkflow.class, workflowOptions);
        WorkflowClient.start(weatherForecast::processActivity, city);
        return "just a random message";
    }
}
