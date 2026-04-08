package com.foodtech.automation.screenplay.operator.tasks;

import com.foodtech.automation.screenplay.support.OrderApiClient;
import com.foodtech.automation.screenplay.support.TestContext;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.targets.Target;

public class PreStartThenClick implements Task {

    public static PreStartThenClick atomically() {
        return Tasks.instrumented(PreStartThenClick.class);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        Long taskId = TestContext.getTaskId();
        OrderApiClient.startTask(TestContext.getOperatorToken(), taskId);
        Target specific = Target.the("start task button for task " + taskId)
                .locatedBy("[data-testid='start-task-btn-" + taskId + "']");
        specific.resolveFor(actor).click();
    }
}
