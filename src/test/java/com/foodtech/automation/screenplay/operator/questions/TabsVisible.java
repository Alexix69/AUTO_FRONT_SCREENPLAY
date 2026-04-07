package com.foodtech.automation.screenplay.operator.questions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class TabsVisible implements Question<Boolean> {

    public static TabsVisible onTheBoard() {
        return new TabsVisible();
    }

    @Override
    public Boolean answeredBy(Actor actor) {
        WebDriver driver = BrowseTheWeb.as(actor).getDriver();
        List<WebElement> pending = driver.findElements(By.cssSelector("[data-testid='tab-pending']"));
        List<WebElement> inPrep = driver.findElements(By.cssSelector("[data-testid='tab-in-preparation']"));
        List<WebElement> completed = driver.findElements(By.cssSelector("[data-testid='tab-completed']"));
        return !pending.isEmpty() && pending.get(0).isDisplayed()
                && !inPrep.isEmpty() && inPrep.get(0).isDisplayed()
                && !completed.isEmpty() && completed.get(0).isDisplayed();
    }
}
