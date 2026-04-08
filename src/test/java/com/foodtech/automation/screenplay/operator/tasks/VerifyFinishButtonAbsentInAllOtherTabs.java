package com.foodtech.automation.screenplay.operator.tasks;

import com.foodtech.automation.screenplay.operator.questions.FinishButtonAbsentFromTab;
import com.foodtech.automation.screenplay.operator.ui.OperatorBoardUI;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static org.hamcrest.Matchers.is;

public class VerifyFinishButtonAbsentInAllOtherTabs implements Task {

    public static VerifyFinishButtonAbsentInAllOtherTabs onBothNonPrepTabs() {
        return Tasks.instrumented(VerifyFinishButtonAbsentInAllOtherTabs.class);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                SelectTab.named(OperatorBoardUI.TAB_PENDING),
                WaitForBoardToLoad.afterTabSelection()
        );
        actor.should(seeThat(FinishButtonAbsentFromTab.inCurrentTab(), is(true)));
        actor.attemptsTo(
                SelectTab.named(OperatorBoardUI.TAB_COMPLETED),
                WaitForBoardToLoad.afterTabSelection()
        );
        actor.should(seeThat(FinishButtonAbsentFromTab.inCurrentTab(), is(true)));
    }
}
