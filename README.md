# AUTO_FRONT_SCREENPLAY

Frontend UI automation for the **FoodTech** restaurant management application using the **Screenplay** pattern with **Serenity BDD**.

---

## Workshop Context

This project is the second of three automation deliverables for **Semana 5 — Maestría en automatización: del objeto al actor**. It covers the Screenplay requirement with two new, independent scenarios — different from those automated with POM — validating the FoodTech user registration flow with automatic session start.

---

## Application Under Test

**FoodTech Front** — React 19 SPA running at `http://localhost:5173` (configurable).

| Attribute | Value |
|---|---|
| Authentication URL | `{base.url}/login` |
| Post-registration redirect | `/mesero` (waiter dashboard) |
| Toggle mode button | `data-testid="toggle-mode-btn"` |
| Username field | `data-testid="username-input"` |
| Email field | `data-testid="email-input"` |
| Password field | `data-testid="password-input"` |
| Submit button | `data-testid="submit-btn"` |
| Demo mode checkbox | `data-testid="demo-mode-checkbox"` |
| Error message | `data-testid="error-message"` |
| Error text on login failure | `Credenciales inválidas` |

---

## Scenarios Covered

| # | Tag | Type | Description |
|---|---|---|---|
| 1 | `@positiveRegister` | Positive | New user registers → system auto-starts session → redirected to `/mesero` |
| 2 | `@negativeRegister` | Negative | Registration with an already-registered email → auto-login fails → error message visible, remains on auth page |

Both scenarios are **fully independent**. The negative scenario pre-registers a conflicting user via the backend API in `@Before`, ensuring no state dependency between runs.

> These scenarios are **completely distinct** from those in `AUTO_FRONT_POM_FACTORY`. The POM project validates login; this project validates registration with automatic session start.

---

## Tech Stack

| Tool | Version |
|---|---|
| Java | 21 |
| Serenity BDD | 5.3.2 |
| Serenity Screenplay WebDriver | 5.3.2 |
| Cucumber | 7.34.2 (JUnit 4 runner) |
| Selenium WebDriver | managed by Serenity |
| Gradle | Wrapper (included) |
| JUnit | 4.13.2 |
| Browser | Google Chrome |

---

## Architecture

```
src/test/java/com/foodtech/automation/screenplay/
├── register/
│   ├── tasks/
│   │   ├── NavigateToAuthenticationPage.java ← Task: open auth URL (SRP: navigation only)
│   │   ├── ActivateRegistrationMode.java     ← Task: toggle to registration form (SRP: mode switch only)
│   │   ├── ProvideRegistrationData.java      ← Task: fill email, username, password (SRP: data entry only)
│   │   └── SubmitRegistration.java           ← Task: submit form (SRP: submission only)
│   ├── questions/
│   │   ├── CurrentUrl.java           ← Question: returns current browser URL (no side effects)
│   │   └── VisibleErrorMessage.java  ← Question: returns visible error text (no side effects)
│   └── ui/
│       └── AuthenticationPageUI.java ← Target constants (7 UI targets, no logic)
├── support/
│   ├── RegistrationData.java     ← Record: email + username + password
│   ├── TestDataFactory.java      ← Unique credential generation per run
│   ├── TestContext.java          ← ThreadLocal state: USER + CONFLICTING_USER slots
│   ├── TestConfig.java           ← URL resolution (env var → system property → default)
│   ├── RegistrationApiClient.java ← Backend user provisioning via HTTP
│   └── (unused)
├── stepdefinitions/
│   ├── RegisterStepDefinitions.java ← Glue: Gherkin → Task/Question delegation (no logic)
│   └── RegisterHooks.java           ← @Before: sets up test data; @After: cleans state
└── runners/
    └── RegisterTestRunner.java  ← @RunWith(CucumberWithSerenity) runner

src/test/resources/
└── features/register/register.feature  ← Gherkin: 2 scenarios
serenity.properties                     ← WebDriver + screenshot configuration
```

**Screenplay SRP enforcement**:
- Each `Task` performs exactly one responsibility — navigation, mode activation, data entry, or form submission.
- `Questions` are pure read-only observers with no side effects.
- `Target` constants are isolated in `AuthenticationPageUI` — no selector appears in Tasks or Questions.
- Step Definitions delegate to exactly one Task or Question per step. No assertion lives inside any Task.

---

## Prerequisites

- Java 21 (`java -version`)
- Google Chrome installed
- FoodTech Front running at `http://localhost:5173` (or configured URL)
- FoodTech Kitchen Services API running at `http://localhost:8080` (required for `@Before` user provisioning in negative scenario)

---

## Configuration

Base URL resolution order:

1. Environment variable: `FOODTECH_BASE_URL`
2. Default: `http://localhost:5173`

Backend URL resolution order:

1. Environment variable: `FOODTECH_BACKEND_URL`
2. Default: `http://localhost:8080`

Configured in `serenity.properties`:
```properties
webdriver.driver=chrome
webdriver.base.url=http://localhost:5173
headless.mode=false
serenity.take.screenshots=FOR_EACH_ACTION
```

---

## How to Execute

**Run all scenarios:**
```bash
./gradlew clean test aggregate
```

**Run only the positive scenario:**
```bash
./gradlew clean test aggregate -Dcucumber.filter.tags="@positiveRegister"
```

**Run only the negative scenario:**
```bash
./gradlew clean test aggregate -Dcucumber.filter.tags="@negativeRegister"
```

**Override the application URL:**
```bash
FOODTECH_BASE_URL=http://my-host:5173 ./gradlew clean test aggregate
```

---

## Reports

After execution:

| Report | Location |
|---|---|
| Serenity HTML report | `target/site/serenity/index.html` |
| Single-page report | `target/site/serenity/serenity-summary.html` |

Open `target/site/serenity/index.html` in any browser. No server required.

---

## Spec-Driven Development with SpecKit

This project was built using **Spec-Driven Development (SDD)** with the SpecKit workflow:

```
constitution → specify → plan → tasks → implement
```

All specification artifacts produced during development are preserved in [`docs/specs/002-register-screenplay/`](docs/specs/002-register-screenplay/):

| Artifact | Purpose |
|---|---|
| [`spec.md`](docs/specs/002-register-screenplay/spec.md) | Feature specification: user stories, acceptance criteria, edge cases, Gherkin |
| [`plan.md`](docs/specs/002-register-screenplay/plan.md) | Implementation plan: Screenplay architecture, constitution compliance gate |
| [`tasks.md`](docs/specs/002-register-screenplay/tasks.md) | Atomic task breakdown with Gherkin→Screenplay mapping table and completion tracking |
| [`checklists/`](docs/specs/002-register-screenplay/checklists/) | Pre-implementation readiness gates |

The spec was produced by AI-assisted analysis of FoodTech registration user stories — spec defined first, code written second following the task breakdown exactly.

---

## Notes for Evaluators

- `RegisterHooks` (`@Before`) provisions a unique fresh user before each positive scenario and a pre-existing conflicting user before each negative scenario — no shared mutable state between tests.
- `TestDataFactory` uses `Instant.now().toEpochMilli()` + salt to guarantee unique credentials on every run, even in rapid succession.
- Demo mode is explicitly validated off (`DEMO_MODE_CHECKBOX` target) before each scenario to prevent the demo bypass path from interfering.
- The Screenplay pattern enforces full SRP: 4 Tasks × 1 responsibility each, 2 Questions × pure observation only.
