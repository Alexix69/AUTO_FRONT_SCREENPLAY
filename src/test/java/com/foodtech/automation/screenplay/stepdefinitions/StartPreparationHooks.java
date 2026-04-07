package com.foodtech.automation.screenplay.stepdefinitions;

import com.foodtech.automation.screenplay.support.LoginData;
import com.foodtech.automation.screenplay.support.OrderApiClient;
import com.foodtech.automation.screenplay.support.RegistrationApiClient;
import com.foodtech.automation.screenplay.support.RegistrationData;
import com.foodtech.automation.screenplay.support.TestContext;
import com.foodtech.automation.screenplay.support.TestDataFactory;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import net.serenitybdd.screenplay.actors.OnlineCast;
import net.serenitybdd.screenplay.actors.OnStage;

import java.util.List;

public class StartPreparationHooks {

    @Before("@startTaskButtonVisible")
    public void beforeStartTaskButtonVisible() {
        OnStage.setTheStage(new OnlineCast());
        OnStage.theActorCalled("staff member");
        RegistrationData mesero = TestDataFactory.createRegistrationData();
        RegistrationApiClient.registerWithRole(mesero, "MESERO");
        String meseroToken = RegistrationApiClient.loginAndGetToken(mesero.email(), mesero.password());
        OrderApiClient.createOrder(meseroToken, 10, List.of(
                OrderApiClient.createOrderItem("Paella", 1, "HOT_KITCHEN")));
        RegistrationData cocinero = TestDataFactory.createRegistrationData();
        RegistrationApiClient.registerWithRole(cocinero, "COCINERO");
        TestContext.setLoginUser(new LoginData(cocinero.email(), cocinero.password()));
    }

    @Before("@cocineroStartsPreparation")
    public void beforeCocineroStartsPreparation() {
        OnStage.setTheStage(new OnlineCast());
        OnStage.theActorCalled("staff member");
        RegistrationData mesero = TestDataFactory.createRegistrationData();
        RegistrationApiClient.registerWithRole(mesero, "MESERO");
        String meseroToken = RegistrationApiClient.loginAndGetToken(mesero.email(), mesero.password());
        String orderId = OrderApiClient.createOrder(meseroToken, 11, List.of(
                OrderApiClient.createOrderItem("Paella Cocinero", 1, "HOT_KITCHEN")));
        RegistrationData cocinero = TestDataFactory.createRegistrationData();
        RegistrationApiClient.registerWithRole(cocinero, "COCINERO");
        String cocineroToken = RegistrationApiClient.loginAndGetToken(cocinero.email(), cocinero.password());
        TestContext.setOperatorToken(cocineroToken);
        Long taskId = OrderApiClient.getTaskIdForOrder(cocineroToken, "HOT_KITCHEN", orderId);
        TestContext.setTaskId(taskId);
        TestContext.setLoginUser(new LoginData(cocinero.email(), cocinero.password()));
    }

    @Before("@bartenderStartsPreparation")
    public void beforeBartenderStartsPreparation() {
        OnStage.setTheStage(new OnlineCast());
        OnStage.theActorCalled("staff member");
        RegistrationData mesero = TestDataFactory.createRegistrationData();
        RegistrationApiClient.registerWithRole(mesero, "MESERO");
        String meseroToken = RegistrationApiClient.loginAndGetToken(mesero.email(), mesero.password());
        String orderId = OrderApiClient.createOrder(meseroToken, 12, List.of(
                OrderApiClient.createOrderItem("Mojito Bartender", 1, "BAR")));
        RegistrationData bartender = TestDataFactory.createRegistrationData();
        RegistrationApiClient.registerWithRole(bartender, "BARTENDER");
        String bartenderToken = RegistrationApiClient.loginAndGetToken(bartender.email(), bartender.password());
        TestContext.setOperatorToken(bartenderToken);
        Long taskId = OrderApiClient.getTaskIdForOrder(bartenderToken, "BAR", orderId);
        TestContext.setTaskId(taskId);
        TestContext.setLoginUser(new LoginData(bartender.email(), bartender.password()));
    }

    @Before("@apiStartErrorModalVisible")
    public void beforeApiStartErrorModalVisible() {
        OnStage.setTheStage(new OnlineCast());
        OnStage.theActorCalled("staff member");
        RegistrationData mesero = TestDataFactory.createRegistrationData();
        RegistrationApiClient.registerWithRole(mesero, "MESERO");
        String meseroToken = RegistrationApiClient.loginAndGetToken(mesero.email(), mesero.password());
        String orderId = OrderApiClient.createOrder(meseroToken, 13, List.of(
                OrderApiClient.createOrderItem("Daiquiri Error", 1, "BAR")));
        RegistrationData bartender = TestDataFactory.createRegistrationData();
        RegistrationApiClient.registerWithRole(bartender, "BARTENDER");
        String bartenderToken = RegistrationApiClient.loginAndGetToken(bartender.email(), bartender.password());
        Long taskId = OrderApiClient.getTaskIdForOrder(bartenderToken, "BAR", orderId);
        TestContext.setTaskId(taskId);
        TestContext.setOperatorToken(bartenderToken);
        TestContext.setLoginUser(new LoginData(bartender.email(), bartender.password()));
    }

    @After("@startTaskButtonVisible or @cocineroStartsPreparation or @bartenderStartsPreparation or @apiStartErrorModalVisible")
    public void afterStartPreparationScenario() {
        TestContext.clear();
        OnStage.drawTheCurtain();
    }
}
