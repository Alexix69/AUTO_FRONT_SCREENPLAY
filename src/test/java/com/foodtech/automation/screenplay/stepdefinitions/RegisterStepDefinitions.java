package com.foodtech.automation.screenplay.stepdefinitions;

import com.foodtech.automation.screenplay.register.questions.CurrentUrl;
import com.foodtech.automation.screenplay.register.questions.VisibleErrorMessage;
import com.foodtech.automation.screenplay.register.tasks.ActivateRegistrationMode;
import com.foodtech.automation.screenplay.register.tasks.NavigateToAuthenticationPage;
import com.foodtech.automation.screenplay.register.tasks.ProvideRegistrationData;
import com.foodtech.automation.screenplay.register.tasks.SubmitRegistration;
import com.foodtech.automation.screenplay.support.TestContext;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actors.OnStage;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class RegisterStepDefinitions {

    private Actor actor() {
        return OnStage.theActorInTheSpotlight();
    }

    @Given("the user is on the authentication page with demo mode off")
    public void theUserIsOnTheAuthenticationPageWithDemoModeOff() {
        actor().attemptsTo(NavigateToAuthenticationPage.now());
    }

    @And("the user activates the registration mode")
    public void theUserActivatesTheRegistrationMode() {
        actor().attemptsTo(ActivateRegistrationMode.now());
    }

    @When("the user provides a valid email, a unique username, and a valid password")
    public void theUserProvidesValidRegistrationData() {
        actor().attemptsTo(ProvideRegistrationData.with(TestContext.getUser()));
    }

    @When("the user provides an email already registered with a different password")
    public void theUserProvidesConflictingRegistrationData() {
        actor().attemptsTo(ProvideRegistrationData.with(TestContext.getConflictingUser()));
    }

    @And("the user submits the registration form")
    public void theUserSubmitsTheRegistrationForm() {
        actor().attemptsTo(SubmitRegistration.now());
    }

    @Then("the system creates the account and starts the session automatically")
    public void theSystemCreatesTheAccountAndStartsTheSessionAutomatically() {
        actor().should(seeThat(CurrentUrl.forThePage(), containsString("/mesero")));
    }

    @And("the user is taken to the main operational view")
    public void theUserIsTakenToTheMainOperationalView() {
        actor().should(seeThat(CurrentUrl.forThePage(), containsString("/mesero")));
    }

    @Then("the system displays an invalid credentials message")
    public void theSystemDisplaysAnInvalidCredentialsMessage() {
        actor().should(seeThat(VisibleErrorMessage.forThePage(), equalTo("Credenciales inválidas")));
    }

    @And("the user remains on the authentication page")
    public void theUserRemainsOnTheAuthenticationPage() {
        actor().should(seeThat(CurrentUrl.forThePage(), containsString("/login")));
    }
}
