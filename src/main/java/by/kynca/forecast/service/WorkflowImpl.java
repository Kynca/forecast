package by.kynca.forecast.service;


import com.uber.cadence.workflow.Workflow;

public class WorkflowImpl implements ForecastWorkflow {

    private final ForecastActivity activity;

    public WorkflowImpl() {
        this.activity = Workflow.newActivityStub(ForecastActivity.class);
    }

    @Override
    public boolean processActivity(String city) {
        System.out.println("city is " + city);
        Double temperature = activity.getAirTemperature(city);
        if(temperature == null){
            System.out.println("temperature for " + city + "cant be found");
            return false;
        }
        activity.storeInfo(temperature, city);
        System.out.println("temperature " + temperature + " for " + city + " is saved");
        return true;
    }
}


