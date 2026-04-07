package com.foodtech.automation.screenplay.login.questions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;

public class RedirectedToPath implements Question<Boolean> {

    private final String path;

    private RedirectedToPath(String path) {
        this.path = path;
    }

    public static RedirectedToPath containing(String path) {
        return new RedirectedToPath(path);
    }

    @Override
    public Boolean answeredBy(Actor actor) {
        return BrowseTheWeb.as(actor).getDriver().getCurrentUrl().contains(path);
    }
}
