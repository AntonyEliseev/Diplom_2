import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.GeneralSteps;
import steps.UserSteps;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static client.UserTestData.*;

@DisplayName("Создание пользователя, api/auth/register")
public class CreateUserTest {
    private static final String FORBIDDEN_USER_EXPECTED_MESSAGE = "User already exists";
    private static final String FORBIDDEN_MISS_FIELD_EXPECTED_MESSAGE = "Email, password and name are required fields";
    private GeneralSteps generalSteps = new GeneralSteps();
    private UserSteps userSteps = new UserSteps();
    @Before
    public void setUp() {
        generalSteps.enableLogs();
        userSteps.createUserClient();
    }
    @Test
    @DisplayName("Создание пользователя")
    public void createNewUser() {
        ValidatableResponse response = userSteps.createUser().assertThat().body("success", equalTo(true))
                .body("user.email", equalTo(EMAIL))
                .body("user.name", equalTo(NAME))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue())
                .and()
                .statusCode(SC_OK);
        userSteps.accessToken = userSteps.getAccessToken(response);
    }
    @Test
    @DisplayName("Создание ранее зарегистрированного пользователя")
    public void createSameUser() {
        ValidatableResponse response = userSteps.createUser();
        userSteps.accessToken = userSteps.getAccessToken(response);
        userSteps.createUser().assertThat().body("success", equalTo(false))
                .body("message",  equalTo(FORBIDDEN_USER_EXPECTED_MESSAGE))
                .and()
                .statusCode(SC_FORBIDDEN);
    }
    @Test
    @DisplayName("Создание пользователя без обязательного поля")
    public void createUserMissField() {
        ValidatableResponse response = userSteps.createInvalidUser().assertThat().body("success", equalTo(false))
                .body("message",  equalTo(FORBIDDEN_MISS_FIELD_EXPECTED_MESSAGE))
                .and()
                .statusCode(SC_FORBIDDEN);
        userSteps.accessToken = userSteps.getAccessToken(response);
    }
    @After
    public void deleteTestDataAndTurnOffLogs() {
        userSteps.deleteUser();
        generalSteps.disableLogs();
    }
}