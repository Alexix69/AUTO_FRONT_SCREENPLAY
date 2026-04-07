Feature: Role-Based View Redirection and Isolation Post-Login
  As a registered restaurant staff member
  I want to be redirected to my role-specific view after login
  So that my workspace is focused on my responsibilities

  @redirectMesero
  Scenario: MESERO is redirected to /mesero after successful login
    Given a registered user with role MESERO exists in the system
    When the user logs in with valid credentials on the authentication page
    Then the system redirects them to the mesero view

  @redirectBartender
  Scenario: BARTENDER is redirected to /barra after successful login
    Given a registered user with role BARTENDER exists in the system
    When the user logs in with valid credentials on the authentication page
    Then the system redirects them to the barra view

  @redirectCocinero
  Scenario: COCINERO is redirected to /cocina after successful login
    Given a registered user with role COCINERO exists in the system
    When the user logs in with valid credentials on the authentication page
    Then the system redirects them to the cocina view

  @roleNavIsolation
  Scenario: Navigation links to other roles are not visible after login
    Given a registered user with role MESERO exists in the system
    When the user logs in with valid credentials on the authentication page
    Then navigation controls for other roles are not visible in the navigation bar

  @kitchenInternalNav
  Scenario: COCINERO sees hot kitchen and cold kitchen navigation tabs
    Given a registered user with role COCINERO exists in the system
    When the user logs in with valid credentials on the authentication page
    Then the cocina caliente and cocina fria tabs are visible in the kitchen view
