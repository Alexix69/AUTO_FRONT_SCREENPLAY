package com.foodtech.automation.screenplay.register.ui;

import net.serenitybdd.screenplay.targets.Target;

public class AuthenticationPageUI {

    public static final Target SUBMIT_BUTTON =
            Target.the("submit button").locatedBy("[data-testid='submit-btn']");

    public static final Target TOGGLE_MODE_BUTTON =
            Target.the("toggle mode button").locatedBy("[data-testid='toggle-mode-btn']");

    public static final Target USERNAME_INPUT =
            Target.the("username input").locatedBy("[data-testid='username-input']");

    public static final Target EMAIL_INPUT =
            Target.the("email input").locatedBy("[data-testid='email-input']");

    public static final Target PASSWORD_INPUT =
            Target.the("password input").locatedBy("[data-testid='password-input']");

    public static final Target ERROR_MESSAGE =
            Target.the("error message").locatedBy("[data-testid='error-message']");

    public static final Target DEMO_MODE_CHECKBOX =
            Target.the("demo mode checkbox").locatedBy("[data-testid='demo-mode-checkbox']");
}
