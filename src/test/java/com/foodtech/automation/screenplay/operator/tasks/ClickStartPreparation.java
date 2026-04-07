package com.foodtech.automation.screenplay.operator.tasks;

import com.foodtech.automation.screenplay.operator.ui.StartPreparationUI;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.actions.Click;

public class ClickStartPreparation implements Task {

    public static ClickStartPreparation onFirstPendingCard() {
        return Tasks.instrumented(ClickStartPreparation.class);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(Click.on(StartPreparationUI.START_TASK_BUTTON));
    }
}
