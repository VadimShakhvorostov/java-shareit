package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    UserRepository userRepository;

    @Override
    public UserDto save(User user) {
        validationEmail(user.getEmail());
        return UserMapper.toUserDto(userRepository.save(user));
    }

    @Override
    public UserDto update(Long id, User userUpdate) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("Пользователь с id: " + id + " не найден"));
        if (userUpdate.getEmail() != null && userUpdate.getName() != null) {
            validationEmail(userUpdate.getEmail());
            user.setName(userUpdate.getName());
            user.setEmail(userUpdate.getEmail());
        } else if (userUpdate.getEmail() == null && userUpdate.getName() != null) {
            user.setName(userUpdate.getName());
        } else if (userUpdate.getEmail() != null) {
            validationEmail(userUpdate.getEmail());
            user.setEmail(userUpdate.getEmail());
        } else {
            throw new ValidationException("Нет данных для обновления");
        }
        return UserMapper.toUserDto(userRepository.update(user));
    }

    @Override
    public UserDto findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("Пользователь с id: " + id + " не найден"));
        return UserMapper.toUserDto(user);
    }

    @Override
    public void delete(Long id) {
        userRepository.delete(id);
    }

    private void validationEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new ValidationException("Пользователь с email: " + email + " уже существует");
        }
    }
}
