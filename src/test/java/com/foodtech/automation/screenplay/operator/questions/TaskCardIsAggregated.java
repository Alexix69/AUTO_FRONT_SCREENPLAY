package com.foodtech.automation.screenplay.operator.questions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class TaskCardIsAggregated implements Question<Boolean> {

    private final int expected;

    private TaskCardIsAggregated(int expected) {
        this.expected = expected;
    }

    public static TaskCardIsAggregated withQuantity(int expected) {
        return new TaskCardIsAggregated(expected);
    }

    @Override
    public Boolean answeredBy(Actor actor) {
        WebDriver driver = BrowseTheWeb.as(actor).getDriver();
        List<WebElement> productRows = driver.findElements(By.cssSelector("[data-testid='task-product-0']"));
        if (productRows.isEmpty()) return false;
        String qty = productRows.get(0).getAttribute("data-product-quantity");
        return String.valueOf(expected).equals(qty);
    }
}
