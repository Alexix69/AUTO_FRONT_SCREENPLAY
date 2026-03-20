# Implementation Plan: User Registration with Automatic Session Start

**Branch**: `002-register-screenplay` | **Date**: 2026-03-19 | **Spec**: [spec.md](spec.md)
**Input**: Feature specification from `/specs/002-register-screenplay/spec.md`

## Summary

Implement Screenplay-based end-to-end automation for the FoodTech registration flow.
Registration and login share a single page (`/login`). A successful registration
immediately triggers an automatic login, redirecting the actor to `/mesero`. A failed
automatic login (email already registered, different password) surfaces the error message
"Credenciales inválidas" in the DOM while the actor remains on `/login`. Both scenarios
are independently executable via Cucumber tag filters with deterministic test data managed
through lifecycle hooks.

---

## Technical Context

**Language/Version**: Java 21 (OpenJDK)
**Build Tool**: Gradle (wrapper — `./gradlew`)
**Framework**: Serenity BDD + Cucumber (cucumber-junit-platform-engine 7.34.2)
**Pattern**: Screenplay (Constitution §7) — no POM classes, no page abstractions
**Web Driver**: Selenium WebDriver via `serenity-screenplay-webdriver`
**API Client**: `java.net.http.HttpClient` via `CallTheApi` ability (test data setup only)
**Reporting**: Serenity HTML (`serenity-gradle-plugin 5.3.2`)
**Target Project**: `AUTO_FRONT_SCREENPLAY`
**Constraints**: Scenarios independently executable; no shared mutable state between scenarios

---

## Constitution Check

| # | Gate | §Ref | Status |
|---|---|---|---|
| G1 | Scenarios validate **system behavior**, not UI mechanics | §2 | ✅ |
| G2 | All scenarios are **independently executable** — no shared state | §3 | ✅ |
| G3 | Gherkin uses Given/When/Then with **no implementation details** | §4 | ✅ |
| G4 | Serenity BDD components present: feature files, step defs, runner, config | §5 | ✅ |
| G5 | POM only (AUTO_FRONT_POM_FACTORY): Page Objects hold locators + behavior, no assertions | §6 | N/A |
| G6 | Screenplay only (AUTO_FRONT_SCREENPLAY): Actor/Task/Action/Question split enforced, SRP | §7 | ✅ |
| G7 | API project: validates status code + response body + business rules | §8 | N/A |
| G8 | Semantic class naming, no abbreviations | §9 | ✅ |
| G9 | No commented code, unused variables, or unclear methods | §10 | ☐ (enforced at impl review) |
| G10 | Serenity report configured and generated on every test run | §11 | ✅ |
| G11 | No direct implementation — spec → plan → tasks → implement flow followed | §12 | ✅ |

---

## 1. Technical Architecture

### 1.1 Actor Model

```
Actor("staff member")
  ├── Ability: BrowseTheWeb          ← drives all UI interactions
  └── Ability: CallTheApi            ← used exclusively in hook setup (never in Tasks/Questions)
```

The Actor is placed on stage in `@Before` hooks. `OnStage.setTheStage(new OnlineCast())`
initialises the stage; `OnStage.theActorInTheSpotlight()` retrieves the active actor
in step definitions. `OnStage.drawTheCurtainOn()` tears down the Actor in `@After` hooks.

`CallTheApi` carries the HTTP client used by `RegistrationApiClient` during test data setup.
It is NOT used inside any Task or Interaction — only in `RegisterHooks`.

---

### 1.2 Abilities

| Ability | Source | Purpose |
|---|---|---|
| `BrowseTheWeb` | `serenity-screenplay-webdriver` | All UI navigation, clicks, and field entry |
| `CallTheApi` | `serenity-screenplay` (or custom wrapper over `java.net.http.HttpClient`) | Pre-test user registration via `POST /api/auth/register` |

---

### 1.3 Tasks

Each Task represents exactly one business action. Tasks contain zero assertions.

#### `NavigateToAuthenticationPage`

**Business action**: Place the actor on the authentication page with demo mode confirmed off.

**Composed of**:
1. `Open` — navigates to `TestConfig.getBaseUrl() + "/login"`
2. `WaitUntil` — `[data-testid="submit-btn"]` is visible (form is rendered and ready)
3. `WaitUntil` — `[data-testid="demo-mode-checkbox"]` is present in the DOM
4. Verify demo mode checkbox is NOT checked — if it is checked, the task fails with an explicit
   precondition message: "Precondition failed: demo mode is active"

**Mapped Gherkin steps**:
- `Given the user is on the authentication page with demo mode off`

**Precondition established**: The authentication form is in default login mode; demo mode is off.

---

#### `ActivateRegistrationMode`

**Business action**: Transition the form from login mode to registration mode.

**Composed of**:
1. `Click` — `[data-testid="toggle-mode-btn"]`
2. `WaitUntil` — `[data-testid="username-input"]` is visible

The username field is conditionally rendered by React only when `isRegisterMode === true`.
Its DOM presence is the authoritative signal that mode transition is complete.

**Mapped Gherkin steps**:
- `And the user activates the registration mode`

**Precondition established**: Three input fields are visible (email, username, password);
form is in registration mode.

---

#### `ProvideRegistrationData`

**Business action**: Fill the three registration form fields with the actor's data.

**Composed of** (order is mandatory — email first, then username, then password):
1. `Enter` — email value into `[data-testid="email-input"]`
2. `Enter` — username value into `[data-testid="username-input"]`
3. `Enter` — password value into `[data-testid="password-input"]`

**Parameterized by**: A `RegistrationData` record carrying `email`, `username`, `password`.
Exposes a static factory: `ProvideRegistrationData.with(RegistrationData data)`.
The task has no knowledge of whether the data is unique or conflicting — that distinction
lives entirely in the hook that populates `TestContext`.

**Mapped Gherkin steps**:
- `When the user provides a valid email, a unique username, and a valid password`
  → `ProvideRegistrationData.with(TestContext.getUser())`
- `When the user provides an email already registered with a different password`
  → `ProvideRegistrationData.with(TestContext.getConflictingUser())`

---

#### `SubmitRegistration`

**Business action**: Trigger the form submission and wait until the system reaches a
stable, observable terminal state.

**Composed of**:
1. `Click` — `[data-testid="submit-btn"]`
2. `WaitUntil` — submit button is `disabled` (confirms form handler has been invoked;
   button text changes to "Registrando...")
3. `WaitUntil` — branched terminal condition resolves (see §5 Synchronization Strategy):
   - URL contains `/mesero` **OR** `[data-testid="error-message"]` is visible in the DOM

**This task asserts nothing.** It advances the actor to the point where the system
has reached a stable, observable state. Which state was reached is determined by
Questions invoked after this task completes.

**Mapped Gherkin steps**:
- `And the user submits the registration form`

---

### 1.4 Questions

Each Question extracts exactly one observable fact. Questions contain zero side effects.

#### `CurrentUrl`

**Observable fact**: The current browser URL as a string.

**Returns**: `Question<String>` — the full URL from `Serenity.getDriver().getCurrentUrl()`.

**No waits** — invoked only after `SubmitRegistration` has already guaranteed a terminal state.

**Used to assert**:
- Positive scenario: URL contains `/mesero` — confirms auto-login succeeded and
  operational view is loaded
- Negative scenario: URL still contains `/login` — confirms actor has not been navigated away

**Mapped Gherkin steps**:
- `Then the system creates the account and starts the session automatically`
  → `actor.should(seeThat(CurrentUrl.forThePage(), containsString("/mesero")))`
- `And the user is taken to the main operational view`
  → `actor.should(seeThat(CurrentUrl.forThePage(), containsString("/mesero")))`
- `And the user remains on the authentication page`
  → `actor.should(seeThat(CurrentUrl.forThePage(), containsString("/login")))`

Note: both positive Then steps are bound to `CurrentUrl` because the URL change to `/mesero`
is the single observable proof that (a) the account was created, (b) auto-login succeeded,
and (c) navigation to the operational view occurred. They are not redundant — they are
two business assertions grounded in the same observable fact.

---

#### `VisibleErrorMessage`

**Observable fact**: The text content of the error message element when it is visible in the DOM.

**Returns**: `Question<String>` — text of `[data-testid="error-message"]`.

**Precondition**: Element is visible before this Question is asked.
`SubmitRegistration` guarantees this for the negative path.

**Used to assert**:
- Negative scenario: text equals `"Credenciales inválidas"`

**Mapped Gherkin step**:
- `Then the system displays an invalid credentials message`
  → `actor.should(seeThat(VisibleErrorMessage.forThePage(), equalTo("Credenciales inválidas")))`

---

### 1.5 Interactions

Low-level reusable UI actions. Used internally by Tasks. Never referenced directly from
step definitions.

| Interaction | Provided by | Used in |
|---|---|---|
| `Click` | `serenity-screenplay-webdriver` | ActivateRegistrationMode, SubmitRegistration |
| `Enter` | `serenity-screenplay-webdriver` | ProvideRegistrationData (3 uses) |
| `Open` | `serenity-screenplay-webdriver` | NavigateToAuthenticationPage |
| `WaitUntil` | `serenity-screenplay-webdriver` | NavigateToAuthenticationPage, ActivateRegistrationMode, SubmitRegistration |

---

### 1.6 UI Targets

All `Target` locator constants are centralised in a single class `AuthenticationPageUI`.
No selector string appears in any Task, Interaction, or Question.

| Constant | Selector | Used by |
|---|---|---|
| `SUBMIT_BUTTON` | `[data-testid="submit-btn"]` | NavigateToAuthenticationPage, SubmitRegistration |
| `TOGGLE_MODE_BUTTON` | `[data-testid="toggle-mode-btn"]` | ActivateRegistrationMode |
| `USERNAME_INPUT` | `[data-testid="username-input"]` | ActivateRegistrationMode (wait condition), ProvideRegistrationData |
| `EMAIL_INPUT` | `[data-testid="email-input"]` | ProvideRegistrationData |
| `PASSWORD_INPUT` | `[data-testid="password-input"]` | ProvideRegistrationData |
| `ERROR_MESSAGE` | `[data-testid="error-message"]` | SubmitRegistration (wait condition), VisibleErrorMessage |
| `DEMO_MODE_CHECKBOX` | `[data-testid="demo-mode-checkbox"]` | NavigateToAuthenticationPage (precondition check) |

---

## 2. Component Responsibilities

| Component | Responsibility | Must NOT contain |
|---|---|---|
| `Actor` | Represent the user; carry Abilities | Business logic |
| `Task` | One business action; composed of Interactions + explicit waits | Assertions, Questions, direct WebDriver calls |
| `Interaction` | One low-level UI action | More than one UI gesture |
| `Question` | Extract one observable fact from system state | Side effects, state changes, waits |
| `StepDefinition` | One Cucumber binding → one Actor delegation; nothing else | Logic, assertions, conditionals, direct WebDriver access |
| `Hook` | Lifecycle setup/teardown; stage init; test data via API | UI actions, WebDriver, assertions |
| `AuthenticationPageUI` | All `Target` constants | Business logic |
| `RegistrationApiClient` | Pre-test user creation via HTTP; called only from hooks | WebDriver, assertions, UI logic |
| `TestContext` | `ThreadLocal` per-scenario data slots | Business logic |
| `TestDataFactory` | Deterministic data generation | Stateful data or side effects |
| `TestConfig` | Environment URL resolution | Business or test logic |

---

## 3. Execution Flow

### 3.1 Positive Scenario (`@positiveRegister`)

```
[Hook: @Before @positiveRegister]
  1. OnStage.setTheStage(new OnlineCast())
  2. data = TestDataFactory.createRegistrationData()     → unique email, username, password
  3. TestContext.setUser(data)

[Cucumber steps]
  4. Given the user is on the authentication page with demo mode off
        → actor.attemptsTo(NavigateToAuthenticationPage.now())
           - Open /login
           - WaitUntil SUBMIT_BUTTON visible
           - WaitUntil DEMO_MODE_CHECKBOX present; assert unchecked

  5. And the user activates the registration mode
        → actor.attemptsTo(ActivateRegistrationMode.now())
           - Click TOGGLE_MODE_BUTTON
           - WaitUntil USERNAME_INPUT visible

  6. When the user provides a valid email, a unique username, and a valid password
        → actor.attemptsTo(ProvideRegistrationData.with(TestContext.getUser()))
           - Enter email → EMAIL_INPUT
           - Enter username → USERNAME_INPUT
           - Enter password → PASSWORD_INPUT

  7. And the user submits the registration form
        → actor.attemptsTo(SubmitRegistration.now())
           - Click SUBMIT_BUTTON
           - WaitUntil SUBMIT_BUTTON is disabled          (loading state active)
           - FluentWait 10s/500ms: URL contains "/mesero" OR ERROR_MESSAGE visible
              → resolves on "/mesero" (auto-login succeeded)

  8. Then the system creates the account and starts the session automatically
        → actor.should(seeThat(CurrentUrl.forThePage(), containsString("/mesero")))

  9. And the user is taken to the main operational view
        → actor.should(seeThat(CurrentUrl.forThePage(), containsString("/mesero")))

[Hook: @After @positiveRegister]
  10. TestContext.clear()
  11. OnStage.drawTheCurtainOn()
```

---

### 3.2 Negative Scenario (`@negativeRegister`)

```
[Hook: @Before @negativeRegister]
  1. OnStage.setTheStage(new OnlineCast())
  2. dataA = TestDataFactory.createRegistrationData()    → unique email, username, password-A
  3. RegistrationApiClient.register(dataA)               → account created in backend
  4. conflicting = new RegistrationData(dataA.email(), dataA.username(),
                       TestDataFactory.generatePassword())  → same email, password-B ≠ password-A
  5. TestContext.setConflictingUser(conflicting)

[Cucumber steps]
  6. Given the user is on the authentication page with demo mode off
        → actor.attemptsTo(NavigateToAuthenticationPage.now())

  7. And the user activates the registration mode
        → actor.attemptsTo(ActivateRegistrationMode.now())

  8. When the user provides an email already registered with a different password
        → actor.attemptsTo(ProvideRegistrationData.with(TestContext.getConflictingUser()))
           - Enter email (already registered) → EMAIL_INPUT
           - Enter username → USERNAME_INPUT
           - Enter password-B (different from password-A) → PASSWORD_INPUT

  9. And the user submits the registration form
        → actor.attemptsTo(SubmitRegistration.now())
           - Click SUBMIT_BUTTON
           - WaitUntil SUBMIT_BUTTON is disabled
           - FluentWait 10s/500ms: URL contains "/mesero" OR ERROR_MESSAGE visible
              → resolves on ERROR_MESSAGE visible (auto-login failed; 401 from backend)

  10. Then the system displays an invalid credentials message
        → actor.should(seeThat(VisibleErrorMessage.forThePage(), equalTo("Credenciales inválidas")))

  11. And the user remains on the authentication page
        → actor.should(seeThat(CurrentUrl.forThePage(), containsString("/login")))

[Hook: @After @negativeRegister]
  12. TestContext.clear()
  13. OnStage.drawTheCurtainOn()
```

---

## 4. Package Layout

```
AUTO_FRONT_SCREENPLAY/
  src/test/java/
    com/foodtech/automation/screenplay/register/
      tasks/
        NavigateToAuthenticationPage.java
        ActivateRegistrationMode.java
        ProvideRegistrationData.java
        SubmitRegistration.java
      questions/
        CurrentUrl.java
        VisibleErrorMessage.java
      ui/
        AuthenticationPageUI.java

    com/foodtech/automation/screenplay/support/
      TestConfig.java
      TestDataFactory.java
      TestContext.java
      RegistrationApiClient.java

    com/foodtech/automation/screenplay/stepdefinitions/
      RegisterStepDefinitions.java
      RegisterHooks.java

    com/foodtech/automation/screenplay/runners/
      RegisterTestRunner.java

  src/test/resources/features/register/
    register.feature

  serenity.properties
  build.gradle
```

---

## 5. Data Strategy

### 5.1 Positive Scenario

| Attribute | Strategy |
|---|---|
| Email | `"test+" + Instant.now().toEpochMilli() + "@restaurant.com"` — unique per run |
| Username | `"user" + Instant.now().toEpochMilli()` — unique per run |
| Password | `"Pass" + Instant.now().toEpochMilli()` — unique per run |
| Backend pre-setup | None — account must NOT exist before the test |
| Source | Generated in `@Before("@positiveRegister")` hook; stored in `TestContext.USER` |

### 5.2 Negative Scenario

| Attribute | Strategy |
|---|---|
| Email (A) | `TestDataFactory.generateEmail()` — unique per run |
| Username (A) | `TestDataFactory.generateUsername()` |
| Password (A) | `TestDataFactory.generatePassword()` — registered with the backend |
| Password (B) | `TestDataFactory.generatePassword()` called again (epoch millis advances) — guaranteed ≠ password-A |
| Backend pre-setup | `RegistrationApiClient.register(dataA)` — creates account via `POST /api/auth/register` |
| Source | Generated and pre-registered in `@Before("@negativeRegister")` hook; conflicting object stored in `TestContext.CONFLICTING_USER` |

### 5.3 `TestContext` — ThreadLocal Slots

```
ThreadLocal<RegistrationData> USER               → positive scenario data
ThreadLocal<RegistrationData> CONFLICTING_USER   → negative scenario data (email matches registered; password does not)
```

`clear()` removes both slots. Called in every `@After` hook.

### 5.4 `RegistrationApiClient`

- Endpoint: `POST <backendBaseUrl>/api/auth/register`
- Payload: `{ "email": "...", "username": "...", "password": "..." }`
- Transport: `java.net.http.HttpClient` with 5-second connect + request timeout
- Success: HTTP 200 or 201
- Failure: throws `IllegalStateException("Setup failed: backend registration unavailable (status N)")` — fails the test before the browser opens

---

## 6. Synchronization Strategy

### 6.1 The Async Problem

Submitting the registration form triggers two sequential HTTP calls:

```
Click SUBMIT_BUTTON
  → POST /api/auth/register          (response status ignored by frontend)
  → POST /api/auth/login
       ├── 200 OK  → navigate("/mesero")
       └── 401     → setError("Credenciales inválidas")
```

Total async window: 2 × network round-trip. Timing is backend-dependent. All waits
are condition-based. Thread.sleep is forbidden.

### 6.2 Wait Definition for `SubmitRegistration`

**Phase 1 — Loading state** (confirms form handler invoked):
```
WaitUntil.the(SUBMIT_BUTTON, WebElementStateMatchers.isNotEnabled())
  timeout: 3 seconds
```

**Phase 2 — Terminal state** (branched condition; whichever arrives first):
```
FluentWait(driver)
  .withTimeout(Duration.ofSeconds(10))
  .pollingEvery(Duration.ofMillis(500))
  .ignoring(NoSuchElementException.class)
  .until(driver ->
      driver.getCurrentUrl().contains("/mesero")
      || isVisible(driver, ERROR_MESSAGE)
  )
```

`isVisible` is a private helper inside `SubmitRegistration`:
- locates `ERROR_MESSAGE` target
- returns false if not found or not displayed, never throws
- returns true only when element is in DOM AND visible

### 6.3 Wait definition for `NavigateToAuthenticationPage`

```
WaitUntil.the(SUBMIT_BUTTON, WebElementStateMatchers.isVisible())
  timeout: 5 seconds
WaitUntil.the(DEMO_MODE_CHECKBOX, WebElementStateMatchers.isPresent())
  timeout: 3 seconds
```

After both waits resolve: verify `DEMO_MODE_CHECKBOX.resolveFor(actor).isSelected() == false`.
If checked: fail with explicit message before any UI interaction.

### 6.4 Wait definition for `ActivateRegistrationMode`

```
WaitUntil.the(USERNAME_INPUT, WebElementStateMatchers.isVisible())
  timeout: 5 seconds
```

### 6.5 Questions — No Internal Waits

`CurrentUrl` and `VisibleErrorMessage` contain no waits. Terminal-state guarantees
are entirely the responsibility of `SubmitRegistration`. If either Question is invoked
before `SubmitRegistration` completes, the test has a structural error.

---

## 7. Risks and Constraints

| ID | Source | Risk | Mitigation |
|---|---|---|---|
| R-01 | spec Assumption 5 | `authService.register()` ignores HTTP response status — the frontend always proceeds to auto-login regardless of backend outcome | The negative scenario is grounded in auto-login failure (401), not registration failure. This is the correct and only deterministic error surface. |
| R-02 | spec Assumption 6 | "Credenciales inválidas" is the only observable error message in this path | Step text and Question assertion are both hardcoded to this string. Any backend change to the error message will break assertions. |
| R-03 | spec Assumption 1 | Negative scenario requires a live, reachable backend for `RegistrationApiClient` | `RegistrationApiClient` fails fast with `IllegalStateException` if backend returns non-200/201, preventing the browser from opening with invalid preconditions. |
| R-04 | spec Assumption 4 / FR-010 | Demo mode checkbox is visible in both login and registration modes | `NavigateToAuthenticationPage` explicitly checks and fails if demo mode is active. This prevents silent test pass on an incorrect UI state. |
| R-05 | sync strategy §6.2 | Two sequential HTTP calls mean FluentWait timeout must cover both round trips under load | 10-second timeout with 500ms polling is the documented floor. May need adjustment if backend response time exceeds 4 seconds per call. |
| R-06 | spec Assumption 2 | Network failure scenarios are out of scope | No error handling for connection timeouts in Tasks or Questions. Setup-time failures in `RegistrationApiClient` are caught separately. |
