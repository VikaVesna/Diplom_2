package order;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import java.util.Map;

import static java.net.HttpURLConnection.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class OrderChecks {

    @Step("Успешное создание заказа")
    public void checkCreateOrder(ValidatableResponse createOrderResponse) {
        int number = createOrderResponse
                .assertThat()
                .statusCode(HTTP_OK)
                .extract()
                .path("order.number");
        assertNotEquals(0, number);
    }

    @Step("Ошибка при создании заказа без ингредиентов")
    public void checkCreateOrderWithoutIngredients(ValidatableResponse createOrderWithoutIngredientsResponse) {
        var body = createOrderWithoutIngredientsResponse
                .assertThat()
                .statusCode(HTTP_BAD_REQUEST)
                .extract()
                .body().as(Map.class);
        assertEquals("Неверное сообщение об ошибке", "Ingredient ids must be provided", body.get("message"));
    }

    @Step("Ошибка при создании заказа с невалидным хешем ингредиентов")
    public void checkCreateOrderWithIncorrectHash(ValidatableResponse createOrderWithIncorrectHashResponse) {
        var body = createOrderWithIncorrectHashResponse
                .assertThat()
                .statusCode(HTTP_INTERNAL_ERROR);
    }

    @Step("Ошибка при создании заказа неавторизованным пользователем")
    public void checkCreateOrderWithoutAuth(ValidatableResponse createOrderResponse) {
        var body = createOrderResponse
                .assertThat()
                .statusCode(HTTP_MOVED_TEMP);
    }

    @Step("Получение списка заказов авторизованного пользователя")
    public void checkGetOrderListAuthorized(ValidatableResponse getOrderListResponse) {
        int total = getOrderListResponse
                .assertThat()
                .statusCode(HTTP_OK)
                .extract()
                .path("total");
        assertNotEquals(0, total);
    }

    @Step("Получение списка заказов неавторизованного пользователя")
    public void checkGetOrderListUnauthorized(ValidatableResponse getOrderListResponse) {
        var body = getOrderListResponse
                .assertThat()
                .statusCode(HTTP_UNAUTHORIZED);
    }

}















