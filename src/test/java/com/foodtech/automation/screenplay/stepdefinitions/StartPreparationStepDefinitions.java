package com.foodtech.automation.screenplay.stepdefinitions;

import com.foodtech.automation.screenplay.login.tasks.LoginWithCredentials;
import com.foodtech.automation.screenplay.operator.questions.ErrorBannerIsVisible;
import com.foodtech.automation.screenplay.operator.questions.StartButtonVisibleOnCards;
import com.foodtech.automation.screenplay.operator.questions.TaskMovedToInPreparation;
import com.foodtech.automation.screenplay.operator.questions.TaskRemainsInPending;
import com.foodtech.automation.screenplay.operator.tasks.ClickStartPreparation;
import com.foodtech.automation.screenplay.operator.tasks.PreStartThenClick;
import com.foodtech.automation.screenplay.operator.tasks.SelectTab;
import com.foodtech.automation.screenplay.operator.tasks.WaitForBoardToLoad;
import com.foodtech.automation.screenplay.operator.tasks.WaitForTaskToMoveToInPrep;
import com.foodtech.automation.screenplay.operator.ui.OperatorBoardUI;
import com.foodtech.automation.screenplay.support.TestContext;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actors.OnStage;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static org.hamcrest.Matchers.is;

public class StartPreparationStepDefinitions {

    private Actor actor() {
        return OnStage.theActorInTheSpotlight();
    }

    @Given("a COCINERO operator has a pending kitchen task on the board")
    public void givenCocineroHasPendingKitchenTask() {
    }

    @Given("a BARTENDER operator has a pending bar task on the board")
    public void givenBartenderHasPendingBarTask() {
    }

    @Given("a BARTENDER operator has a pending bar task and the API will fail on start")
    public void givenBartenderHasPendingBarTaskAndApiWillFail() {
    }

    @When("the operator navigates to the board and views pending tasks")
    public void theOperatorNavigatesToTheBoardAndViewsPendingTasks() {
        actor().attemptsTo(LoginWithCredentials.usingDataFrom(TestContext.getLoginUser()));
        actor().attemptsTo(SelectTab.named(OperatorBoardUI.TAB_PENDING));
        actor().attemptsTo(WaitForBoardToLoad.afterTabSelection());
    }

    @When("the operator clicks the start preparation button")
    public void theOperatorClicksTheStartPreparationButton() {
        actor().attemptsTo(ClickStartPreparation.onFirstPendingCard());
    }

    @And("the operator pre-starts the task via API then clicks the start button")
    public void theOperatorPreStartsThenClicks() {
        actor().attemptsTo(PreStartThenClick.atomically());
    }

    @Then("each task card shows the start preparation button")
    public void eachTaskCardShowsTheStartPreparationButton() {
        actor().should(seeThat(StartButtonVisibleOnCards.inPendingSection(), is(true)));
    }

    @Then("the task moves to the in preparation section")
    public void theTaskMovesToTheInPreparationSection() {
        actor().attemptsTo(WaitForTaskToMoveToInPrep.afterStarting());
        actor().should(seeThat(TaskMovedToInPreparation.afterStartAction(), is(true)));
    }

    @Then("the task remains in pending and an error banner appears")
    public void theTaskRemainsInPendingAndErrorBannerAppears() {
        actor().should(seeThat(TaskRemainsInPending.afterFailedAction(), is(true)));
        actor().should(seeThat(ErrorBannerIsVisible.afterFailedAction(), is(true)));
    }
}
