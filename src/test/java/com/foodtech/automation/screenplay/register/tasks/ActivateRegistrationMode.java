package com.foodtech.automation.screenplay.register.tasks;

import com.foodtech.automation.screenplay.register.ui.AuthenticationPageUI;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.matchers.WebElementStateMatchers;
import net.serenitybdd.screenplay.waits.WaitUntil;

public class ActivateRegistrationMode implements Performable {

    public static ActivateRegistrationMode now() {
        return new ActivateRegistrationMode();
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Click.on(AuthenticationPageUI.TOGGLE_MODE_BUTTON),
                WaitUntil.the(AuthenticationPageUI.USERNAME_INPUT, WebElementStateMatchers.isVisible())
                        .forNoMoreThan(5).seconds()
        );
    }
}
