package praktikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UserAuthorizationTest {
    private User user;
    private ValidatableResponse response;
    private final UserClient client = new UserClient();
    private final UserChecks check = new UserChecks();
    private String accessToken;

    @Before
    @DisplayName("Создание пользователя перед тестом")
    public void setUp() {
        user = User.randomUser();
        response = client.createUser(user);
        check.checkCreated(response);

        accessToken = response.extract().path("accessToken");
    }

    @After
    @DisplayName("Удаление пользователя после теста")
    public void tearDown() {
        if (accessToken != null && !accessToken.isEmpty()) {
            client.deleteUser(accessToken);
        }
    }


    @Test
    @DisplayName("Авторизация под существующим пользователем")
    public void loginInWithCorrectData() {
        UserCredentials cred = UserCredentials.fromUser(user);

        ValidatableResponse loginResponse = client.logIn(cred);
        check.checkLoggedIn(loginResponse);
    }


    @Test
    @DisplayName("Система вернёт ошибку при авторизации с неверным email")
    public void incorrectEmailInAuthorization() {
        UserCredentials incorrectEmailCred = new UserCredentials("testMail@mail.ru", user.getPassword());
        ValidatableResponse loginResponse = client.logIn(incorrectEmailCred);
        check.checkLoggedInWithIncorrectData(loginResponse);
    }

    @Test
    @DisplayName("Система вернёт ошибку при авторизации с неверным паролем")
    public void incorrectPasswordInAuthorization() {
        UserCredentials incorrectPasswordCred = new UserCredentials(user.getEmail(), "testPassword");
        ValidatableResponse loginResponse = client.logIn(incorrectPasswordCred);
        check.checkLoggedInWithIncorrectData(loginResponse);
    }


    @Test
    @DisplayName("Система вернёт ошибку при авторизации без email")
    public void emptyEmailInAuthorization() {
        UserCredentials emptyEmailCred = new UserCredentials("", user.getPassword());
        ValidatableResponse loginResponse = client.logIn(emptyEmailCred);
        check.checkLoggedInWithoutData(loginResponse);
    }

    @Test
    @DisplayName("Система вернёт ошибку при авторизации без пароля")
    public void emptyPasswordInAuthorization() {
        UserCredentials incorrectPasswordCred = new UserCredentials(user.getEmail(), "");
        ValidatableResponse loginResponse = client.logIn(incorrectPasswordCred);
        check.checkLoggedInWithoutData(loginResponse);
    }


    @Test
    @DisplayName("Система вернёт ошибку при авторизации с null в поле email")
    public void nullEmailInAuthorization() {
        UserCredentials nullInEmailCred = new UserCredentials(null, user.getPassword());
        ValidatableResponse loginResponse = client.logIn(nullInEmailCred);
        check.checkLoggedInWithoutData(loginResponse);
    }

    @Test
    @DisplayName("Система вернёт ошибку при авторизации с null в поле password")
    public void nullPasswordInAuthorization() {
        UserCredentials nullInPasswordCred = new UserCredentials(user.getEmail(), null);
        ValidatableResponse loginResponse = client.logIn(nullInPasswordCred);
        check.checkLoggedInWithoutData(loginResponse);
    }
}