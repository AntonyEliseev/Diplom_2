import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.GeneralSteps;
import steps.GetOrdersSteps;
import steps.OrderSteps;
import steps.UserSteps;
import java.util.List;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

@DisplayName("Получение заказов пользователя, api/orders")
public class GetOrdersTest {
    private static final String UNAUTHORIZED_EXPECTED_MESSAGE = "You should be authorised";
    private GeneralSteps generalSteps = new GeneralSteps();
    private UserSteps userSteps = new UserSteps();
    private OrderSteps orderSteps = new OrderSteps();
    private GetOrdersSteps getOrdersSteps = new GetOrdersSteps();
    @Before
    public void setUp() {
        generalSteps.enableLogs();
        orderSteps.createOrderClient();
        getOrdersSteps.createGetOrdersClient();
    }
    @Test
    @DisplayName("Получение заказов пользователя с авторизацией")
    public void getOrdersAuth() {
        userSteps.createUserClient();
        userSteps.createUser();
        ValidatableResponse response = userSteps.login();
        userSteps.accessToken = userSteps.getAccessToken(response);
        List<String> twoIngredients = orderSteps.getTwoIngredients();
        orderSteps.createNewOrderAuth(twoIngredients, userSteps.accessToken);
        getOrdersSteps.getOrdersWithAuth(userSteps.accessToken).assertThat().body("success", equalTo(true))
                .body("orders", notNullValue())
                .body("total",notNullValue())
                .body("totalToday",notNullValue())
                .and()
                .statusCode(SC_OK);
    }
    @Test
    @DisplayName("Получение заказов пользователя без авторизации")
    public void getOrdersNotAuth() {
        userSteps.createUserClient();
        userSteps.createUser();
        ValidatableResponse response = userSteps.login();
        userSteps.accessToken = userSteps.getAccessToken(response);
        List<String> twoIngredients = orderSteps.getTwoIngredients();
        orderSteps.createNewOrderAuth(twoIngredients, userSteps.accessToken);
        getOrdersSteps.getOrdersWithoutAuth().assertThat().body("success", equalTo(false))
                .body("message",  equalTo(UNAUTHORIZED_EXPECTED_MESSAGE))
                .and()
                .statusCode(SC_UNAUTHORIZED);
    }
    @After
    public void deleteTestDataAndTurnOffLogs() {
        userSteps.deleteUser();
        generalSteps.disableLogs();
    }
}