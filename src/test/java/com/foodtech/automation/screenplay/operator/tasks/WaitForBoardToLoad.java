package com.foodtech.automation.screenplay.operator.tasks;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WaitForBoardToLoad implements Task {

    public static WaitForBoardToLoad afterTabSelection() {
        return Tasks.instrumented(WaitForBoardToLoad.class);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        WebDriver driver = BrowseTheWeb.as(actor).getDriver();
        new WebDriverWait(driver, Duration.ofSeconds(8))
                .until(d -> !d.findElements(By.cssSelector("[data-testid^='task-card-']")).isEmpty()
                        || !d.findElements(By.cssSelector("[data-testid='empty-tasks-message']")).isEmpty());
    }
}
