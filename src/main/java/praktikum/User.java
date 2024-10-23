package praktikum;

import org.apache.commons.lang3.RandomStringUtils;

public class User {

    private String email;
    private String password;
    private String name;


    public User() {
    }

    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }


    public static User randomUser() {
        String email = RandomStringUtils.randomAlphanumeric(6, 10) + "@mail.ru";
        String password = RandomStringUtils.randomAlphanumeric(6, 10);
        String name = RandomStringUtils.randomAlphanumeric(6, 10);
        return new User(email, password, name);
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public static User withoutEmail() {
        return new User("", "123456", "newTestUser");
    }

    public static User withoutPassword() {
        return new User(RandomStringUtils.randomAlphanumeric(6, 10) + "@mail.ru", "", "newTestUser");
    }

    public static User withoutName() {
        return new User(RandomStringUtils.randomAlphanumeric(6, 10) + "@mail.ru", "123456", "");
    }


    public static User nullInEmail() {
        return new User(null, "123456", "newTestUser");
    }

    public static User nullInPassword() {
        return new User(RandomStringUtils.randomAlphanumeric(6, 10) + "@mail.ru", null, "newTestUser");
    }

    public static User nullInName() {
        return new User(RandomStringUtils.randomAlphanumeric(6, 10) + "@mail.ru", "123456", null);
    }

}