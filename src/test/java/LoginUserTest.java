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
import static client.UserTestData.EMAIL;
import static client.UserTestData.NAME;

@DisplayName("Логин пользователя, api/auth/login")
public class LoginUserTest {
    private static final String UNAUTHORIZED_EXPECTED_MESSAGE = "email or password are incorrect";
    private GeneralSteps generalSteps = new GeneralSteps();
    private UserSteps userSteps = new UserSteps();
    @Before
    public void setUp() {
        generalSteps.enableLogs();
        userSteps.createUserClient();
    }
    @Test
    @DisplayName("Логин под существующим пользователем")
    public void loginUser() {
        userSteps.createUser();
        ValidatableResponse response = userSteps.login().assertThat().body("success", equalTo(true))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue())
                .body("user.email", equalTo(EMAIL))
                .body("user.name", equalTo(NAME))
                .and()
                .statusCode(SC_OK);
        userSteps.accessToken = userSteps.getAccessToken(response);
    }
    @Test
    @DisplayName("Логин с неверными кредами")
    public void loginInvalidUser() {
        userSteps.createUnknownUser();
        ValidatableResponse response = userSteps.login().assertThat().body("success", equalTo(false))
                .body("message", equalTo(UNAUTHORIZED_EXPECTED_MESSAGE))
                .and()
                .statusCode(SC_UNAUTHORIZED);
        userSteps.accessToken = userSteps.getAccessToken(response);
    }
    @After
    public void deleteTestDataAndTurnOffLogs() {
        userSteps.deleteUser();
        generalSteps.disableLogs();
    }
}