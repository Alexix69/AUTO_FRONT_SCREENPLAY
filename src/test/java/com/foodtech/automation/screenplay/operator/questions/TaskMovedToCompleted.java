package com.foodtech.automation.screenplay.operator.questions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class TaskMovedToCompleted implements Question<Boolean> {

    public static TaskMovedToCompleted afterFinishAction() {
        return new TaskMovedToCompleted();
    }

    @Override
    public Boolean answeredBy(Actor actor) {
        WebDriver driver = BrowseTheWeb.as(actor).getDriver();
        List<WebElement> cards = driver.findElements(By.cssSelector("[data-testid^='task-card-']"));
        return !cards.isEmpty() && cards.stream().anyMatch(WebElement::isDisplayed);
    }
}
