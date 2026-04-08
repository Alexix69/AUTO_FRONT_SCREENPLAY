package com.foodtech.automation.screenplay.operator.questions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class FinishButtonVisibleOnInPrepCards implements Question<Boolean> {

    public static FinishButtonVisibleOnInPrepCards inInPrepSection() {
        return new FinishButtonVisibleOnInPrepCards();
    }

    @Override
    public Boolean answeredBy(Actor actor) {
        WebDriver driver = BrowseTheWeb.as(actor).getDriver();
        try {
            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(d -> !d.findElements(By.cssSelector("[data-testid^='complete-task-btn-']")).isEmpty());
            List<WebElement> buttons = driver.findElements(By.cssSelector("[data-testid^='complete-task-btn-']"));
            return buttons.stream().anyMatch(WebElement::isDisplayed);
        } catch (Exception e) {
            return false;
        }
    }
}
