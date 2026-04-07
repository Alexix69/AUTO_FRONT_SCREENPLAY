package com.foodtech.automation.screenplay.operator.questions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class TaskRemainsInPending implements Question<Boolean> {

    public static TaskRemainsInPending afterFailedAction() {
        return new TaskRemainsInPending();
    }

    @Override
    public Boolean answeredBy(Actor actor) {
        WebDriver driver = BrowseTheWeb.as(actor).getDriver();
        return !driver.findElements(By.cssSelector("[data-testid^='task-card-']")).isEmpty();
    }
}
