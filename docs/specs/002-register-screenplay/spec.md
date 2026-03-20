# Feature Specification: User Registration with Automatic Session Start

**Feature Branch**: `002-register-screenplay`
**Created**: 2026-03-19
**Status**: Draft
**Input**: User description: "User Registration with Automatic Session Start - Screenplay-based automation for FoodTech authentication flows"

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Successful Registration and Automatic Login (Priority: P1)

As a new restaurant staff member, I want to create an account from the authentication page so that the system automatically starts my session and takes me to the main operational view without any extra steps.

A new user who has no existing account navigates to the authentication page, activates the registration mode available within that same page, provides a valid email address, a unique username, and a valid password, and submits the registration form. The system completes the account creation, immediately authenticates the user with the supplied credentials, and navigates the user to the main operational view. The user does not need to log in separately after registering. Demo mode is off.

**Why this priority**: This is the foundational happy path for new users. Without a working registration-plus-automatic-login flow, no new staff member can access the system. It validates the full onboarding contract from account creation to first access in a single atomic flow.

**Independent Test**: Can be fully tested by preparing a unique set of credentials not yet registered in the system, performing the registration flow from the authentication page, and verifying that the main operational view becomes accessible. Delivers confirmation that new users can independently reach the application without manual intervention.

**Acceptance Scenarios** *(Automation Lab Constitution §4 — Gherkin rules apply)*:

> ✅ Express **observable outcomes**, not UI mechanics.
> ✅ Given = system context | When = user action | Then = verifiable result.
> ❌ Never reference button names, CSS selectors, or HTTP methods directly.

1. **Given** the user is on the authentication page with demo mode off, **When** the user activates registration mode and provides a valid email, a unique username, and a valid password, and submits the registration form, **Then** the system creates the account, starts the session automatically, and takes the user to the main operational view

---

### User Story 2 - Failed Automatic Login After Registration Attempt (Priority: P1)

As a system operator, I want the authentication page to display a clear error message when a registration attempt cannot result in a valid session so that the staff member is not left in an undefined state and can take corrective action.

A user attempts to register with an email that is already associated with an existing account but provides a different password. The system processes the registration form submission, the automatic login step that follows cannot succeed because the supplied credentials do not match the stored account, and an invalid credentials message is displayed within the page. The user remains on the authentication page. Demo mode is off.

**Why this priority**: Equally critical to the happy path. When registration appears to succeed but automatic login fails, the user must receive clear feedback. Without this, the user could be left in a blank state with no indication of what went wrong, breaking the onboarding contract.

**Independent Test**: Can be fully tested by pre-registering an account via API before the test begins, then providing that same email with a different password in the registration form. After submitting, the test verifies that an error message is visible on the page and the user has not been navigated away from the authentication page.

**Acceptance Scenarios** *(Constitution §4)*:

1. **Given** the user is on the authentication page with demo mode off, **When** the user activates registration mode and provides an email already registered with a different password, and submits the registration form, **Then** the system displays an invalid credentials message and the user remains on the authentication page

---

### Edge Cases

- What happens if demo mode is active during a registration attempt? Demo mode bypasses both the registration and the automatic login calls entirely, redirecting directly to the main operational view without creating an account. This path does not exercise the registration flow. Demo mode must be confirmed off before any registration scenario executes.

## Gherkin Feature File *(mandatory for automation specs)*

```gherkin
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
```

### Gherkin Compliance Notes

| Rule | Status |
|---|---|
| Given/When/Then used properly | ✅ |
| No implementation details in steps | ✅ |
| Business-readable language | ✅ |
| Steps are atomic and declarative | ✅ |
| No coupling between scenarios | ✅ |
| Each scenario independently executable | ✅ |
| Constitution §4 compliant | ✅ |
| Suitable for Screenplay implementation | ✅ |
| Supports Serenity reporting | ✅ |
| Negative scenario uses deterministic condition | ✅ |
| Tags present for hook-based data setup | ✅ |

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: The system MUST present registration mode as an accessible state within the authentication page — there is no dedicated registration URL
- **FR-002**: The system MUST require an email address, a username, and a password to complete a registration submission
- **FR-003**: Upon a registration form submission with valid credentials for a new account, the system MUST automatically authenticate the user with those same credentials without requiring a separate login action
- **FR-004**: Upon successful registration and automatic login, the system MUST navigate the user to the main operational view
- **FR-005**: When the automatic login step that follows registration fails, the system MUST display a visible error message within the page content — the message must appear in the DOM, not as a browser alert
- **FR-006**: When automatic login fails after a registration attempt, the system MUST keep the user on the authentication page without navigating away
- **FR-007**: Each test scenario MUST be deterministic — the same inputs always produce the same observable outputs
- **FR-008**: Each test scenario MUST be independently executable without depending on the execution order or outcome of any other scenario
- **FR-009**: Test data setup for pre-existing accounts required by negative scenarios MUST be performed outside the UI via direct backend call before the test begins
- **FR-010**: Demo mode MUST be confirmed inactive before any registration scenario executes

### Key Entities

- **Registration Credentials**: The set of data submitted by a new user — email address, username, and password. Accepted by the system without client-side format or strength enforcement.
- **Authentication Session**: The active state established after successful automatic login. Stored as a token in local browser storage. Required for access to all protected views.
- **Authentication Page**: The single page at the `/login` route that serves both login and registration mode. Mode is toggled by the user within the page; no URL change occurs during the toggle.

## Assumptions

- A pre-existing account with a known email and a known password is available for the negative scenario. This account is created via direct backend call outside the UI before the test run, not through the UI.
- The backend is reachable and operational during test execution. Network failure scenarios are out of scope for this feature slice.
- Unique email addresses used in the positive scenario are generated fresh per test run to guarantee they are not already registered in the system.
- Demo mode is always inactive at the start of each test. The precondition "with demo mode off" in each Gherkin step is the automation signal to verify this state.
- The registration response HTTP status from the backend is not evaluated by the frontend. The observable outcome is always determined by the result of the automatic login step that follows registration.
- The negative scenario surfaces "Credenciales inválidas" because the backend rejects the auto-login step (401). This is the only observable error message produced by this failure path.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: A new staff member can complete account creation and reach the main operational view in a single uninterrupted flow, with no manual login step required after registration
- **SC-002**: 100% of positive registration test executions result in the user reaching the main operational view when valid unique credentials are provided
- **SC-003**: 100% of negative registration test executions result in a visible "Credenciales inválidas" error message on the authentication page when an already-registered email is submitted with a different password
- **SC-004**: Each scenario is independently executable with no shared state, no dependency on execution order, and no reliance on data produced by a prior run
- **SC-005**: Both scenarios are tagged (`@positiveRegister`, `@negativeRegister`) and can be executed in isolation by tag filter without running the full suite
