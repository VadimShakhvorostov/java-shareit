package ru.practicum.shareit.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;


import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserIntegrationTest {

    @Autowired
    public UserServiceImpl userService;
    @Autowired
    UserRepository userRepository;

    UserDto userRequest;

    @BeforeEach
    public void initData() {
        userRequest = new UserDto();
        userRequest.setName("test-user");
        userRequest.setEmail("test@mail.ru");
    }

    @AfterEach
    public void cleanUser() {
        userRepository.deleteAll();
    }


    @Test
    public void saveUser() {

        userService.save(userRequest);

        User user = userRepository.findAll().getFirst();

        assertEquals(userRequest.getName(), user.getName());
    }

    @Test
    public void updateUser() {

        UserDto userSave = userService.save(userRequest);

        UserDto userUpdate = new UserDto(userSave.getId(), "update", "update@,ail.ru");

        userService.update(userSave.getId(), userUpdate);

        User user = userRepository.findAll().getFirst();

        assertEquals(user.getId(), userUpdate.getId());
        assertEquals(user.getName(), userUpdate.getName());
        assertEquals(user.getEmail(), userUpdate.getEmail());

    }

}
