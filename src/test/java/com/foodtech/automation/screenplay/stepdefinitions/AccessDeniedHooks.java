package com.foodtech.automation.screenplay.stepdefinitions;

import com.foodtech.automation.screenplay.login.tasks.LoginWithCredentials;
import com.foodtech.automation.screenplay.login.tasks.NavigateToUnauthorizedRoute;
import com.foodtech.automation.screenplay.support.LoginData;
import com.foodtech.automation.screenplay.support.RegistrationApiClient;
import com.foodtech.automation.screenplay.support.RegistrationData;
import com.foodtech.automation.screenplay.support.TestContext;
import com.foodtech.automation.screenplay.support.TestDataFactory;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import net.serenitybdd.screenplay.actors.OnlineCast;
import net.serenitybdd.screenplay.actors.OnStage;

public class AccessDeniedHooks {

    @Before("@accessDeniedMesero")
    public void beforeAccessDeniedMesero() {
        OnStage.setTheStage(new OnlineCast());
        OnStage.theActorCalled("staff member");
        RegistrationData data = TestDataFactory.createRegistrationData();
        RegistrationApiClient.registerWithRole(data, "MESERO");
        TestContext.setLoginUser(new LoginData(data.email(), data.password()));
        OnStage.theActorInTheSpotlight().attemptsTo(
                LoginWithCredentials.usingDataFrom(TestContext.getLoginUser())
        );
    }

    @Before("@accessDeniedCocinero")
    public void beforeAccessDeniedCocinero() {
        OnStage.setTheStage(new OnlineCast());
        OnStage.theActorCalled("staff member");
        RegistrationData data = TestDataFactory.createRegistrationData();
        RegistrationApiClient.registerWithRole(data, "COCINERO");
        TestContext.setLoginUser(new LoginData(data.email(), data.password()));
        OnStage.theActorInTheSpotlight().attemptsTo(
                LoginWithCredentials.usingDataFrom(TestContext.getLoginUser())
        );
    }

    @Before("@accessDeniedBackButton")
    public void beforeAccessDeniedBackButton() {
        OnStage.setTheStage(new OnlineCast());
        OnStage.theActorCalled("staff member");
        RegistrationData data = TestDataFactory.createRegistrationData();
        RegistrationApiClient.registerWithRole(data, "BARTENDER");
        TestContext.setLoginUser(new LoginData(data.email(), data.password()));
        OnStage.theActorInTheSpotlight().attemptsTo(
                LoginWithCredentials.usingDataFrom(TestContext.getLoginUser()),
                NavigateToUnauthorizedRoute.at("/mesero")
        );
    }

    @After("@accessDeniedMesero or @accessDeniedCocinero or @accessDeniedBackButton")
    public void afterAccessDeniedScenario() {
        TestContext.clear();
        OnStage.drawTheCurtain();
    }
}
