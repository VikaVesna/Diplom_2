package praktikum;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import main.Client;

import io.qameta.allure.Param;
import static io.qameta.allure.model.Parameter.Mode.HIDDEN;

public class UserClient extends Client {

    @Step("Создание уникального пользователя")
    public ValidatableResponse createUser(User user){
        return spec()
                .body(user)
                .when()
                .post("/auth/register")
                .then().log().all();
    }

    @Step("Авторизация пользователя")
    public ValidatableResponse logIn(UserCredentials cred) {
        return spec()
                .body(cred)
                .when()
                .post("/auth/login")
                .then().log().all();
    }

    @Step("Удаление пользователя")
    public ValidatableResponse deleteUser(@Param(mode=HIDDEN)String accessToken) {
        return spec()
                .header("Authorization", accessToken)
                .when()
                .delete("/auth/user")
                .then().log().all();
    }

    @Step("Обновление данных авторизованного пользователя")
    public ValidatableResponse changedUser(@Param(mode=HIDDEN)String accessToken, User user){
        return spec()
                .header("Authorization", accessToken)
                .body(user)
                .when()
                .patch("/auth/user")
                .then().log().all();
    }

    @Step("Обновление данных не авторизованного пользователя")
    public ValidatableResponse changeDataWithoutAuth(User user){
        return spec()
                .body(user)
                .when()
                .patch("/auth/user")
                .then().log().all();
    }

}