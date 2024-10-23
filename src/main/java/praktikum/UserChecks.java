package praktikum;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import java.util.Map;

import static java.net.HttpURLConnection.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UserChecks {

    @Step("Проверка успешного создания уникального пользователя")
    public void checkCreated(ValidatableResponse response) {
        boolean created = response
                .assertThat()
                .statusCode(HTTP_OK)
                .extract()
                .path("success");
        assertTrue("Пользователь не создан", created);
    }

    @Step("Ошибка при создании пользователя, который уже зарегистрирован")
    public void checkDuplicateRequest(ValidatableResponse response) {
        var body = response
                .assertThat()
                .statusCode(HTTP_FORBIDDEN)
                .extract()
                .body().as(Map.class);
        assertEquals("Неверное сообщение об ошибке", "User already exists", body.get("message"));
    }

    @Step("Ошибка при создании пользователя без заполнения одного из полей")
    public void checkFailed(ValidatableResponse response) {
        var body = response
                .assertThat()
                .statusCode(HTTP_FORBIDDEN)
                .extract()
                .body().as(Map.class);
        assertEquals("Неверное сообщение об ошибке", "Email, password and name are required fields", body.get("message"));
    }

    @Step("Успешная авторизация пользователя")
    public void checkLoggedIn(ValidatableResponse loginResponse) {
        boolean loggedIn = loginResponse
                .assertThat()
                .statusCode(HTTP_OK)
                .extract()
                .path("success");
        assertTrue("Пользователь не авторизован", loggedIn);
    }


    @Step("Ошибка авторизации пользователя при неверном email или пароле")
    public void checkLoggedInWithIncorrectData(ValidatableResponse loginResponse) {
        var body = loginResponse
                .assertThat()
                .statusCode(HTTP_UNAUTHORIZED)
                .extract()
                .body().as(Map.class);
        assertEquals("Неверное сообщение об ошибке", "email or password are incorrect", body.get("message"));
    }


    @Step("Ошибка авторизации пользователя, если отправить запрос без email или пароля")
    public void checkLoggedInWithoutData(ValidatableResponse loginResponse) {
        var body = loginResponse
                .assertThat()
                .statusCode(HTTP_UNAUTHORIZED)
                .extract()
                .body().as(Map.class);
        assertEquals("Неверное сообщение об ошибке", "email or password are incorrect", body.get("message"));
    }


    @Step("Успешное обновление данных пользователя")
    public void checkChangedUserData(ValidatableResponse response, User changedUser){
        boolean changed = response
                .assertThat()
                .statusCode(HTTP_OK)
                .extract()
                .path("success");
        assertTrue("Данные пользователя не обновлены", changed);
    }

    @Step("Обновление данных не авторизованного пользователя")
    public void checkChangedUserDataWithoutAuth(ValidatableResponse response, User changedUser){
        var body = response
                .assertThat()
                .statusCode(HTTP_UNAUTHORIZED)
                .extract()
                .body().as(Map.class);
        assertEquals("Неверное сообщение об ошибке", "You should be authorised", body.get("message"));
    }

    @Step("Обновление почты на ту, которая уже используется")
    public void checkDuplicateEmail(ValidatableResponse response, User changedUser){
        var body = response
                .assertThat()
                .statusCode(HTTP_FORBIDDEN)
                .extract()
                .body().as(Map.class);
        assertEquals("Неверное сообщение об ошибке", "User with such email already exists", body.get("message"));
    }

}