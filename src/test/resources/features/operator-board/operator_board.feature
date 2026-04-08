@capability:UI
@feature:OperatorTaskBoard
Feature: Operator Task Board — Role-Based Task Management by Status
  As a kitchen or bar operator
  I want to see only my station's tasks organized by status
  So that I can manage my workload efficiently

  @taskBoardTabsCocinero
  Scenario: COCINERO sees the three status tabs on the task board
    Given a COCINERO operator is authenticated with pending kitchen orders
    When the operator navigates to their board
    Then the board displays the three operator tabs
    And the Todas las tareas tab is not visible

  @taskBoardTabsBartender
  Scenario: BARTENDER sees the three status tabs on the task board
    Given a BARTENDER operator is authenticated with pending bar orders
    When the operator navigates to their board
    Then the board displays the three operator tabs
    And the Todas las tareas tab is not visible

  @taskBoardFilterCocinero
  Scenario: COCINERO sees only kitchen tasks filtered by their station
    Given a COCINERO operator is authenticated with mixed station orders
    When the operator navigates to their board
    Then only tasks for their station are displayed

  @taskBoardAggregation
  Scenario: Three identical products from one order produce a single card with quantity three
    Given a COCINERO operator is authenticated with an aggregated kitchen order
    When the operator navigates to their board
    Then the task is shown as a single card with quantity 3

  @taskBoardCardFields
  Scenario: Each task card shows the required fields
    Given a COCINERO operator is authenticated with a kitchen order
    When the operator navigates to their board
    Then each task card shows the required fields

  @taskBoardFifo
  Scenario: Tasks are shown in FIFO order in the pending tab
    Given a BARTENDER operator is authenticated with three sequential bar orders
    When the operator navigates to their board
    Then tasks are displayed in FIFO order

  @taskBoardEmptyState
  Scenario: An empty state message is shown when no tasks exist for a tab
    Given a COCINERO operator is authenticated with no pending orders
    And the operator navigates to their board
    When the operator selects the "En Preparación" tab
    Then an empty state message is displayed
