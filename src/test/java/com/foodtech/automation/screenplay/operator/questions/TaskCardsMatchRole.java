package com.foodtech.automation.screenplay.operator.questions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TaskCardsMatchRole implements Question<Boolean> {

    private final Set<String> allowedStations;

    private TaskCardsMatchRole(Set<String> allowedStations) {
        this.allowedStations = allowedStations;
    }

    public static TaskCardsMatchRole forStation(String stationSpec) {
        Set<String> stations = Arrays.stream(stationSpec.split(","))
                .map(String::trim)
                .collect(Collectors.toSet());
        return new TaskCardsMatchRole(stations);
    }

    @Override
    public Boolean answeredBy(Actor actor) {
        WebDriver driver = BrowseTheWeb.as(actor).getDriver();
        List<WebElement> cards = driver.findElements(By.cssSelector("[data-testid^='task-card-']"));
        if (cards.isEmpty()) return false;
        return cards.stream().allMatch(card -> allowedStations.contains(card.getAttribute("data-station")));
    }
}
