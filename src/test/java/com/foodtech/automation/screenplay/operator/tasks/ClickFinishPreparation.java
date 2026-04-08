package com.foodtech.automation.screenplay.operator.tasks;

import com.foodtech.automation.screenplay.operator.ui.FinishPreparationUI;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.actions.Click;

public class ClickFinishPreparation implements Task {

    public static ClickFinishPreparation onFirstInPrepCard() {
        return Tasks.instrumented(ClickFinishPreparation.class);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(Click.on(FinishPreparationUI.COMPLETE_TASK_BUTTON));
    }
}
