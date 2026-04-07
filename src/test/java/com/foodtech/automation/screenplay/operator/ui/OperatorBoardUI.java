package com.foodtech.automation.screenplay.operator.ui;

import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;

public class OperatorBoardUI {

    public static final Target TAB_PENDING =
            Target.the("pending tab").locatedBy("[data-testid='tab-pending']");

    public static final Target TAB_IN_PREPARATION =
            Target.the("in preparation tab").locatedBy("[data-testid='tab-in-preparation']");

    public static final Target TAB_COMPLETED =
            Target.the("completed tab").locatedBy("[data-testid='tab-completed']");

    public static final Target TASK_CARD =
            Target.the("task card").locatedBy("[data-testid^='task-card-']");

    public static final Target TASK_PRODUCT_ROW =
            Target.the("task product row").locatedBy("[data-testid^='task-product-']");

    public static final Target EMPTY_STATE =
            Target.the("empty state message").locatedBy("[data-testid='empty-tasks-message']");

    public static final Target TODAS_LAS_TAREAS_BUTTON =
            Target.the("todas las tareas button").located(By.xpath("//button[normalize-space()='Todas las tareas']"));
}
