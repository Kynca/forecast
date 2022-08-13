package by.kynca.forecast;

import by.kynca.forecast.service.ForecastActivity;
import by.kynca.forecast.service.ForecastActivityImpl;
import by.kynca.forecast.service.WorkflowImpl;
import com.uber.cadence.client.WorkflowClient;
import com.uber.cadence.client.WorkflowClientOptions;
import com.uber.cadence.serviceclient.ClientOptions;
import com.uber.cadence.serviceclient.WorkflowServiceTChannel;
import com.uber.cadence.worker.Worker;
import com.uber.cadence.worker.WorkerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class ForecastApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(ForecastApplication.class, args);
        WorkflowClient workflowClient =
                WorkflowClient.newInstance(
                        new WorkflowServiceTChannel(ClientOptions.defaultInstance()),
                        WorkflowClientOptions.newBuilder().setDomain("forecast").build());
        WorkerFactory factory = WorkerFactory.newInstance(workflowClient);
        Worker worker = factory.newWorker("ForecastActivities");
        worker.registerWorkflowImplementationTypes(WorkflowImpl.class);
        ForecastActivity activity = context.getBean(ForecastActivityImpl.class);
        worker.registerActivitiesImplementations(activity);
        factory.start();
    }

}
