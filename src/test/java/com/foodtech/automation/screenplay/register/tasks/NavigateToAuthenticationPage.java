package com.foodtech.automation.screenplay.register.tasks;

import com.foodtech.automation.screenplay.register.ui.AuthenticationPageUI;
import com.foodtech.automation.screenplay.support.TestConfig;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.actions.Open;
import net.serenitybdd.screenplay.matchers.WebElementStateMatchers;
import net.serenitybdd.screenplay.waits.WaitUntil;

public class NavigateToAuthenticationPage implements Performable {

    public static NavigateToAuthenticationPage now() {
        return new NavigateToAuthenticationPage();
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Open.url(TestConfig.getBaseUrl() + "/login"),
                WaitUntil.the(AuthenticationPageUI.SUBMIT_BUTTON, WebElementStateMatchers.isVisible())
                        .forNoMoreThan(5).seconds(),
                WaitUntil.the(AuthenticationPageUI.DEMO_MODE_CHECKBOX, WebElementStateMatchers.isPresent())
                        .forNoMoreThan(3).seconds()
        );

        boolean demoModeActive = AuthenticationPageUI.DEMO_MODE_CHECKBOX
                .resolveFor(actor)
                .isSelected();

        if (demoModeActive) {
            throw new IllegalStateException("Precondition failed: demo mode is active");
        }
    }
}
