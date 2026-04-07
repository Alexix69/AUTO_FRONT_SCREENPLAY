package com.foodtech.automation.screenplay.login.questions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class IsOnAccessDeniedPage implements Question<Boolean> {

    private IsOnAccessDeniedPage() {
    }

    public static IsOnAccessDeniedPage now() {
        return new IsOnAccessDeniedPage();
    }

    @Override
    public Boolean answeredBy(Actor actor) {
        WebDriver driver = BrowseTheWeb.as(actor).getDriver();
        List<WebElement> elements = driver.findElements(By.cssSelector("[data-testid='regresar-btn']"));
        return !elements.isEmpty() && elements.get(0).isDisplayed();
    }
}
