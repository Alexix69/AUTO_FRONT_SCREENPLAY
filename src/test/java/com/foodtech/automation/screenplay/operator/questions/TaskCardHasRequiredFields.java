package com.foodtech.automation.screenplay.operator.questions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class TaskCardHasRequiredFields implements Question<Boolean> {

    public static TaskCardHasRequiredFields displayed() {
        return new TaskCardHasRequiredFields();
    }

    @Override
    public Boolean answeredBy(Actor actor) {
        WebDriver driver = BrowseTheWeb.as(actor).getDriver();
        List<WebElement> cards = driver.findElements(By.cssSelector("[data-testid^='task-card-']"));
        if (cards.isEmpty()) return false;
        WebElement card = cards.get(0);

        List<WebElement> tableNumbers = card.findElements(By.cssSelector("[data-testid='task-table-number']"));
        if (tableNumbers.isEmpty() || !tableNumbers.get(0).isDisplayed()) return false;
        String tableText = tableNumbers.get(0).getText();
        if (tableText == null || tableText.isBlank()) return false;

        List<WebElement> productRows = card.findElements(By.cssSelector("[data-testid='task-product-0']"));
        if (productRows.isEmpty()) return false;
        WebElement productRow = productRows.get(0);

        String productName = productRow.getAttribute("data-product-name");
        String productQty = productRow.getAttribute("data-product-quantity");
        if (productName == null || productName.isBlank()) return false;
        if (productQty == null || productQty.isBlank()) return false;

        String cardText = card.getText();
        return cardText != null && cardText.matches("(?s).*\\d{2}:\\d{2}.*");
    }
}
