package com.foodtech.automation.screenplay.operator.tasks;

import com.foodtech.automation.screenplay.login.tasks.LoginWithCredentials;
import com.foodtech.automation.screenplay.operator.ui.OperatorBoardUI;
import com.foodtech.automation.screenplay.support.TestContext;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.targets.Target;

public class NavigateToOperatorTab implements Performable {

    private final Target tab;

    private NavigateToOperatorTab(Target tab) {
        this.tab = tab;
    }

    public static NavigateToOperatorTab pending() {
        return new NavigateToOperatorTab(OperatorBoardUI.TAB_PENDING);
    }

    public static NavigateToOperatorTab inPreparation() {
        return new NavigateToOperatorTab(OperatorBoardUI.TAB_IN_PREPARATION);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                LoginWithCredentials.usingDataFrom(TestContext.getLoginUser()),
                SelectTab.named(tab),
                WaitForBoardToLoad.afterTabSelection()
        );
    }
}
