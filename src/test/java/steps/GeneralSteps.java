package steps;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;

public class GeneralSteps {
    @Step("Включение логирования")
    public void enableLogs() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }
    @Step("Оключение логирования")
    public void disableLogs() {
        RestAssured.reset();
    }
}