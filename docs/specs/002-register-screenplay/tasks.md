# Tasks: User Registration with Automatic Session Start

**Input**: Design documents from `/specs/002-register-screenplay/`
**Prerequisites**: [plan.md](plan.md) ✅ | [spec.md](spec.md) ✅
**Target Repo**: `AUTO_FRONT_SCREENPLAY`
**Framework**: Serenity BDD 5.x + Cucumber 7.x + Gradle + Java 21
**Pattern**: Pure Screenplay (Constitution §7) — no POM, no page classes, no assertions inside Tasks, no side effects inside Questions

## Format: `[ID] [P?] [Story?] Description`

- **[P]**: Can run in parallel with other [P] tasks in the same phase (different files, no blocking dependency)
- **[Story]**: User story label — [US1] = Positive Registration + Auto-Login | [US2] = Failed Auto-Login
- Exact file paths are included in all implementation tasks

---

## Gherkin Step → Screenplay Component Mapping

| Gherkin Step | Screenplay Component | Delegation |
|---|---|---|
| `Given the user is on the authentication page with demo mode off` | Task | `actor.attemptsTo(NavigateToAuthenticationPage.now())` |
| `And the user activates the registration mode` | Task | `actor.attemptsTo(ActivateRegistrationMode.now())` |
| `When the user provides a valid email, a unique username, and a valid password` | Task | `actor.attemptsTo(ProvideRegistrationData.with(TestContext.getUser()))` |
| `When the user provides an email already registered with a different password` | Task | `actor.attemptsTo(ProvideRegistrationData.with(TestContext.getConflictingUser()))` |
| `And the user submits the registration form` | Task | `actor.attemptsTo(SubmitRegistration.now())` |
| `Then the system creates the account and starts the session automatically` | Question | `actor.should(seeThat(CurrentUrl.forThePage(), containsString("/mesero")))` |
| `And the user is taken to the main operational view` | Question | `actor.should(seeThat(CurrentUrl.forThePage(), containsString("/mesero")))` |
| `Then the system displays an invalid credentials message` | Question | `actor.should(seeThat(VisibleErrorMessage.forThePage(), equalTo("Credenciales inválidas")))` |
| `And the user remains on the authentication page` | Question | `actor.should(seeThat(CurrentUrl.forThePage(), containsString("/login")))` |

> **Traceability rule**: every Gherkin step in `register.feature` maps 1:1 to exactly one delegation in `RegisterStepDefinitions`. No step delegates to more than one Task or Question. No assertion lives inside a Task.

---

## Phase 1: Serenity BDD + Screenplay Setup

**Purpose**: Bootstrap `AUTO_FRONT_SCREENPLAY` with the Serenity BDD + Cucumber project structure, Gradle configuration, and test runner. This phase produces a compilable skeleton with no feature code.
*(Constitution §5 Serenity components | §11 reporting | §12 Spec-Driven workflow)*

- [X] T001 Verify or initialize the standard directory structure in `AUTO_FRONT_SCREENPLAY/`: `src/test/java/`, `src/test/resources/`, `src/test/resources/features/register/`, `gradle/wrapper/`
- [X] T002 [P] Configure `AUTO_FRONT_SCREENPLAY/build.gradle` — add `serenity-screenplay-webdriver`, `serenity-screenplay`, `serenity-cucumber`, `cucumber-junit-platform-engine 7.34.2`, `serenity-gradle-plugin 5.3.2`; set `sourceCompatibility = JavaVersion.VERSION_21`, `targetCompatibility = JavaVersion.VERSION_21`
- [X] T003 [P] Configure `AUTO_FRONT_SCREENPLAY/serenity.properties` — set `serenity.project.name=FoodTech Register Automation`, `webdriver.driver=chrome`, `headless.mode=true`, `webdriver.base.url=http://localhost:5173`, `serenity.take.screenshots=AFTER_EACH_STEP`
- [X] T004 Create `AUTO_FRONT_SCREENPLAY/src/test/java/com/foodtech/automation/screenplay/runners/RegisterTestRunner.java` — annotate with `@Suite`, `@IncludeEngines("cucumber")`, `@SelectClasspathResource("features/register")`, `@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.foodtech.automation.screenplay.stepdefinitions")`

**Checkpoint**: `./gradlew compileTestJava` succeeds with no compilation errors. Runner class and `serenity.properties` are present.

---

## Phase 2: Screenplay Foundation (Blocking Prerequisites)

**Purpose**: Create all Screenplay infrastructure components shared by both user stories — UI targets, test data model, data factories, test context, API client, feature file, all four Tasks, and the shared Question `CurrentUrl`. No user story step definition or hook work begins until this phase is complete.
*(Constitution §7 Screenplay SRP | §8 Semantic naming | §9 Clean code)*

**⚠️ CRITICAL**: Both user stories depend on every component in this phase. No step definition or hook implementation can begin until this phase is complete.

- [X] T005 [P] Create `AUTO_FRONT_SCREENPLAY/src/test/java/com/foodtech/automation/screenplay/register/ui/AuthenticationPageUI.java` — declare all 7 `Target` constants: `SUBMIT_BUTTON` (`[data-testid="submit-btn"]`), `TOGGLE_MODE_BUTTON` (`[data-testid="toggle-mode-btn"]`), `USERNAME_INPUT` (`[data-testid="username-input"]`), `EMAIL_INPUT` (`[data-testid="email-input"]`), `PASSWORD_INPUT` (`[data-testid="password-input"]`), `ERROR_MESSAGE` (`[data-testid="error-message"]`), `DEMO_MODE_CHECKBOX` (`[data-testid="demo-mode-checkbox"]`) — no business logic, no selectors outside this class
- [X] T006 [P] Create `AUTO_FRONT_SCREENPLAY/src/test/java/com/foodtech/automation/screenplay/support/RegistrationData.java` — Java record with three fields: `String email`, `String username`, `String password`
- [X] T007 [P] Create `AUTO_FRONT_SCREENPLAY/src/test/java/com/foodtech/automation/screenplay/support/TestConfig.java` — implement `getBaseUrl()` resolving `FOODTECH_BASE_URL` env var, falling back to `http://localhost:5173`; implement `getBackendBaseUrl()` resolving `FOODTECH_BACKEND_URL`, falling back to `http://localhost:8080`
- [X] T008 [P] Create `AUTO_FRONT_SCREENPLAY/src/test/java/com/foodtech/automation/screenplay/support/TestDataFactory.java` — implement `generateEmail()` → `"test+" + Instant.now().toEpochMilli() + "@restaurant.com"`, `generateUsername()` → `"user" + Instant.now().toEpochMilli()`, `generatePassword(String salt)` → `"Pass" + UUID.randomUUID().toString().replace("-", "").substring(0, 8) + salt` (the `salt` argument (`"A"` or `"B"`) makes each call produce a structurally distinct value even if called within the same millisecond; two consecutive calls with different salts can NEVER return equal strings), `createRegistrationData()` → `new RegistrationData(generateEmail(), generateUsername(), generatePassword("A"))` — no state, no side effects
- [X] T009 [P] Create `AUTO_FRONT_SCREENPLAY/src/test/java/com/foodtech/automation/screenplay/support/TestContext.java` — declare two `ThreadLocal<RegistrationData>` slots: `USER` and `CONFLICTING_USER`; implement `setUser(RegistrationData)`, `getUser()`, `setConflictingUser(RegistrationData)`, `getConflictingUser()`, `clear()` (removes both slots) — no business logic
- [X] T010 [P] Create `AUTO_FRONT_SCREENPLAY/src/test/java/com/foodtech/automation/screenplay/support/RegistrationApiClient.java` — implement `register(RegistrationData data)` posting `{ "email": "...", "username": "...", "password": "..." }` to `TestConfig.getBackendBaseUrl() + "/api/auth/register"` via `java.net.http.HttpClient` (5-second connect + request timeout); accepts HTTP 200 or 201; throws `IllegalStateException("Setup failed: backend registration unavailable (status N)")` on any other status — called only from `RegisterHooks`, never from Tasks or Questions
- [X] T011 Create `AUTO_FRONT_SCREENPLAY/src/test/resources/features/register/register.feature` — write Feature header with narrative (`As a new restaurant staff member / I want to create an account from the authentication page / So that I can access the system and begin managing orders`); add `@positiveRegister` Scenario with all 6 Given/And/When/And/Then/And steps; add `@negativeRegister` Scenario with all 6 Given/And/When/And/Then/And steps — steps must match spec.md Gherkin verbatim
- [X] T012 Create `AUTO_FRONT_SCREENPLAY/src/test/java/com/foodtech/automation/screenplay/register/tasks/NavigateToAuthenticationPage.java` — implements `Performable`; static factory `now()`; body: `Open` URL from `TestConfig.getBaseUrl() + "/login"`, `WaitUntil.the(SUBMIT_BUTTON, isVisible())` timeout 5s, `WaitUntil.the(DEMO_MODE_CHECKBOX, isPresent())` timeout 3s; then verify `DEMO_MODE_CHECKBOX.resolveFor(actor).isSelected() == false` — if checked throw `IllegalStateException("Precondition failed: demo mode is active")`; zero assertions; zero Questions
- [X] T013 [P] Create `AUTO_FRONT_SCREENPLAY/src/test/java/com/foodtech/automation/screenplay/register/tasks/ActivateRegistrationMode.java` — implements `Performable`; static factory `now()`; body: `Click.on(TOGGLE_MODE_BUTTON)`, `WaitUntil.the(USERNAME_INPUT, isVisible())` timeout 5s; zero assertions; zero Questions
- [X] T014 [P] Create `AUTO_FRONT_SCREENPLAY/src/test/java/com/foodtech/automation/screenplay/register/tasks/ProvideRegistrationData.java` — implements `Performable`; static factory `with(RegistrationData data)`; body (order mandatory): `Enter.theValue(data.email()).into(EMAIL_INPUT)`, `Enter.theValue(data.username()).into(USERNAME_INPUT)`, `Enter.theValue(data.password()).into(PASSWORD_INPUT)`; no knowledge of whether data is unique or conflicting; zero assertions; zero Questions
- [X] T015 Create `AUTO_FRONT_SCREENPLAY/src/test/java/com/foodtech/automation/screenplay/register/tasks/SubmitRegistration.java` — implements `Performable`; static factory `now()`; body: `Click.on(SUBMIT_BUTTON)`, `WaitUntil.the(SUBMIT_BUTTON, isNotEnabled())` timeout 3s; then obtain `WebDriver driver = BrowseTheWeb.as(actor).getDriver()` (actor-scoped — do NOT use `Serenity.getDriver()`), then `FluentWait` (10s timeout, 500ms polling, ignoring `NoSuchElementException`) until `driver.getCurrentUrl().contains("/mesero") || isVisible(driver, ERROR_MESSAGE)`; private helper `isVisible(WebDriver, Target)` returns false on missing/not-displayed element, never throws; zero assertions; zero Questions
- [X] T016 [P] Create `AUTO_FRONT_SCREENPLAY/src/test/java/com/foodtech/automation/screenplay/register/questions/CurrentUrl.java` — implements `Question<String>`; static factory `forThePage()`; implement `answerAs(Actor actor)` returning `BrowseTheWeb.as(actor).getDriver().getCurrentUrl()` (actor-scoped — do NOT use `Serenity.getDriver()`); no waits; no side effects

**Checkpoint**: `./gradlew compileTestJava` succeeds. All 4 Tasks, 1 Question, all support classes, `AuthenticationPageUI`, and the feature file are present and compile cleanly. No step definitions or hooks yet.

---

## Phase 3: User Story 1 — Successful Registration and Automatic Login (Priority: P1) 🎯 MVP

**Goal**: A new user can register and reach the main operational view (`/mesero`) in a single flow with no manual login. Automation confirms the URL change to `/mesero` as the only observable proof of account creation + auto-login + navigation.

**Independent Test**: `./gradlew clean test -Dcucumber.filter.tags="@positiveRegister"` — passes when FoodTech app is running and `POST /api/auth/register` + `POST /api/auth/login` both succeed with unique generated credentials.

### Implementation for User Story 1

- [X] T017 [US1] Create `AUTO_FRONT_SCREENPLAY/src/test/java/com/foodtech/automation/screenplay/stepdefinitions/RegisterHooks.java` — add `@Before("@positiveRegister")` hook: call `OnStage.setTheStage(new OnlineCast())`, generate `RegistrationData data = TestDataFactory.createRegistrationData()`, call `TestContext.setUser(data)`; add `@After("@positiveRegister")` hook: call `TestContext.clear()`, call `OnStage.drawTheCurtainOn()`
- [X] T018 [US1] Create `AUTO_FRONT_SCREENPLAY/src/test/java/com/foodtech/automation/screenplay/stepdefinitions/RegisterStepDefinitions.java` — bind all 6 positive scenario steps: `@Given("the user is on the authentication page with demo mode off")` → `actor.attemptsTo(NavigateToAuthenticationPage.now())`; `@And("the user activates the registration mode")` → `actor.attemptsTo(ActivateRegistrationMode.now())`; `@When("the user provides a valid email, a unique username, and a valid password")` → `actor.attemptsTo(ProvideRegistrationData.with(TestContext.getUser()))`; `@And("the user submits the registration form")` → `actor.attemptsTo(SubmitRegistration.now())`; `@Then("the system creates the account and starts the session automatically")` → `actor.should(seeThat(CurrentUrl.forThePage(), containsString("/mesero")))`; `@And("the user is taken to the main operational view")` → `actor.should(seeThat(CurrentUrl.forThePage(), containsString("/mesero")))`; retrieve actor via `OnStage.theActorInTheSpotlight()`; each method body is a single delegation call — no logic, no conditionals, no direct WebDriver access
- [ ] T019 [US1] Run positive scenario against the running FoodTech app and verify it passes: `./gradlew clean test -Dcucumber.filter.tags="@positiveRegister"`; confirm URL resolves to `/mesero`; confirm `SubmitRegistration` FluentWait resolves on the URL branch (not the error branch)

**Checkpoint**: `@positiveRegister` scenario passes end-to-end. URL contains `/mesero` after `SubmitRegistration` completes. Serenity report step names are readable.

---

## Phase 4: User Story 2 — Failed Automatic Login After Registration Attempt (Priority: P1)

**Goal**: When a user submits registration with an already-registered email but a different password, the system surfaces "Credenciales inválidas" in the DOM and keeps the actor on `/login`. Automation confirms the error text and the URL.

**Independent Test**: `./gradlew clean test -Dcucumber.filter.tags="@negativeRegister"` — passes when FoodTech app is running and the backend correctly returns HTTP 401 on the auto-login step after duplicate registration.

### Implementation for User Story 2

- [X] T020 [P] [US2] Create `AUTO_FRONT_SCREENPLAY/src/test/java/com/foodtech/automation/screenplay/register/questions/VisibleErrorMessage.java` — implements `Question<String>`; static factory `forThePage()`; body: `return ERROR_MESSAGE.resolveFor(actor).getText()`; precondition: element is visible (guaranteed by `SubmitRegistration` for the negative path); no waits; no side effects
- [X] T021 [US2] In `RegisterHooks.java`, add `@Before("@negativeRegister")` hook: call `OnStage.setTheStage(new OnlineCast())`, generate `RegistrationData dataA = TestDataFactory.createRegistrationData()` (uses `generatePassword("A")` internally), call `RegistrationApiClient.register(dataA)` (creates account in backend; throws `IllegalStateException` on failure before browser opens), create `RegistrationData conflicting = new RegistrationData(dataA.email(), dataA.username(), TestDataFactory.generatePassword("B"))` — the salt `"B"` guarantees `conflicting.password()` can NEVER equal `dataA.password()` regardless of execution speed; the username is reused incidentally (backend may accept or reject duplicate username — this is irrelevant because per spec.md Assumption 5 the frontend ignores the registration response and always proceeds to auto-login; the test objective is to confirm auto-login failure when password-B does not match the stored password-A); call `TestContext.setConflictingUser(conflicting)`; add `@After("@negativeRegister")` hook: call `TestContext.clear()`, call `OnStage.drawTheCurtainOn()`
- [X] T022 [US2] In `RegisterStepDefinitions.java`, add bindings for all 6 negative scenario steps: `@When("the user provides an email already registered with a different password")` → `actor.attemptsTo(ProvideRegistrationData.with(TestContext.getConflictingUser()))`; `@Then("the system displays an invalid credentials message")` → `actor.should(seeThat(VisibleErrorMessage.forThePage(), equalTo("Credenciales inválidas")))`; `@And("the user remains on the authentication page")` → `actor.should(seeThat(CurrentUrl.forThePage(), containsString("/login")))`; re-use step bindings shared with US1 for `@Given`, `@And("the user activates")`, `@And("the user submits")` — each method body is a single delegation call
- [ ] T023 [US2] Run negative scenario against the running FoodTech app and verify it passes: `./gradlew clean test -Dcucumber.filter.tags="@negativeRegister"`; confirm `VisibleErrorMessage` returns `"Credenciales inválidas"`; confirm `CurrentUrl` returns a URL containing `/login`; confirm `SubmitRegistration` FluentWait resolves on the `ERROR_MESSAGE` branch (not the URL branch)

**Checkpoint**: `@negativeRegister` scenario passes end-to-end. Error message equals `"Credenciales inválidas"`. URL still contains `/login`. `RegistrationApiClient` pre-setup succeeded before browser opened.

---

## Phase 5: Polish & Cross-Cutting Concerns

**Purpose**: Full suite validation, Serenity report confirmation, Screenplay SRP compliance review, and data isolation verification.

- [ ] T024 Run both scenarios together and verify all pass: `./gradlew clean test`
- [ ] T025 [P] Generate Serenity aggregate report and verify both scenario names appear with step-level evidence: `./gradlew aggregate` → open `AUTO_FRONT_SCREENPLAY/target/site/serenity/index.html`; confirm `@positiveRegister` and `@negativeRegister` scenarios are listed with readable step names
- [ ] T026 [P] Review all Task classes (`NavigateToAuthenticationPage`, `ActivateRegistrationMode`, `ProvideRegistrationData`, `SubmitRegistration`) — confirm zero assertions, zero Question invocations, zero direct WebDriver calls (Constitution §7 SRP)
- [ ] T027 [P] Review all Question classes (`CurrentUrl`, `VisibleErrorMessage`) — confirm zero waits, zero state changes, zero side effects; confirm `RegistrationApiClient` is referenced only in `RegisterHooks`, never in any Task or Question (Constitution §7 SRP)
- [ ] T028 [P] Review `RegisterStepDefinitions.java` — confirm every `@Given`/`@When`/`@Then`/`@And` method body is exactly one delegation call (`actor.attemptsTo(...)` or `actor.should(...)`); confirm no logic, no conditionals, no direct WebDriver access (Constitution §7 + §10)

**Checkpoint**: `./gradlew clean test aggregate` succeeds. Both scenarios pass. Serenity HTML report includes both scenarios with step evidence. All Constitution §7 gates confirmed.

---

## Dependencies & Execution Order

### Phase Dependencies

```
Phase 1 (Setup)
    └── Phase 2 (Foundation)  ← BLOCKS both user stories
            ├── Phase 3 (US1 — Positive)  ← independently testable when done
            │       └── Phase 4 (US2 — Negative)  ← adds VisibleErrorMessage + negative hooks/bindings
            │               └── Phase 5 (Polish)
```

### User Story Dependencies

| Story | Depends On | Shared Components from Foundation | Can Be Tested Independently After |
|---|---|---|---|
| US1 — Positive Registration + Auto-Login | Phase 2 complete | All 4 Tasks, `CurrentUrl`, `AuthenticationPageUI`, `TestDataFactory`, `TestContext` | Phase 3 complete |
| US2 — Failed Auto-Login After Registration | Phase 2 + Phase 3 complete | All 4 Tasks, `CurrentUrl`, `RegistrationApiClient`; adds `VisibleErrorMessage` | Phase 4 complete |

### Parallel Opportunities Per Phase

**Phase 1**: T002, T003 can run in parallel after T001
**Phase 2**: T005–T010 can all run in parallel (independent files); T011–T015 require T005 complete; T016 can run in parallel with T012–T015
**Phase 3**: T017 and T018 can be developed in parallel; T019 requires both
**Phase 4**: T020 can run in parallel with T021; T022 requires T020 and T021; T023 requires T022
**Phase 5**: T025–T028 can all run in parallel after T024

### Within Each User Story

- Tasks before Questions (Questions depend on Tasks establishing terminal state)
- Hooks before StepDefinitions (StepDefinitions reference `TestContext` populated by hooks)
- Feature file before StepDefinitions (step text must match verbatim)
- Validation (`./gradlew clean test -Dtag`) last in each phase

---

## Summary

| Metric | Value |
|---|---|
| Total tasks | 28 |
| Phase 1 — Setup | 4 tasks (T001–T004) |
| Phase 2 — Foundation | 12 tasks (T005–T016) |
| Phase 3 — US1 Positive | 3 tasks (T017–T019) |
| Phase 4 — US2 Negative | 4 tasks (T020–T023) |
| Phase 5 — Polish | 5 tasks (T024–T028) |
| Parallelizable tasks [P] | 18 |
| Gherkin steps mapped | 9 (all steps in both scenarios) |
| Questions | 2 (`CurrentUrl`, `VisibleErrorMessage`) |
| Tasks (Screenplay) | 4 (`NavigateToAuthenticationPage`, `ActivateRegistrationMode`, `ProvideRegistrationData`, `SubmitRegistration`) |
| Suggested MVP scope | US1 only (Phases 1–3): register → auto-login → `/mesero` redirect |
