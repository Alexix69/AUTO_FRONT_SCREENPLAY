package com.foodtech.automation.screenplay.runners;

import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;
import io.cucumber.junit.CucumberOptions;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
	features = "classpath:features/register",
	glue = "com.foodtech.automation.screenplay.stepdefinitions",
	plugin = {"pretty"}
)
public class RegisterTestRunner {
}
