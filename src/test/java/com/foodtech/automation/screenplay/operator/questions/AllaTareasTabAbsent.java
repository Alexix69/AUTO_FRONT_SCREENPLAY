package com.foodtech.automation.screenplay.operator.questions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AllaTareasTabAbsent implements Question<Boolean> {

    public static AllaTareasTabAbsent onTheBoard() {
        return new AllaTareasTabAbsent();
    }

    @Override
    public Boolean answeredBy(Actor actor) {
        WebDriver driver = BrowseTheWeb.as(actor).getDriver();
        return driver.findElements(By.xpath("//button[normalize-space()='Todas las tareas']")).isEmpty();
    }
}
