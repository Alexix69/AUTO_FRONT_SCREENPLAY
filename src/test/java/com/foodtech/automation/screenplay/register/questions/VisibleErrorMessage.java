package com.foodtech.automation.screenplay.register.questions;

import com.foodtech.automation.screenplay.register.ui.AuthenticationPageUI;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class VisibleErrorMessage implements Question<String> {

    public static VisibleErrorMessage forThePage() {
        return new VisibleErrorMessage();
    }

    @Override
    public String answeredBy(Actor actor) {
        WebDriver driver = BrowseTheWeb.as(actor).getDriver();
        try {
            WebElement el = driver.findElement(By.cssSelector("[data-testid='error-message']"));
            if (el.isDisplayed()) return el.getText();
        } catch (NoSuchElementException ignored) {}
        try {
            WebElement el = driver.findElement(By.cssSelector("[data-testid='field-error-email']"));
            if (el.isDisplayed()) return el.getText();
        } catch (NoSuchElementException ignored) {}
        return "";
    }
}
