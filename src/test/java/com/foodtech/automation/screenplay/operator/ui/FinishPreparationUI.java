package com.foodtech.automation.screenplay.operator.ui;

import net.serenitybdd.screenplay.targets.Target;

public class FinishPreparationUI {

    public static final Target COMPLETE_TASK_BUTTON =
            Target.the("complete task button").locatedBy("[data-testid^='complete-task-btn-']");

    public static final Target KITCHEN_ERROR_BANNER =
            Target.the("kitchen error banner").locatedBy("[data-testid='kitchen-error']");
}
