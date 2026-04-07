@capability:UI
@feature:OperatorStartPreparation
Feature: Operator Task Board — Iniciar Preparación de Tarea
  As a COCINERO or BARTENDER operator
  I want to mark the start of preparation for a pending task
  So that the board reflects my active workload in real time

  @startTaskButtonVisible
  Scenario: Start preparation button is visible on each pending task card
    Given a COCINERO operator has a pending kitchen task on the board
    When the operator navigates to the board and views pending tasks
    Then each task card shows the start preparation button

  @cocineroStartsPreparation
  Scenario: COCINERO initiates preparation — task moves to En Preparación
    Given a COCINERO operator has a pending kitchen task on the board
    When the operator navigates to the board and views pending tasks
    And the operator clicks the start preparation button
    Then the task moves to the in preparation section

  @bartenderStartsPreparation
  Scenario: BARTENDER initiates preparation — task moves to En Preparación
    Given a BARTENDER operator has a pending bar task on the board
    When the operator navigates to the board and views pending tasks
    And the operator clicks the start preparation button
    Then the task moves to the in preparation section

  @apiStartErrorModalVisible
  Scenario: API failure on start — task remains in Pendientes and error banner is shown
    Given a BARTENDER operator has a pending bar task and the API will fail on start
    When the operator navigates to the board and views pending tasks
    And the operator pre-starts the task via API then clicks the start button
    Then the task remains in pending and an error banner appears
