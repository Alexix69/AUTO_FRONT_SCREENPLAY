package com.foodtech.automation.screenplay.login.tasks;

import com.foodtech.automation.screenplay.login.ui.AccessDeniedPageUI;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.actions.Click;

public class ClickBackButton implements Task {

    public static ClickBackButton onAccessDeniedPage() {
        return Tasks.instrumented(ClickBackButton.class);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Click.on(AccessDeniedPageUI.REGRESAR_BUTTON)
        );
    }
}
