package com.foodtech.automation.screenplay.stepdefinitions;

import com.foodtech.automation.screenplay.support.RegistrationApiClient;
import com.foodtech.automation.screenplay.support.RegistrationData;
import com.foodtech.automation.screenplay.support.TestContext;
import com.foodtech.automation.screenplay.support.TestDataFactory;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import net.serenitybdd.screenplay.actors.OnlineCast;
import net.serenitybdd.screenplay.actors.OnStage;

public class RegisterHooks {

    @Before("@positiveRegister")
    public void beforePositiveRegister() {
        OnStage.setTheStage(new OnlineCast());
        OnStage.theActorCalled("User");
        RegistrationData data = TestDataFactory.createRegistrationData();
        TestContext.setUser(data);
    }

    @After("@positiveRegister")
    public void afterPositiveRegister() {
        TestContext.clear();
        OnStage.drawTheCurtain();
    }

    @Before("@negativeRegister")
    public void beforeNegativeRegister() {
        OnStage.setTheStage(new OnlineCast());
        OnStage.theActorCalled("User");
        RegistrationData dataA = TestDataFactory.createRegistrationData();
        RegistrationApiClient.register(dataA);
        RegistrationData conflicting = new RegistrationData(
                dataA.email(),
                dataA.username(),
                TestDataFactory.generatePassword("B")
        );
        TestContext.setConflictingUser(conflicting);
    }

    @After("@negativeRegister")
    public void afterNegativeRegister() {
        TestContext.clear();
        OnStage.drawTheCurtain();
    }
}
