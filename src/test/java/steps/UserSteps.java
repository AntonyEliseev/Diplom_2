package steps;

import com.github.javafaker.Faker;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import client.UserClient;
import pojo.User;
import pojo.UserCreds;
import pojo.UserData;
import static client.UserTestData.*;

public class UserSteps {
    private Faker faker = new Faker();
    private UserClient userClient;
    private User user;
    public String accessToken;
    public String fakerName;
    public String fakerEmail;

    @Step("Создание клиента пользователя")
    public void createUserClient() {
        userClient = new UserClient();
    }
    @Step("Создание пользователя")
    public ValidatableResponse createUser() {
        user = new User(EMAIL, PASSWORD, NAME);
        return userClient.createUserClient(user);
    }
    @Step("Создание пользователя без обязательного поля")
    public ValidatableResponse createInvalidUser() {
        user = new User(EMAIL, PASSWORD, NAME);
        user.setEmail(null);
        return userClient.createUserClient(user);
    }
    @Step("Создание несуществующего пользователя")
    public void createUnknownUser() {
        fakerEmail = faker.internet().emailAddress();
        String fakerPassword = faker.internet().password();
        user = new User(fakerEmail, fakerPassword, NAME);
    }
    @Step("Получение токена")
    public String getAccessToken(ValidatableResponse response) {
        return response.extract().jsonPath().getString("accessToken");
    }
    @Step("Авторизация пользователя в системе")
    public ValidatableResponse login() {
        return userClient.loginUserClient(UserCreds.from(user));
    }

    @Step("Изменение пользователя с авторизацией")
    public ValidatableResponse editAuth() {
        fakerEmail = faker.internet().emailAddress();
        fakerName = faker.name().firstName();
        UserData userData = new UserData(fakerEmail, fakerName);
        return userClient.editUserClientAuth(userData, accessToken);
    }
    @Step("Изменение пользователя без авторизации")
    public ValidatableResponse editWithoutAuth() {
        fakerEmail = faker.internet().emailAddress();
        fakerName = faker.name().firstName();
        UserData userData = new UserData(fakerEmail, fakerName);
        return userClient.editUserClientWithoutAuth(userData);
    }
    @Step("Удаление пользователя")
    public void deleteUser() {
        if (accessToken != null) {
            userClient.deleteUserClient(accessToken);
        }
    }
}