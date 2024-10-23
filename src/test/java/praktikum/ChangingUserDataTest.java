package praktikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ChangingUserDataTest {
    private User user;
    private final UserClient client = new UserClient();
    private final UserChecks check = new UserChecks();
    private String accessToken;

    @Before
    @DisplayName("Создание пользователя перед тестом")
    public void setUp() {
        user = User.randomUser();
        ValidatableResponse response = client.createUser(user);
        check.checkCreated(response);

        // Получаем токен для дальнейшего удаления пользователя
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
    @DisplayName("Успешное изменение email авторизованного пользователя")
    public void changeUserEmail() {
        User changedEmail = new User("changedMail@mail.ru", user.getPassword(), user.getName());
        ValidatableResponse changedResponse = client.changedUser(accessToken, changedEmail);
        check.checkChangedUserData(changedResponse, changedEmail);
    }

    @Test
    @DisplayName("Успешное изменение пароля авторизованного пользователя")
    public void changeUserPassword() {
        User changedPassword = new User(user.getEmail(), "changedPassword", user.getName());
        ValidatableResponse changedResponse = client.changedUser(accessToken, changedPassword);
        check.checkChangedUserData(changedResponse, changedPassword);
    }

    @Test
    @DisplayName("Успешное изменение имени авторизованного пользователя")
    public void changeUserName() {
        User changedName = new User(user.getEmail(), user.getPassword(), "changedName");
        ValidatableResponse changedResponse = client.changedUser(accessToken, changedName);
        check.checkChangedUserData(changedResponse, changedName);
    }


    @Test
    @DisplayName("Система вернет ошибку на изменение email не авторизованного пользователя")
    public void changeUserEmailWithoutAuth() {
        User changedEmailWithoutAuth = new User("changedMail@mail.ru", user.getPassword(), user.getName());
        ValidatableResponse changedResponse = client.changeDataWithoutAuth(changedEmailWithoutAuth);
        check.checkChangedUserDataWithoutAuth(changedResponse, changedEmailWithoutAuth);
    }

    @Test
    @DisplayName("Система вернет ошибку на изменение пароля не авторизованного пользователя")
    public void changeUserPasswordWithoutAuth() {
        User changedPasswordWithoutAuth = new User(user.getEmail(), "changedPassword", user.getName());
        ValidatableResponse changedResponse = client.changeDataWithoutAuth(changedPasswordWithoutAuth);
        check.checkChangedUserDataWithoutAuth(changedResponse, changedPasswordWithoutAuth);
    }

    @Test
    @DisplayName("Система вернет ошибку на изменение имени не авторизованного пользователя")
    public void changeUserNameWithoutAuth() {
        User changedNameWithoutAuth = new User(user.getEmail(), user.getPassword(), "changedName");
        ValidatableResponse changedResponse = client.changeDataWithoutAuth(changedNameWithoutAuth);
        check.checkChangedUserDataWithoutAuth(changedResponse, changedNameWithoutAuth);
    }


    @Test
    @DisplayName("Система вернет ошибку, если изменить почту на ту, которая уже используется")
    public void changeUserEmailOnDuplicate() {
        User secondUser = User.randomUser();
        ValidatableResponse secondUserResponse = client.createUser(secondUser);
        check.checkCreated(secondUserResponse);

        String secondAccessToken = secondUserResponse.extract().path("accessToken");

        User duplicateEmailUser = new User(user.getEmail(), secondUser.getPassword(), secondUser.getName());
        ValidatableResponse changedResponse = client.changedUser(secondAccessToken, duplicateEmailUser);

        check.checkDuplicateEmail(changedResponse, duplicateEmailUser);
    }

}