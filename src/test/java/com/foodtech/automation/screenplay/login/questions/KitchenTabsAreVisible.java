package com.foodtech.automation.screenplay.login.questions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class KitchenTabsAreVisible implements Question<Boolean> {

    public static KitchenTabsAreVisible forThePage() {
        return new KitchenTabsAreVisible();
    }

    @Override
    public Boolean answeredBy(Actor actor) {
        try {
            WebDriver driver = BrowseTheWeb.as(actor).getDriver();
            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.visibilityOfElementLocated(
                            By.cssSelector("[data-testid='tab-pending']")));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
