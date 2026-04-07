package com.foodtech.automation.screenplay.login.tasks;

import com.foodtech.automation.screenplay.register.ui.AuthenticationPageUI;
import com.foodtech.automation.screenplay.support.LoginData;
import com.foodtech.automation.screenplay.support.TestConfig;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.actions.Open;
import net.serenitybdd.screenplay.matchers.WebElementStateMatchers;
import net.serenitybdd.screenplay.waits.WaitUntil;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.FluentWait;

import java.time.Duration;

public class LoginWithCredentials implements Performable {

    private final LoginData data;

    private LoginWithCredentials(LoginData data) {
        this.data = data;
    }

    public static LoginWithCredentials usingDataFrom(LoginData data) {
        return new LoginWithCredentials(data);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Open.url(TestConfig.getBaseUrl() + "/login"),
                WaitUntil.the(AuthenticationPageUI.SUBMIT_BUTTON, WebElementStateMatchers.isVisible())
                        .forNoMoreThan(5).seconds(),
                WaitUntil.the(AuthenticationPageUI.DEMO_MODE_CHECKBOX, WebElementStateMatchers.isPresent())
                        .forNoMoreThan(3).seconds()
        );

        boolean demoModeActive = AuthenticationPageUI.DEMO_MODE_CHECKBOX
                .resolveFor(actor)
                .isSelected();

        if (demoModeActive) {
            throw new IllegalStateException("Precondition failed: demo mode is active");
        }

        actor.attemptsTo(
                Enter.theValue(data.email()).into(AuthenticationPageUI.EMAIL_INPUT),
                Enter.theValue(data.password()).into(AuthenticationPageUI.PASSWORD_INPUT),
                Click.on(AuthenticationPageUI.SUBMIT_BUTTON)
        );

        WebDriver driver = BrowseTheWeb.as(actor).getDriver();

        new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(10))
                .pollingEvery(Duration.ofMillis(500))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class)
                .until(d -> d.getCurrentUrl().contains("/mesero")
                        || d.getCurrentUrl().contains("/barra")
                        || d.getCurrentUrl().contains("/cocina")
                        || isErrorVisible(d));
    }

    private boolean isErrorVisible(WebDriver driver) {
        try {
            return driver.findElement(By.cssSelector("[data-testid='error-message']")).isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }
}
