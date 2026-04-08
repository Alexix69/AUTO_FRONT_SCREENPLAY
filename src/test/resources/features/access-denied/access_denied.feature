@capability:UI
@feature:AccessDenied
Feature: Role-Based Route Protection - Access Denied Page
  As a logged-in staff member
  I want the system to block access to routes outside my role
  So that I cannot access views that are not meant for my responsibilities

  @accessDeniedMesero
  Scenario: TC-OP1-4.1 MESERO is blocked from reaching the kitchen route
    Given a staff member is authenticated and positioned on their role home view
    When the user navigates directly to "/cocina"
    Then the access denied page is displayed

  @accessDeniedCocinero
  Scenario: COCINERO is blocked from reaching the mesero route
    Given a staff member is authenticated and positioned on their role home view
    When the user navigates directly to "/mesero"
    Then the access denied page is displayed

  @accessDeniedBackButton
  Scenario: Staff member returns to role home after seeing the access denied page
    Given a staff member is authenticated and positioned on their role home view
    When the user clicks the back button
    Then the user is redirected to their role home route
