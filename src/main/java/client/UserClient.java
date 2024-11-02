package client;

import io.restassured.response.ValidatableResponse;
import pojo.User;
import pojo.UserCreds;
import pojo.UserData;
import static io.restassured.RestAssured.given;

public class UserClient extends Specification {
    private static final String CREATE_USER_API = "/api/auth/register";
    private static final String DELETE_USER_API = "/api/auth/user";
    private static final String LOGIN_USER_API = "/api/auth/login";
    private static final String EDIT_USER_API = "/api/auth/user";
    private static final String AUTHORIZATION = "Authorization";

    public ValidatableResponse createUserClient(User user) {
        return given()
                .spec(getSpec())
                .body(user)
                .when()
                .post(CREATE_USER_API)
                .then();
    }
    public ValidatableResponse loginUserClient(UserCreds userCreds) {
        return given()
                .spec(getSpec())
                .body(userCreds)
                .when()
                .post(LOGIN_USER_API)
                .then();
    }
    public ValidatableResponse editUserClientAuth(UserData userData, String accessToken) {
        return given()
                .spec(getSpec())
                .header(AUTHORIZATION,accessToken)
                .body(userData)
                .when()
                .patch(EDIT_USER_API)
                .then();
    }
    public ValidatableResponse editUserClientWithoutAuth(UserData userData) {
        return given()
                .spec(getSpec())
                .body(userData)
                .when()
                .patch(EDIT_USER_API)
                .then();
    }
    public void deleteUserClient(String accessToken) {
        given()
                .spec(getSpec())
                .header(AUTHORIZATION,accessToken)
                .delete(DELETE_USER_API)
                .then();
    }
}