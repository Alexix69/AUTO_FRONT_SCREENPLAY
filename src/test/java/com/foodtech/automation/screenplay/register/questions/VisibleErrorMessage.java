package com.foodtech.automation.screenplay.register.questions;

import com.foodtech.automation.screenplay.register.ui.AuthenticationPageUI;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;

public class VisibleErrorMessage implements Question<String> {

    public static VisibleErrorMessage forThePage() {
        return new VisibleErrorMessage();
    }

    @Override
    public String answeredBy(Actor actor) {
        return AuthenticationPageUI.ERROR_MESSAGE.resolveFor(actor).getText();
    }
}
