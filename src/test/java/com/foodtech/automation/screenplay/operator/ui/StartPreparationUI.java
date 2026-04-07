package com.foodtech.automation.screenplay.operator.ui;

import net.serenitybdd.screenplay.targets.Target;

public class StartPreparationUI {

    public static final Target START_TASK_BUTTON =
            Target.the("start task button").locatedBy("[data-testid^='start-task-btn-']");

    public static final Target ERROR_BANNER =
            Target.the("error banner").locatedBy("[data-testid='bar-error']");
}
