package com.foodtech.automation.screenplay.login.questions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.By;

public class NavigationElementIsAbsent implements Question<Boolean> {

    private final String testId;

    private NavigationElementIsAbsent(String testId) {
        this.testId = testId;
    }

    public static NavigationElementIsAbsent withTestId(String testId) {
        return new NavigationElementIsAbsent(testId);
    }

    @Override
    public Boolean answeredBy(Actor actor) {
        return BrowseTheWeb.as(actor).getDriver()
                .findElements(By.cssSelector("[data-testid='" + testId + "']"))
                .isEmpty();
    }
}
