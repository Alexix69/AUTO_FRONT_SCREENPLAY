package com.foodtech.automation.screenplay.stepdefinitions;

import com.foodtech.automation.screenplay.login.questions.BoardTabsAreVisible;
import com.foodtech.automation.screenplay.login.questions.OtherRoleNavigationIsHidden;
import com.foodtech.automation.screenplay.login.questions.RedirectedToPath;
import com.foodtech.automation.screenplay.login.tasks.LoginWithCredentials;
import com.foodtech.automation.screenplay.support.TestContext;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actors.OnStage;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static org.hamcrest.Matchers.is;

public class LoginStepDefinitions {

    private Actor actor() {
        return OnStage.theActorInTheSpotlight();
    }

    @Given("a registered user with role {word} exists in the system")
    public void aRegisteredUserWithRoleExistsInTheSystem(String role) {
    }

    @When("the user logs in with valid credentials on the authentication page")
    public void theUserLogsInWithValidCredentials() {
        actor().attemptsTo(LoginWithCredentials.usingDataFrom(TestContext.getLoginUser()));
    }

    @Then("the system redirects them to the mesero view")
    public void theSystemRedirectsThemToTheMeseroView() {
        actor().should(seeThat(RedirectedToPath.containing("/mesero"), is(true)));
    }

    @Then("the system redirects them to the barra view")
    public void theSystemRedirectsThemToTheBarraView() {
        actor().should(seeThat(RedirectedToPath.containing("/barra"), is(true)));
    }

    @Then("the system redirects them to the cocina view")
    public void theSystemRedirectsThemToTheCocinaView() {
        actor().should(seeThat(RedirectedToPath.containing("/cocina"), is(true)));
    }

    @Then("navigation controls for other roles are not visible in the navigation bar")
    public void navigationControlsForOtherRolesAreNotVisible() {
        actor().should(seeThat(OtherRoleNavigationIsHidden.inTheNavigationBar(), is(true)));
    }

    @Then("the three board status tabs are visible on the kitchen board")
    public void theThreeBoardStatusTabsAreVisible() {
        actor().should(seeThat(BoardTabsAreVisible.forThePage(), is(true)));
    }
}
