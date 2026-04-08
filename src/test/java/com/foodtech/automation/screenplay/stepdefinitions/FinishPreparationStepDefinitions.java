package com.foodtech.automation.screenplay.stepdefinitions;

import com.foodtech.automation.screenplay.operator.questions.FinishButtonVisibleOnInPrepCards;
import com.foodtech.automation.screenplay.operator.questions.KitchenErrorBannerIsVisible;
import com.foodtech.automation.screenplay.operator.questions.TaskMovedToCompleted;
import com.foodtech.automation.screenplay.operator.questions.TaskRemainsInInPreparation;
import com.foodtech.automation.screenplay.operator.tasks.ClickFinishPreparation;
import com.foodtech.automation.screenplay.operator.tasks.NavigateToOperatorTab;
import com.foodtech.automation.screenplay.operator.tasks.PreCompleteTaskThenClick;
import com.foodtech.automation.screenplay.operator.tasks.VerifyFinishButtonAbsentInAllOtherTabs;
import com.foodtech.automation.screenplay.operator.tasks.WaitForTaskToMoveToCompleted;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actors.OnStage;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static org.hamcrest.Matchers.is;

public class FinishPreparationStepDefinitions {

    private Actor actor() {
        return OnStage.theActorInTheSpotlight();
    }

    @Given("a COCINERO operator has a task in IN_PREPARATION state on the kitchen board")
    public void givenCocineroHasTaskInPreparation() {
    }

    @Given("a COCINERO operator is authenticated and the board has tasks in Pendientes and Completadas")
    public void givenCocineroHasTasksInPendientesAndCompletadas() {
    }

    @Given("a COCINERO operator has a task in En Preparacion and the API will fail on complete")
    public void givenCocineroHasTaskInPrepAndApiWillFail() {
    }

    @When("the operator navigates to the board and views the in preparation section")
    public void theOperatorNavigatesToTheBoardAndViewsInPrep() {
        actor().attemptsTo(NavigateToOperatorTab.inPreparation());
    }

    @And("the operator clicks the finish preparation button")
    public void theOperatorClicksTheFinishPreparationButton() {
        actor().attemptsTo(ClickFinishPreparation.onFirstInPrepCard());
    }

    @And("the operator pre-completes the task via API then clicks the finish button")
    public void theOperatorPreCompletesTaskViaApiThenClicksFinishButton() {
        actor().attemptsTo(PreCompleteTaskThenClick.atomically());
    }

    @Then("each task card in En Preparacion displays an enabled Completar button")
    public void eachTaskCardInInPrepDisplaysCompletarButton() {
        actor().should(seeThat(FinishButtonVisibleOnInPrepCards.inInPrepSection(), is(true)));
    }

    @Then("no task card in Pendientes or Completadas displays the Completar button")
    public void noTaskCardInOtherTabsDisplaysCompletarButton() {
        actor().attemptsTo(VerifyFinishButtonAbsentInAllOtherTabs.onBothNonPrepTabs());
    }

    @Then("the task moves to the completed section")
    public void theTaskMovesToTheCompletedSection() {
        actor().attemptsTo(WaitForTaskToMoveToCompleted.afterFinishing());
        actor().should(seeThat(TaskMovedToCompleted.afterFinishAction(), is(true)));
    }

    @Then("the task remains in En Preparacion and an error banner is displayed")
    public void theTaskRemainsInPrepAndErrorBannerIsDisplayed() {
        actor().should(seeThat(TaskRemainsInInPreparation.afterFailedAction(), is(true)));
        actor().should(seeThat(KitchenErrorBannerIsVisible.afterFailedAction(), is(true)));
    }
}
