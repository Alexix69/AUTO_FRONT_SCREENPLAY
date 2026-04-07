package com.foodtech.automation.screenplay.operator.questions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class StartButtonVisibleOnCards implements Question<Boolean> {

    public static StartButtonVisibleOnCards inPendingSection() {
        return new StartButtonVisibleOnCards();
    }

    @Override
    public Boolean answeredBy(Actor actor) {
        WebDriver driver = BrowseTheWeb.as(actor).getDriver();
        List<WebElement> buttons = driver.findElements(By.cssSelector("[data-testid^='start-task-btn-']"));
        return !buttons.isEmpty() && buttons.stream().anyMatch(WebElement::isDisplayed);
    }
}
