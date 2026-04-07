package com.foodtech.automation.screenplay.operator.questions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class EmptyStateIsVisible implements Question<Boolean> {

    public static EmptyStateIsVisible onTheBoard() {
        return new EmptyStateIsVisible();
    }

    @Override
    public Boolean answeredBy(Actor actor) {
        WebDriver driver = BrowseTheWeb.as(actor).getDriver();
        List<WebElement> elements = driver.findElements(By.cssSelector("[data-testid='empty-tasks-message']"));
        return !elements.isEmpty() && elements.get(0).isDisplayed();
    }
}
