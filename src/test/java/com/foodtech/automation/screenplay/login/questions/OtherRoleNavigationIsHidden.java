package com.foodtech.automation.screenplay.login.questions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class OtherRoleNavigationIsHidden implements Question<Boolean> {

    public static OtherRoleNavigationIsHidden inTheNavigationBar() {
        return new OtherRoleNavigationIsHidden();
    }

    @Override
    public Boolean answeredBy(Actor actor) {
        WebDriver driver = BrowseTheWeb.as(actor).getDriver();
        return driver.findElements(By.cssSelector("[data-testid='nav-barra']")).isEmpty()
                && driver.findElements(By.cssSelector("[data-testid='nav-cocina']")).isEmpty();
    }
}
