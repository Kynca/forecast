package by.kynca.forecast.service;


import by.kynca.forecast.bean.Weather;
import com.uber.cadence.workflow.Workflow;

public class WorkflowImpl implements ForecastWorkflow {

    private final ForecastActivity activity;

    public WorkflowImpl() {
        this.activity = Workflow.newActivityStub(ForecastActivity.class);
    }

    @Override
    public boolean processActivity(String name) {
        System.out.println("city is " + name);
        Double temperature = activity.getAirTemperature(name);
        if(temperature == null){
            System.out.println("temperature for " + name + "cant be found");
            return false;
        }
        activity.storeInfo(temperature, name);
        return true;
    }
}


