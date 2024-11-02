package client;

import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;

public class GetOrdersClient extends Specification {
    private static final String GET_ORDERS_API = "/api/orders";
    private static final String AUTHORIZATION = "Authorization";

    public ValidatableResponse getOrdersClientAuth(String accessToken) {
        return given()
                .spec(getSpec())
                .header(AUTHORIZATION,accessToken)
                .get(GET_ORDERS_API)
                .then();
    }
    public ValidatableResponse getOrdersClientWithoutAuth() {
        return given()
                .spec(getSpec())
                .get(GET_ORDERS_API)
                .then();
    }
}