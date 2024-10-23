package praktikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import order.CreateNewOrder;
import order.OrderChecks;
import order.OrderClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static java.net.HttpURLConnection.*;

public class GetOrderListTest {

    private OrderChecks orderCheck = new OrderChecks();
    private OrderClient orderClient = new OrderClient();
    private UserClient userClient = new UserClient();
    private UserChecks userCheck = new UserChecks();
    private User user;
    private String accessToken;
    private ArrayList<String> ingredientsIds;

    @Before
    @DisplayName("Создание пользователя и заказа перед тестом")
    public void setUp() throws Exception {
        user = User.randomUser();
        ValidatableResponse response = userClient.createUser(user);
        userCheck.checkCreated(response);

        accessToken = response.extract().path("accessToken");

        ingredientsIds = orderClient.getIngredientsIds();

        CreateNewOrder order = new CreateNewOrder(new String[]{ingredientsIds.get(0), ingredientsIds.get(1), ingredientsIds.get(2)});
        orderClient.createOrder(order, accessToken, HTTP_OK);
    }

    @After
    @DisplayName("Удаление пользователя после теста")
    public void tearDown() {
        if (accessToken != null && !accessToken.isEmpty()) {
            userClient.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Авторизованный пользователь может получить список заказов")
    public void getOrderListAuthorizedUserTest() {
        ValidatableResponse getOrderListResponse = orderClient.getOrderList(accessToken);

        orderCheck.checkGetOrderListAuthorized(getOrderListResponse);
    }

    @Test
    @DisplayName("Не авторизованный пользователь не может получить список заказов")
    public void getOrderListUnauthorizedUserTest() {
        ValidatableResponse getOrderListResponse = orderClient.getOrderListUnauthorized();

        orderCheck.checkGetOrderListUnauthorized(getOrderListResponse);
    }

}