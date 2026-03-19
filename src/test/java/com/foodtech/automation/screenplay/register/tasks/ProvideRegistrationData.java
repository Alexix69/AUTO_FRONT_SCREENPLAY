package com.foodtech.automation.screenplay.register.tasks;

import com.foodtech.automation.screenplay.register.ui.AuthenticationPageUI;
import com.foodtech.automation.screenplay.support.RegistrationData;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.actions.Enter;

public class ProvideRegistrationData implements Performable {

    private final RegistrationData data;

    private ProvideRegistrationData(RegistrationData data) {
        this.data = data;
    }

    public static ProvideRegistrationData with(RegistrationData data) {
        return new ProvideRegistrationData(data);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Enter.theValue(data.email()).into(AuthenticationPageUI.EMAIL_INPUT),
                Enter.theValue(data.username()).into(AuthenticationPageUI.USERNAME_INPUT),
                Enter.theValue(data.password()).into(AuthenticationPageUI.PASSWORD_INPUT)
        );
    }
}
