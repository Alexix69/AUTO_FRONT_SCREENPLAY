package com.foodtech.automation.screenplay.operator.tasks;

import com.foodtech.automation.screenplay.operator.ui.OperatorBoardUI;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WaitForTaskToMoveToInPrep implements Task {

    public static WaitForTaskToMoveToInPrep afterStarting() {
        return Tasks.instrumented(WaitForTaskToMoveToInPrep.class);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(SelectTab.named(OperatorBoardUI.TAB_IN_PREPARATION));
        WebDriver driver = BrowseTheWeb.as(actor).getDriver();
        new WebDriverWait(driver, Duration.ofSeconds(8))
                .until(d -> !d.findElements(By.cssSelector("[data-testid^='task-card-']")).isEmpty());
    }
}
