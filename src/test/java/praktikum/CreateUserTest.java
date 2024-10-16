package praktikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;

public class CreateUserTest {

    private final UserClient client = new UserClient();
    private final UserChecks check = new UserChecks();
    private String accessToken;


    @After
    @DisplayName("Удаление пользователя после теста")
    public void tearDown() {
        if (accessToken != null && !accessToken.isEmpty()) {
            client.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Можно создать уникального пользователя")
    public void createUser(){
        // Создаем уникального пользователя
        var user = User.randomUser();
        ValidatableResponse response = client.createUser(user);
        check.checkCreated(response);

        // Авторизуемся и получаем токен
        var cred = UserCredentials.fromUser(user);
        String tokenWithBearer = client.logIn(cred).extract().path("accessToken");

        // Удаляем префикс "Bearer " и сохраняем только токен
        accessToken = tokenWithBearer.replace("Bearer ", "");
    }


    @Test
    @DisplayName("Нельзя создать пользователя, который уже зарегистрирован")
    public void createDuplicateCourier() {
        // Создаем уникального пользователя
        var user = User.randomUser();
        ValidatableResponse firstResponse = client.createUser(user);
        check.checkCreated(firstResponse);

        // Авторизуемся, получаем токен, удаляем префикс "Bearer " и сохраняем только токен
        var cred = UserCredentials.fromUser(user);
        String tokenWithBearer = client.logIn(cred).extract().path("accessToken");
        accessToken = tokenWithBearer.replace("Bearer ", "");

        // Повторно создаем того же пользователя
        ValidatableResponse secondResponse = client.createUser(user);
        check.checkDuplicateRequest(secondResponse);
    }

}