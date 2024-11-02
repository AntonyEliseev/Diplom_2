package steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import client.OrderClient;
import pojo.Ingredients;
import java.util.ArrayList;
import java.util.List;

public class OrderSteps {
    private static final String INVALID_HASH_INGREDIENT = "not0valid1hash2";
    private OrderClient orderClient;

    @Step("Создание клиента для заказа")
    public void createOrderClient() {
        orderClient = new OrderClient();
    }
    @Step("Получение 0 ингредиентов")
    public List<String> getNullIngredients() {
        return new ArrayList<>();
    }
    @Step("Получение 2 ингредиентов")
    public List<String> getTwoIngredients() {
        List<String> twoIngredients = orderClient.getIngredientClient().extract().jsonPath().getList("data._id");
        return twoIngredients.subList(0,2);
    }
    @Step("Получение ингредиентов с неправильным hash")
    public List<String> getInvalidHashIngredient() {
        List<String> invalidIngredient = new ArrayList<>();
        invalidIngredient.add(INVALID_HASH_INGREDIENT);
        return invalidIngredient;
    }
    @Step("Создание заказа с авторизацией")
    public ValidatableResponse createNewOrderAuth(List<String> ingredients, String accessToken) {
        Ingredients newIngredients = new Ingredients(ingredients);
        return orderClient.createOrderClientAuth(newIngredients, accessToken);
    }
    @Step("Создание заказа без авторизации")
    public ValidatableResponse createNewOrderWithoutAuth(List<String> ingredients) {
        Ingredients newIngredients = new Ingredients(ingredients);
        return orderClient.createOrderClientWithoutAuth(newIngredients);
    }
}