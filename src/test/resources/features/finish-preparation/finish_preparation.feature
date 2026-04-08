@capability:UI
@feature:OperatorFinishPreparation
Feature: Operator Task Board — Completar Preparación de Tarea
  As a COCINERO or BARTENDER operator
  I want to mark the completion of preparation for an active task
  So that the board reflects my completed workload in real time

  @finishButtonVisibleInPrep
  Scenario: Completar button is visible and enabled only on En Preparacion task cards
    Given a COCINERO operator has a task in IN_PREPARATION state on the kitchen board
    When the operator navigates to the board and views the in preparation section
    Then each task card in En Preparacion displays an enabled Completar button

  @finishButtonAbsentInOtherTabs
  Scenario: Completar button is absent on Pendientes and Completadas task cards
    Given a COCINERO operator is authenticated and the board has tasks in Pendientes and Completadas
    When the operator navigates to the board and views pending tasks
    Then no task card in Pendientes or Completadas displays the Completar button

  @cocineroFinishesPreparation
  Scenario: COCINERO finalizes preparation — task moves to Completadas
    Given a COCINERO operator has a task in IN_PREPARATION state on the kitchen board
    When the operator navigates to the board and views the in preparation section
    And the operator clicks the finish preparation button
    Then the task moves to the completed section

  @finishErrorBannerVisible
  Scenario: API failure on complete — task remains in En Preparacion and error banner is shown
    Given a COCINERO operator has a task in En Preparacion and the API will fail on complete
    When the operator navigates to the board and views the in preparation section
    And the operator pre-completes the task via API then clicks the finish button
    Then the task remains in En Preparacion and an error banner is displayed
