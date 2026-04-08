@capability:UI
@feature:UserRegistration
Feature: User Registration
  As a new restaurant staff member
  I want to create an account from the authentication page
  So that I can access the system and begin managing orders

  @positiveRegister
  Scenario: A new user registers and the system starts their session automatically
    Given the user is on the authentication page with demo mode off
    And the user activates the registration mode
    When the user provides a valid email, a unique username, and a valid password
    And the user submits the registration form
    Then the system creates the account and starts the session automatically
    And the user is taken to the main operational view

  @negativeRegister
  Scenario: Registration with an already registered email causes an automatic login failure
    Given the user is on the authentication page with demo mode off
    And the user activates the registration mode
    When the user provides an email already registered with a different password
    And the user submits the registration form
    Then the system displays an invalid credentials message
    And the user remains on the authentication page
