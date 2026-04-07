package com.foodtech.automation.screenplay.operator.questions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class TasksAreInFifoOrder implements Question<Boolean> {

    public static TasksAreInFifoOrder inPendingSection() {
        return new TasksAreInFifoOrder();
    }

    @Override
    public Boolean answeredBy(Actor actor) {
        WebDriver driver = BrowseTheWeb.as(actor).getDriver();
        List<WebElement> cards = driver.findElements(By.cssSelector("[data-testid^='task-card-']"));
        if (cards.size() < 2) return true;
        List<Long> ids = cards.stream()
                .map(card -> {
                    String testId = card.getAttribute("data-testid");
                    return Long.parseLong(testId.substring("task-card-".length()));
                })
                .toList();
        for (int i = 1; i < ids.size(); i++) {
            if (ids.get(i) < ids.get(i - 1)) return false;
        }
        return true;
    }
}
