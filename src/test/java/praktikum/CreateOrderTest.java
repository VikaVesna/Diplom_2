package praktikum;

import io.qameta.allure.internal.shadowed.jackson.core.JsonProcessingException;
import io.restassured.response.ValidatableResponse;
import order.OrderChecks;
import order.OrderClient;
import order.CreateNewOrder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;
import static java.net.HttpURLConnection.*;

import java.util.ArrayList;


public class CreateOrderTest {

    private OrderChecks orderCheck = new OrderChecks();
    private OrderClient orderClient = new OrderClient();
    private UserClient userClient = new UserClient();
    private UserChecks userCheck = new UserChecks();
    private User user;
    private String accessToken;
    private ArrayList<String> ingredientsIds;
    private String[] ingredients;

    @Before
    @DisplayName("Создание пользователя перед тестом")
    public void setUp() {
        user = User.randomUser();
        ValidatableResponse response = userClient.createUser(user);
        userCheck.checkCreated(response);

        accessToken = response.extract().path("accessToken");

        ingredientsIds = orderClient.getIngredientsIds();
    }

    @After
    @DisplayName("Удаление пользователя после теста")
    public void tearDown() {
        if (accessToken != null && !accessToken.isEmpty()) {
            userClient.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Успешное создание заказа с ингредиентами авторизованного пользователя")
    public void successfulOrderCreationWithIngredientsForAuthorizedUser() throws JsonProcessingException {
        ingredients = new String[]{ingredientsIds.get(0), ingredientsIds.get(1), ingredientsIds.get(2)};
        CreateNewOrder order = new CreateNewOrder(ingredients);
        ValidatableResponse orderResponse = orderClient.createOrder(order, accessToken, HTTP_OK);

        orderCheck.checkCreateOrder(orderResponse);
    }

    @Test
    @DisplayName("Система вернет ошибку при создании заказа без ингредиентов авторизованного пользователя")
    public void orderCreationWithoutIngredientsForAuthorizedUserTest() throws JsonProcessingException {
        ingredients = new String[]{};
        CreateNewOrder order = new CreateNewOrder(ingredients);
        ValidatableResponse createOrderWithoutIngredientsResponse = orderClient.createOrder(order, accessToken, HTTP_BAD_REQUEST);

        orderCheck.checkCreateOrderWithoutIngredients(createOrderWithoutIngredientsResponse);
    }

    @Test
    @DisplayName("Система вернет ошибку при создании заказа с неверным хешем ингредиентов авторизованного пользователя")
    public void orderCreationWithInvalidHashForAuthorizedUserTest() throws JsonProcessingException {
        ingredients = new String[]{"61c0c5a71d1f82001bdbab7t"};
        CreateNewOrder order = new CreateNewOrder(ingredients);
        ValidatableResponse createOrderWithIncorrectHashResponse = orderClient.createOrder(order, accessToken, HTTP_INTERNAL_ERROR);

        orderCheck.checkCreateOrderWithIncorrectHash(createOrderWithIncorrectHashResponse);
    }


    @Test
    @DisplayName("Система вернет ошибку при создании заказа с ингредиентами не авторизованного пользователя")
    public void orderCreationWithIngredientsForUnauthorizedUserTest() throws JsonProcessingException {
        ingredients = new String[]{ingredientsIds.get(0), ingredientsIds.get(1), ingredientsIds.get(2)};
        CreateNewOrder order = new CreateNewOrder(ingredients);
        ValidatableResponse orderWithoutAuthResponse = orderClient.createOrder(order, "", HTTP_MOVED_TEMP);

        orderCheck.checkCreateOrderWithoutAuth(orderWithoutAuthResponse);
    }

    @Test
    @DisplayName("Система вернет ошибку при создании заказа без ингредиентов не авторизованного пользователя")
    public void orderCreationWithoutIngredientsForUnauthorizedUserTest() throws JsonProcessingException {
        ingredients = new String[]{};
        CreateNewOrder order = new CreateNewOrder(ingredients);
        ValidatableResponse orderWithoutAuthResponse = orderClient.createOrder(order, "", HTTP_BAD_REQUEST);

        orderCheck.checkCreateOrderWithoutIngredients(orderWithoutAuthResponse);
    }

}