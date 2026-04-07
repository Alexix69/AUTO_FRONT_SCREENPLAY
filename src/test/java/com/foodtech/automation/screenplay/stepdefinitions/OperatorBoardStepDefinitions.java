package com.foodtech.automation.screenplay.stepdefinitions;

import com.foodtech.automation.screenplay.login.tasks.LoginWithCredentials;
import com.foodtech.automation.screenplay.operator.questions.AllaTareasTabAbsent;
import com.foodtech.automation.screenplay.operator.questions.EmptyStateIsVisible;
import com.foodtech.automation.screenplay.operator.questions.TabsVisible;
import com.foodtech.automation.screenplay.operator.questions.TaskCardHasRequiredFields;
import com.foodtech.automation.screenplay.operator.questions.TaskCardIsAggregated;
import com.foodtech.automation.screenplay.operator.questions.TaskCardsMatchRole;
import com.foodtech.automation.screenplay.operator.questions.TasksAreInFifoOrder;
import com.foodtech.automation.screenplay.operator.tasks.SelectTab;
import com.foodtech.automation.screenplay.operator.tasks.WaitForBoardToLoad;
import com.foodtech.automation.screenplay.operator.ui.OperatorBoardUI;
import com.foodtech.automation.screenplay.support.TestContext;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.targets.Target;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static org.hamcrest.Matchers.is;

public class OperatorBoardStepDefinitions {

    private Actor actor() {
        return OnStage.theActorInTheSpotlight();
    }

    @Given("a COCINERO operator is authenticated with pending kitchen orders")
    public void givenCocineroWithKitchenOrders() {
    }

    @Given("a BARTENDER operator is authenticated with pending bar orders")
    public void givenBartenderWithBarOrders() {
    }

    @Given("a COCINERO operator is authenticated with mixed station orders")
    public void givenCocineroWithMixedOrders() {
    }

    @Given("a COCINERO operator is authenticated with an aggregated kitchen order")
    public void givenCocineroWithAggregatedOrder() {
    }

    @Given("a COCINERO operator is authenticated with a kitchen order")
    public void givenCocineroWithKitchenOrder() {
    }

    @Given("a BARTENDER operator is authenticated with three sequential bar orders")
    public void givenBartenderWithThreeBarOrders() {
    }

    @Given("a COCINERO operator is authenticated with no pending orders")
    public void givenCocineroWithNoPendingOrders() {
    }

    @When("the operator navigates to their board")
    public void theOperatorNavigatesToTheirBoard() {
        actor().attemptsTo(LoginWithCredentials.usingDataFrom(TestContext.getLoginUser()));
        actor().attemptsTo(WaitForBoardToLoad.afterTabSelection());
    }

    @When("the operator selects the {string} tab")
    public void theOperatorSelectsTheTab(String tabName) {
        actor().attemptsTo(SelectTab.named(tabTargetFor(tabName)));
        actor().attemptsTo(WaitForBoardToLoad.afterTabSelection());
    }

    @Then("the board displays the three operator tabs")
    public void theBoardDisplaysTheThreeOperatorTabs() {
        actor().should(seeThat(TabsVisible.onTheBoard(), is(true)));
    }

    @Then("the Todas las tareas tab is not visible")
    public void todasLasTareasTabIsNotVisible() {
        actor().should(seeThat(AllaTareasTabAbsent.onTheBoard(), is(true)));
    }

    @Then("only tasks for their station are displayed")
    public void onlyTasksForTheirStationAreDisplayed() {
        actor().should(seeThat(TaskCardsMatchRole.forStation(TestContext.getExpectedStation()), is(true)));
    }

    @Then("the task is shown as a single card with quantity {int}")
    public void theTaskIsShownAsASingleCardWithQuantity(int quantity) {
        actor().should(seeThat(TaskCardIsAggregated.withQuantity(quantity), is(true)));
    }

    @Then("each task card shows the required fields")
    public void eachTaskCardShowsTheRequiredFields() {
        actor().should(seeThat(TaskCardHasRequiredFields.displayed(), is(true)));
    }

    @Then("tasks are displayed in FIFO order")
    public void tasksAreDisplayedInFifoOrder() {
        actor().should(seeThat(TasksAreInFifoOrder.inPendingSection(), is(true)));
    }

    @Then("an empty state message is displayed")
    public void anEmptyStateMessageIsDisplayed() {
        actor().should(seeThat(EmptyStateIsVisible.onTheBoard(), is(true)));
    }

    private Target tabTargetFor(String tabName) {
        return switch (tabName) {
            case "Pendientes" -> OperatorBoardUI.TAB_PENDING;
            case "En Preparación" -> OperatorBoardUI.TAB_IN_PREPARATION;
            case "Completadas" -> OperatorBoardUI.TAB_COMPLETED;
            default -> throw new IllegalArgumentException("Unknown tab name: " + tabName);
        };
    }
}
