# AUTO_FRONT_SCREENPLAY

Frontend UI automation project using the **Screenplay** pattern.

## Stack

- Serenity BDD
- Cucumber (BDD)
- Selenium WebDriver (via Serenity)
- Gradle
- Java 21

## Project Structure

```
src/
  test/
    java/       ← Actors, Tasks, Interactions, Questions, Step definitions
    resources/  ← serenity.conf, cucumber options
features/       ← Gherkin feature files (UI scenarios)
```

## Workflow

This project follows **Spec-Driven Development** using [Spec-Kit](https://github.com/github/spec-kit).

Branch strategy: `main` → `develop` → `feature/*`
