package com.foodtech.automation.screenplay.login.ui;

import net.serenitybdd.screenplay.targets.Target;

public class AccessDeniedPageUI {

    public static final Target REGRESAR_BUTTON =
            Target.the("regresar button").locatedBy("[data-testid='regresar-btn']");
}
