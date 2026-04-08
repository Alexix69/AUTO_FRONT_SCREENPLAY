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

public class FinishPreparationHooks {

    @Before("@finishButtonVisibleInPrep")
    public void beforeFinishButtonVisibleInPrep() {
        OnStage.setTheStage(new OnlineCast());
        OnStage.theActorCalled("staff member");
        RegistrationData mesero = TestDataFactory.createRegistrationData();
        RegistrationApiClient.registerWithRole(mesero, "MESERO");
        String meseroToken = RegistrationApiClient.loginAndGetToken(mesero.email(), mesero.password());
        String orderId = OrderApiClient.createOrder(meseroToken, 20, List.of(
                OrderApiClient.createOrderItem("Pollo Asado Finish", 1, "HOT_KITCHEN")));
        RegistrationData cocinero = TestDataFactory.createRegistrationData();
        RegistrationApiClient.registerWithRole(cocinero, "COCINERO");
        String cocineroToken = RegistrationApiClient.loginAndGetToken(cocinero.email(), cocinero.password());
        Long taskId = OrderApiClient.getTaskIdForOrder(cocineroToken, "HOT_KITCHEN", orderId);
        OrderApiClient.startTask(cocineroToken, taskId);
        TestContext.setLoginUser(new LoginData(cocinero.email(), cocinero.password()));
    }

    @Before("@finishButtonAbsentInOtherTabs")
    public void beforeFinishButtonAbsentInOtherTabs() {
        OnStage.setTheStage(new OnlineCast());
        OnStage.theActorCalled("staff member");
        RegistrationData mesero = TestDataFactory.createRegistrationData();
        RegistrationApiClient.registerWithRole(mesero, "MESERO");
        String meseroToken = RegistrationApiClient.loginAndGetToken(mesero.email(), mesero.password());
        OrderApiClient.createOrder(meseroToken, 21, List.of(
                OrderApiClient.createOrderItem("Caldo Absent A", 1, "HOT_KITCHEN")));
        String orderId2 = OrderApiClient.createOrder(meseroToken, 22, List.of(
                OrderApiClient.createOrderItem("Caldo Absent B", 1, "HOT_KITCHEN")));
        RegistrationData cocinero = TestDataFactory.createRegistrationData();
        RegistrationApiClient.registerWithRole(cocinero, "COCINERO");
        String cocineroToken = RegistrationApiClient.loginAndGetToken(cocinero.email(), cocinero.password());
        Long taskId2 = OrderApiClient.getTaskIdForOrder(cocineroToken, "HOT_KITCHEN", orderId2);
        OrderApiClient.startTask(cocineroToken, taskId2);
        OrderApiClient.completeTask(cocineroToken, taskId2);
        TestContext.setLoginUser(new LoginData(cocinero.email(), cocinero.password()));
    }

    @Before("@cocineroFinishesPreparation")
    public void beforeCocineroFinishesPreparation() {
        OnStage.setTheStage(new OnlineCast());
        OnStage.theActorCalled("staff member");
        RegistrationData mesero = TestDataFactory.createRegistrationData();
        RegistrationApiClient.registerWithRole(mesero, "MESERO");
        String meseroToken = RegistrationApiClient.loginAndGetToken(mesero.email(), mesero.password());
        String orderId = OrderApiClient.createOrder(meseroToken, 23, List.of(
                OrderApiClient.createOrderItem("Arroz Finish", 1, "HOT_KITCHEN")));
        RegistrationData cocinero = TestDataFactory.createRegistrationData();
        RegistrationApiClient.registerWithRole(cocinero, "COCINERO");
        String cocineroToken = RegistrationApiClient.loginAndGetToken(cocinero.email(), cocinero.password());
        Long taskId = OrderApiClient.getTaskIdForOrder(cocineroToken, "HOT_KITCHEN", orderId);
        OrderApiClient.startTask(cocineroToken, taskId);
        TestContext.setTaskId(taskId);
        TestContext.setOperatorToken(cocineroToken);
        TestContext.setLoginUser(new LoginData(cocinero.email(), cocinero.password()));
    }

    @Before("@finishErrorBannerVisible")
    public void beforeFinishErrorBannerVisible() {
        OnStage.setTheStage(new OnlineCast());
        OnStage.theActorCalled("staff member");
        RegistrationData mesero = TestDataFactory.createRegistrationData();
        RegistrationApiClient.registerWithRole(mesero, "MESERO");
        String meseroToken = RegistrationApiClient.loginAndGetToken(mesero.email(), mesero.password());
        String orderId = OrderApiClient.createOrder(meseroToken, 24, List.of(
                OrderApiClient.createOrderItem("Sopa Error Banner", 1, "HOT_KITCHEN")));
        RegistrationData cocinero = TestDataFactory.createRegistrationData();
        RegistrationApiClient.registerWithRole(cocinero, "COCINERO");
        String cocineroToken = RegistrationApiClient.loginAndGetToken(cocinero.email(), cocinero.password());
        Long taskId = OrderApiClient.getTaskIdForOrder(cocineroToken, "HOT_KITCHEN", orderId);
        OrderApiClient.startTask(cocineroToken, taskId);
        TestContext.setTaskId(taskId);
        TestContext.setOperatorToken(cocineroToken);
        TestContext.setLoginUser(new LoginData(cocinero.email(), cocinero.password()));
    }

    @After("@finishButtonVisibleInPrep or @finishButtonAbsentInOtherTabs or @cocineroFinishesPreparation or @finishErrorBannerVisible")
    public void afterFinishPreparationScenario() {
        TestContext.clear();
        OnStage.drawTheCurtain();
    }
}
