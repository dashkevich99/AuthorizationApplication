package by.clevertec.authorization.listiner;

import by.clevertec.authorization.model.UserApp;
import by.clevertec.authorization.model.UserRoleEnum;
import by.clevertec.authorization.repository.UserRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class CreateUserOnStartUp {
    private final UserRepository userRepository;

    public CreateUserOnStartUp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void loadData() {
        userRepository.saveAll(Stream.of(
                new UserApp("Pavel", "$2a$10$Hiq8iRHyDFXGeqRgBAanGu8lOCATtCQKKIWnSBBfkmL/LYoZvoJza", UserRoleEnum.USER, null),
                new UserApp("Admin", "$2a$10$Hiq8iRHyDFXGeqRgBAanGu8lOCATtCQKKIWnSBBfkmL/LYoZvoJza", UserRoleEnum.ADMIN, null)
        ).filter(userApp -> !userRepository.existsById(userApp.getLogin())).collect(Collectors.toSet()));
    }
}
