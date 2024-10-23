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
        var user = User.randomUser();
        ValidatableResponse response = client.createUser(user);
        check.checkCreated(response);

        accessToken = response.extract().path("accessToken");
    }


    @Test
    @DisplayName("Нельзя создать пользователя, который уже зарегистрирован")
    public void createDuplicateCourier() {
        var user = User.randomUser();
        ValidatableResponse firstResponse = client.createUser(user);
        check.checkCreated(firstResponse);

        accessToken = firstResponse.extract().path("accessToken");

        ValidatableResponse secondResponse = client.createUser(user);
        check.checkDuplicateRequest(secondResponse);
    }


    @Test
    @DisplayName("Нельзя создать пользователя без поля email")
    public void createUserWithoutEmail() {
        var user = User.withoutEmail();
        ValidatableResponse response = client.createUser(user);
        check.checkFailed(response);
    }

    @Test
    @DisplayName("Нельзя создать пользователя без поля password")
    public void createUserWithoutPassword() {
        var user = User.withoutPassword();
        ValidatableResponse response = client.createUser(user);
        check.checkFailed(response);
    }

    @Test
    @DisplayName("Нельзя создать пользователя без поля name")
    public void createUserWithoutName() {
        var user = User.withoutName();
        ValidatableResponse response = client.createUser(user);
        check.checkFailed(response);
    }

    @Test
    @DisplayName("Нельзя создать пользователя с null в поле email")
    public void createUserWithNullInEmail() {
        var user = User.nullInEmail();
        ValidatableResponse response = client.createUser(user);
        check.checkFailed(response);
    }

    @Test
    @DisplayName("Нельзя создать пользователя с null в поле password")
    public void createUserWithNullInPassword() {
        var user = User.nullInPassword();
        ValidatableResponse response = client.createUser(user);
        check.checkFailed(response);
    }

    @Test
    @DisplayName("Нельзя создать пользователя с null в поле name")
    public void createUserWithNullInName() {
        var user = User.nullInName();
        ValidatableResponse response = client.createUser(user);
        check.checkFailed(response);
    }
}