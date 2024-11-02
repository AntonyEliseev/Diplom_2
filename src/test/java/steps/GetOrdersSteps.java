package steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import client.GetOrdersClient;

public class GetOrdersSteps {
    private GetOrdersClient getOrdersClient;
    @Step("Создание клиента для получения заказов")
    public void createGetOrdersClient() {
        getOrdersClient = new GetOrdersClient();
    }
    @Step("Получение заказов авторизованного клиента")
    public ValidatableResponse getOrdersWithAuth(String accessToken) {
        return getOrdersClient.getOrdersClientAuth(accessToken);
    }
    @Step("Получение заказов неавторизованного клиента")
    public ValidatableResponse getOrdersWithoutAuth() {
        return getOrdersClient.getOrdersClientWithoutAuth();
    }
}