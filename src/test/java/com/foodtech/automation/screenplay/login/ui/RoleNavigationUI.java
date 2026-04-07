package com.foodtech.automation.screenplay.login.ui;

import net.serenitybdd.screenplay.targets.Target;

public class RoleNavigationUI {

    public static final Target NAV_MESERO =
            Target.the("mesero nav link").locatedBy("[data-testid='nav-mesero']");

    public static final Target NAV_BARRA =
            Target.the("barra nav link").locatedBy("[data-testid='nav-barra']");

    public static final Target NAV_COCINA =
            Target.the("cocina nav link").locatedBy("[data-testid='nav-cocina']");

    public static final Target TAB_PENDING =
            Target.the("pending tab").locatedBy("[data-testid='tab-pending']");

    public static final Target TAB_IN_PREPARATION =
            Target.the("in preparation tab").locatedBy("[data-testid='tab-in-preparation']");

    public static final Target TAB_COMPLETED =
            Target.the("completed tab").locatedBy("[data-testid='tab-completed']");
}
