import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.GeneralSteps;
import steps.OrderSteps;
import steps.UserSteps;
import java.util.List;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.*;

@DisplayName("Создание заказа, api/orders")
public class CreateOrderTest {
    private static final String BAD_REQUEST_EXPECTED_MESSAGE = "Ingredient ids must be provided";
    private GeneralSteps generalSteps = new GeneralSteps();
    private UserSteps userSteps = new UserSteps();
    private OrderSteps orderSteps = new OrderSteps();

    @Before
    public void setUp() {
        generalSteps.enableLogs();
        orderSteps.createOrderClient();
    }
    @Test
    @DisplayName("Создание заказа с авторизацией и ингредиентами")
    public void createNewOrderAuth() {
        userSteps.createUserClient();
        userSteps.createUser();
        ValidatableResponse response = userSteps.login();
        userSteps.accessToken = userSteps.getAccessToken(response);
        List<String> twoIngredients =  orderSteps.getTwoIngredients();
        orderSteps.createNewOrderAuth(twoIngredients, userSteps.accessToken).assertThat().body("success", equalTo(true))
                .body("name", notNullValue())
                .body("order",notNullValue())
                .and()
                .statusCode(SC_OK);
    }
    @Test
    @DisplayName("Создание заказа с без авторизации и ингредиентами")
    public void createNewOrderWithoutAuth() {
        List<String> twoIngredients =  orderSteps.getTwoIngredients();
        orderSteps.createNewOrderWithoutAuth(twoIngredients).assertThat().body("success", equalTo(true))
                .body("name", notNullValue())
                .body("order",notNullValue())
                .and()
                .statusCode(SC_OK);
    }
    @Test
    @DisplayName("Создание заказа без ингредиентов")
    public void createNewOrderNullIngredients() {
        List<String> nullIngredients =  orderSteps.getNullIngredients();
        orderSteps.createNewOrderWithoutAuth(nullIngredients).assertThat().body("success", equalTo(false))
                .body("message",  equalTo(BAD_REQUEST_EXPECTED_MESSAGE))
                .and()
                .statusCode(SC_BAD_REQUEST);
    }
    @Test
    @DisplayName("Создание заказа с неверным хешем")
    public void createNewOrderInvalidHashIngredients() {
        List<String> invalidIngredient =  orderSteps.getInvalidHashIngredient();
        orderSteps.createNewOrderWithoutAuth(invalidIngredient).statusCode(SC_INTERNAL_SERVER_ERROR);
    }
    @After
    public void deleteTestDataAndTurnOffLogs() {
        userSteps.deleteUser();
        generalSteps.disableLogs();
    }
}