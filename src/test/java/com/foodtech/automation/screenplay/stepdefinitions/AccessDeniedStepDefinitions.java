package com.foodtech.automation.screenplay.stepdefinitions;

import com.foodtech.automation.screenplay.login.questions.IsOnAccessDeniedPage;
import com.foodtech.automation.screenplay.login.questions.RedirectedToPath;
import com.foodtech.automation.screenplay.login.tasks.ClickBackButton;
import com.foodtech.automation.screenplay.login.tasks.NavigateToUnauthorizedRoute;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actors.OnStage;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static org.hamcrest.Matchers.is;

public class AccessDeniedStepDefinitions {

    private Actor actor() {
        return OnStage.theActorInTheSpotlight();
    }

    @Given("a staff member is authenticated and positioned on their role home view")
    public void aStaffMemberIsAuthenticatedAndPositionedOnTheirRoleHomeView() {
    }

    @When("the user navigates directly to {string}")
    public void theUserNavigatesDirectlyTo(String path) {
        actor().attemptsTo(NavigateToUnauthorizedRoute.at(path));
    }

    @Then("the access denied page is displayed")
    public void theAccessDeniedPageIsDisplayed() {
        actor().should(seeThat(IsOnAccessDeniedPage.now(), is(true)));
    }

    @When("the user clicks the back button")
    public void theUserClicksTheBackButton() {
        actor().attemptsTo(ClickBackButton.onAccessDeniedPage());
    }

    @Then("the user is redirected to their role home route")
    public void theUserIsRedirectedToTheirRoleHomeRoute() {
        actor().should(seeThat(RedirectedToPath.containing("/barra"), is(true)));
    }
}
