package order;

import io.qameta.allure.Param;
import io.qameta.allure.internal.shadowed.jackson.core.JsonProcessingException;
import io.qameta.allure.internal.shadowed.jackson.databind.ObjectMapper;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import main.Client;

import java.util.ArrayList;

import static io.qameta.allure.model.Parameter.Mode.HIDDEN;

public class OrderClient extends Client {

    private static final String INGREDIENTS_PATH = "ingredients";
    private static final String ORDER_PATH = "orders";
    private static String json;
    static ObjectMapper mapper = new ObjectMapper();


    @Step("Получение данных ингредиентов")
    public ValidatableResponse getDataOfIngredients () {
        return spec()
                .when()
                .get(INGREDIENTS_PATH)
                .then().log().all();
    }


    @Step("Получение хэшей ингрединтов")
    public ArrayList<String> getIngredientsIds() {
         ValidatableResponse response = getDataOfIngredients();
         ArrayList<String> ingredientsIds = new ArrayList<>(response
                .extract()
                .path("data._id"));
        return ingredientsIds;
    }


    @Step("Создание нового заказа")
    public ValidatableResponse createOrder (CreateNewOrder order, @Param(mode=HIDDEN)String accessToken, int statusCode) throws JsonProcessingException {
        json = mapper.writeValueAsString(order);
        return spec()
                .header("Authorization", accessToken)
                .body(json)
                .when()
                .post(ORDER_PATH)
                .then().log().all();
    }


    @Step("Получение списка заказов для авторизованного пользователя")
    public ValidatableResponse getOrderList (@Param(mode=HIDDEN)String accessToken) {
        return spec()
                .header("Authorization", accessToken)
                .when()
                .get(ORDER_PATH)
                .then().log().all();
    }

    @Step("Получение списка заказов для не авторизованного пользователя")
    public ValidatableResponse getOrderListUnauthorized () {
        return spec()
                .when()
                .get(ORDER_PATH)
                .then().log().all();
    }
}