package by.clevertec.authorization.repository;

import by.clevertec.authorization.model.UserApp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<UserApp, String> {
      List <UserApp> findByLogin(String login);
}