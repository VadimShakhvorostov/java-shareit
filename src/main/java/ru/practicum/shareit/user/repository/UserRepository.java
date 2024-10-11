package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

@Component
public interface UserRepository {
    User save(User user);

    User update(User user);

    Optional<User> findById(Long id);

    boolean existsByEmail(String email);

    void delete(Long id);

}
