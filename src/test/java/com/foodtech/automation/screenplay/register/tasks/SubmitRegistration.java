package com.foodtech.automation.screenplay.register.tasks;

import com.foodtech.automation.screenplay.register.ui.AuthenticationPageUI;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.actions.Click;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.FluentWait;

import java.time.Duration;

public class SubmitRegistration implements Performable {

    public static SubmitRegistration now() {
        return new SubmitRegistration();
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Click.on(AuthenticationPageUI.SUBMIT_BUTTON)
        );

        WebDriver driver = BrowseTheWeb.as(actor).getDriver();

        new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(10))
                .pollingEvery(Duration.ofMillis(500))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class)
                .until(d -> d.getCurrentUrl().contains("/mesero") || isVisible(d));
    }

    private boolean isVisible(WebDriver driver) {
        try {
            if (driver.findElement(By.cssSelector("[data-testid='error-message']")).isDisplayed()) return true;
        } catch (NoSuchElementException | StaleElementReferenceException ignored) {}
        try {
            return driver.findElement(By.cssSelector("[data-testid='field-error-email']")).isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }
}
