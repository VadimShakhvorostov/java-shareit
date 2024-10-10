package ru.practicum.shareit.user.service;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

@Component
public interface UserService {

    UserDto save(User user);

    UserDto update(Long id, User userUpdate);

    UserDto findById(Long id);

    void delete(Long id);
}
