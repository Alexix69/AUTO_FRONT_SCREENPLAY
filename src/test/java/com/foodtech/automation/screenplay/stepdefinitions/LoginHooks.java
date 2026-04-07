package com.foodtech.automation.screenplay.stepdefinitions;

import com.foodtech.automation.screenplay.support.LoginData;
import com.foodtech.automation.screenplay.support.RegistrationApiClient;
import com.foodtech.automation.screenplay.support.RegistrationData;
import com.foodtech.automation.screenplay.support.TestContext;
import com.foodtech.automation.screenplay.support.TestDataFactory;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import net.serenitybdd.screenplay.actors.OnlineCast;
import net.serenitybdd.screenplay.actors.OnStage;

public class LoginHooks {

    @Before("@redirectMesero")
    public void beforeRedirectMesero() {
        OnStage.setTheStage(new OnlineCast());
        OnStage.theActorCalled("staff member");
        RegistrationData data = TestDataFactory.createRegistrationData();
        RegistrationApiClient.registerWithRole(data, "MESERO");
        TestContext.setLoginUser(new LoginData(data.email(), data.password()));
    }

    @Before("@redirectBartender")
    public void beforeRedirectBartender() {
        OnStage.setTheStage(new OnlineCast());
        OnStage.theActorCalled("staff member");
        RegistrationData data = TestDataFactory.createRegistrationData();
        RegistrationApiClient.registerWithRole(data, "BARTENDER");
        TestContext.setLoginUser(new LoginData(data.email(), data.password()));
    }

    @Before("@redirectCocinero")
    public void beforeRedirectCocinero() {
        OnStage.setTheStage(new OnlineCast());
        OnStage.theActorCalled("staff member");
        RegistrationData data = TestDataFactory.createRegistrationData();
        RegistrationApiClient.registerWithRole(data, "COCINERO");
        TestContext.setLoginUser(new LoginData(data.email(), data.password()));
    }

    @Before("@roleNavIsolation")
    public void beforeRoleNavIsolation() {
        OnStage.setTheStage(new OnlineCast());
        OnStage.theActorCalled("staff member");
        RegistrationData data = TestDataFactory.createRegistrationData();
        RegistrationApiClient.registerWithRole(data, "MESERO");
        TestContext.setLoginUser(new LoginData(data.email(), data.password()));
    }

    @Before("@kitchenInternalNav")
    public void beforeKitchenInternalNav() {
        OnStage.setTheStage(new OnlineCast());
        OnStage.theActorCalled("staff member");
        RegistrationData data = TestDataFactory.createRegistrationData();
        RegistrationApiClient.registerWithRole(data, "COCINERO");
        TestContext.setLoginUser(new LoginData(data.email(), data.password()));
    }

    @After("@redirectMesero or @redirectBartender or @redirectCocinero or @roleNavIsolation or @kitchenInternalNav")
    public void afterLoginScenario() {
        TestContext.clear();
        OnStage.drawTheCurtain();
    }
}
