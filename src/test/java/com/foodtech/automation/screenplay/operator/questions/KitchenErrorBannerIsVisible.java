package com.foodtech.automation.screenplay.operator.questions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class KitchenErrorBannerIsVisible implements Question<Boolean> {

    public static KitchenErrorBannerIsVisible afterFailedAction() {
        return new KitchenErrorBannerIsVisible();
    }

    @Override
    public Boolean answeredBy(Actor actor) {
        WebDriver driver = BrowseTheWeb.as(actor).getDriver();
        try {
            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(d -> !d.findElements(By.cssSelector("[data-testid='kitchen-error']")).isEmpty()
                            && d.findElement(By.cssSelector("[data-testid='kitchen-error']")).isDisplayed());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
