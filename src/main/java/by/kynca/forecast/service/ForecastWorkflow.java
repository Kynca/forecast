package by.kynca.forecast.service;

import com.uber.cadence.workflow.WorkflowMethod;

public interface ForecastWorkflow {
    @WorkflowMethod(executionStartToCloseTimeoutSeconds = 80)
    boolean processActivity(String name);
}
