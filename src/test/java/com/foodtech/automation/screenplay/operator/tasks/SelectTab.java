package com.foodtech.automation.screenplay.operator.tasks;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.targets.Target;

public class SelectTab implements Performable {

    private final Target tab;

    private SelectTab(Target tab) {
        this.tab = tab;
    }

    public static SelectTab named(Target tab) {
        return new SelectTab(tab);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(Click.on(tab));
    }
}
