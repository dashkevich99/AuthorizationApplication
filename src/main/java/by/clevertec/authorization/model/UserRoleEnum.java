package by.clevertec.authorization.model;

public enum UserRoleEnum {

    USER(1),
    ADMIN(2);

    private int id;

    UserRoleEnum(int id) {
        this.id = id;
    }

}