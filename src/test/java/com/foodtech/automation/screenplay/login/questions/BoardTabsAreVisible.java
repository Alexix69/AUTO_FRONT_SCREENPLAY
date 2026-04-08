package com.foodtech.automation.screenplay.login.questions;

import com.foodtech.automation.screenplay.operator.ui.OperatorBoardUI;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BoardTabsAreVisible implements Question<Boolean> {

    public static BoardTabsAreVisible forThePage() {
        return new BoardTabsAreVisible();
    }

    @Override
    public Boolean answeredBy(Actor actor) {
        try {
            WebDriver driver = BrowseTheWeb.as(actor).getDriver();
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.visibilityOf(
                    OperatorBoardUI.TAB_PENDING.resolveFor(actor)));
            wait.until(ExpectedConditions.visibilityOf(
                    OperatorBoardUI.TAB_IN_PREPARATION.resolveFor(actor)));
            wait.until(ExpectedConditions.visibilityOf(
                    OperatorBoardUI.TAB_COMPLETED.resolveFor(actor)));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
