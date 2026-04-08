package com.foodtech.automation.screenplay.operator.tasks;

import com.foodtech.automation.screenplay.support.OrderApiClient;
import com.foodtech.automation.screenplay.support.TestContext;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.targets.Target;

public class PreCompleteTaskThenClick implements Task {

    public static PreCompleteTaskThenClick atomically() {
        return Tasks.instrumented(PreCompleteTaskThenClick.class);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        Long taskId = TestContext.getTaskId();
        OrderApiClient.completeTask(TestContext.getOperatorToken(), taskId);
        Target specific = Target.the("complete task button for task " + taskId)
                .locatedBy("[data-testid='complete-task-btn-" + taskId + "']");
        specific.resolveFor(actor).click();
    }
}
