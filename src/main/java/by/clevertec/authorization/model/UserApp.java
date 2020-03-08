package by.clevertec.authorization.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_app")
public class UserApp {
    @Id
    private String login;
    private String password;
    private UserRoleEnum userRoleEnum;
    @Column(name = "token", length = 1000)
    private String token;
}
