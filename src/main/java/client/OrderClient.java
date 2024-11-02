package client;

import io.restassured.response.ValidatableResponse;
import pojo.Ingredients;
import static io.restassured.RestAssured.given;

public class OrderClient extends Specification {
    private static final String GET_INGREDIENTS_API = "/api/ingredients";
    private static final String CREATE_ORDER_API = "/api/orders";
    private static final String AUTHORIZATION = "Authorization";

    public ValidatableResponse getIngredientClient() {
        return given()
                .spec(getSpec())
                .get(GET_INGREDIENTS_API)
                .then();
    }
    public ValidatableResponse createOrderClientAuth(Ingredients ingredients, String accessToken) {
        return given()
                .spec(getSpec())
                .header(AUTHORIZATION,accessToken)
                .body(ingredients)
                .when()
                .post(CREATE_ORDER_API)
                .then();
    }
    public ValidatableResponse createOrderClientWithoutAuth(Ingredients ingredients) {
        return given()
                .spec(getSpec())
                .body(ingredients)
                .when()
                .post(CREATE_ORDER_API)
                .then();
    }
}