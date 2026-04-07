package com.foodtech.automation.screenplay.login.tasks;

import com.foodtech.automation.screenplay.login.ui.AccessDeniedPageUI;
import com.foodtech.automation.screenplay.support.TestConfig;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.actions.Open;
import net.serenitybdd.screenplay.matchers.WebElementStateMatchers;
import net.serenitybdd.screenplay.waits.WaitUntil;

public class NavigateToUnauthorizedRoute implements Performable {

    private final String path;

    private NavigateToUnauthorizedRoute(String path) {
        this.path = path;
    }

    public static NavigateToUnauthorizedRoute at(String path) {
        return new NavigateToUnauthorizedRoute(path);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Open.url(TestConfig.getBaseUrl() + path),
                WaitUntil.the(AccessDeniedPageUI.REGRESAR_BUTTON, WebElementStateMatchers.isVisible())
                        .forNoMoreThan(5).seconds()
        );
    }
}
