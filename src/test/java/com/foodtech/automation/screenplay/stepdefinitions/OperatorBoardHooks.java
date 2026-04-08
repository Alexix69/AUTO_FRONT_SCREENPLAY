package com.foodtech.automation.screenplay.stepdefinitions;

import com.foodtech.automation.screenplay.support.DatabaseCleaner;
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
import java.util.concurrent.TimeUnit;

public class OperatorBoardHooks {

    private static final int FIFO_TIMESTAMP_GAP_MS = 150;

    @Before("@taskBoardTabsCocinero")
    public void beforeTaskBoardTabsCocinero() {
        OnStage.setTheStage(new OnlineCast());
        OnStage.theActorCalled("staff member");
        RegistrationData mesero = TestDataFactory.createRegistrationData();
        RegistrationApiClient.registerWithRole(mesero, "MESERO");
        String token = RegistrationApiClient.loginAndGetToken(mesero.email(), mesero.password());
        OrderApiClient.createOrder(token, 1, List.of(
                OrderApiClient.createOrderItem("Sopa de Pollo", 1, "HOT_KITCHEN")));
        RegistrationData operator = TestDataFactory.createRegistrationData();
        RegistrationApiClient.registerWithRole(operator, "COCINERO");
        TestContext.setLoginUser(new LoginData(operator.email(), operator.password()));
        TestContext.setExpectedStation("HOT_KITCHEN");
    }

    @Before("@taskBoardTabsBartender")
    public void beforeTaskBoardTabsBartender() {
        OnStage.setTheStage(new OnlineCast());
        OnStage.theActorCalled("staff member");
        RegistrationData mesero = TestDataFactory.createRegistrationData();
        RegistrationApiClient.registerWithRole(mesero, "MESERO");
        String token = RegistrationApiClient.loginAndGetToken(mesero.email(), mesero.password());
        OrderApiClient.createOrder(token, 2, List.of(
                OrderApiClient.createOrderItem("Mojito", 1, "BAR")));
        RegistrationData operator = TestDataFactory.createRegistrationData();
        RegistrationApiClient.registerWithRole(operator, "BARTENDER");
        TestContext.setLoginUser(new LoginData(operator.email(), operator.password()));
        TestContext.setExpectedStation("BAR");
    }

    @Before("@taskBoardFilterCocinero")
    public void beforeTaskBoardFilterCocinero() {
        OnStage.setTheStage(new OnlineCast());
        OnStage.theActorCalled("staff member");
        RegistrationData mesero = TestDataFactory.createRegistrationData();
        RegistrationApiClient.registerWithRole(mesero, "MESERO");
        String token = RegistrationApiClient.loginAndGetToken(mesero.email(), mesero.password());
        OrderApiClient.createOrder(token, 3, List.of(
                OrderApiClient.createOrderItem("Arroz con Pollo", 1, "HOT_KITCHEN")));
        OrderApiClient.createOrder(token, 4, List.of(
                OrderApiClient.createOrderItem("Agua Mineral", 1, "BAR")));
        RegistrationData operator = TestDataFactory.createRegistrationData();
        RegistrationApiClient.registerWithRole(operator, "COCINERO");
        TestContext.setLoginUser(new LoginData(operator.email(), operator.password()));
        TestContext.setExpectedStation("HOT_KITCHEN,COLD_KITCHEN");
    }

    @Before("@taskBoardAggregation")
    public void beforeTaskBoardAggregation() {
        OnStage.setTheStage(new OnlineCast());
        OnStage.theActorCalled("staff member");
        DatabaseCleaner.completeAllActiveKitchenTasks();
        RegistrationData mesero = TestDataFactory.createRegistrationData();
        RegistrationApiClient.registerWithRole(mesero, "MESERO");
        String token = RegistrationApiClient.loginAndGetToken(mesero.email(), mesero.password());
        OrderApiClient.createOrder(token, 5, List.of(
                OrderApiClient.createOrderItem("Sopa Test", 3, "HOT_KITCHEN")));
        RegistrationData operator = TestDataFactory.createRegistrationData();
        RegistrationApiClient.registerWithRole(operator, "COCINERO");
        TestContext.setLoginUser(new LoginData(operator.email(), operator.password()));
        TestContext.setExpectedStation("HOT_KITCHEN,COLD_KITCHEN");
    }

    @Before("@taskBoardCardFields")
    public void beforeTaskBoardCardFields() {
        OnStage.setTheStage(new OnlineCast());
        OnStage.theActorCalled("staff member");
        RegistrationData mesero = TestDataFactory.createRegistrationData();
        RegistrationApiClient.registerWithRole(mesero, "MESERO");
        String token = RegistrationApiClient.loginAndGetToken(mesero.email(), mesero.password());
        OrderApiClient.createOrder(token, 6, List.of(
                OrderApiClient.createOrderItem("Ensalada Caesar", 1, "COLD_KITCHEN")));
        RegistrationData operator = TestDataFactory.createRegistrationData();
        RegistrationApiClient.registerWithRole(operator, "COCINERO");
        TestContext.setLoginUser(new LoginData(operator.email(), operator.password()));
        TestContext.setExpectedStation("COLD_KITCHEN");
    }

    @Before("@taskBoardFifo")
    public void beforeTaskBoardFifo() {
        OnStage.setTheStage(new OnlineCast());
        OnStage.theActorCalled("staff member");
        RegistrationData mesero = TestDataFactory.createRegistrationData();
        RegistrationApiClient.registerWithRole(mesero, "MESERO");
        String token = RegistrationApiClient.loginAndGetToken(mesero.email(), mesero.password());
        OrderApiClient.createOrder(token, 7, List.of(
                OrderApiClient.createOrderItem("Mojito Primero", 1, "BAR")));
        try { TimeUnit.MILLISECONDS.sleep(FIFO_TIMESTAMP_GAP_MS); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        OrderApiClient.createOrder(token, 8, List.of(
                OrderApiClient.createOrderItem("Mojito Segundo", 1, "BAR")));
        try { TimeUnit.MILLISECONDS.sleep(FIFO_TIMESTAMP_GAP_MS); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        OrderApiClient.createOrder(token, 9, List.of(
                OrderApiClient.createOrderItem("Mojito Tercero", 1, "BAR")));
        RegistrationData operator = TestDataFactory.createRegistrationData();
        RegistrationApiClient.registerWithRole(operator, "BARTENDER");
        TestContext.setLoginUser(new LoginData(operator.email(), operator.password()));
        TestContext.setExpectedStation("BAR");
    }

    @Before("@taskBoardEmptyState")
    public void beforeTaskBoardEmptyState() {
        OnStage.setTheStage(new OnlineCast());
        OnStage.theActorCalled("staff member");
        DatabaseCleaner.completeAllActiveKitchenTasks();
        RegistrationData operator = TestDataFactory.createRegistrationData();
        RegistrationApiClient.registerWithRole(operator, "COCINERO");
        TestContext.setLoginUser(new LoginData(operator.email(), operator.password()));
        TestContext.setExpectedStation("HOT_KITCHEN");
    }

    @After("@taskBoardTabsCocinero or @taskBoardTabsBartender or @taskBoardFilterCocinero or @taskBoardAggregation or @taskBoardCardFields or @taskBoardFifo or @taskBoardEmptyState")
    public void afterOperatorBoardScenario() {
        TestContext.clear();
        OnStage.drawTheCurtain();
    }
}
