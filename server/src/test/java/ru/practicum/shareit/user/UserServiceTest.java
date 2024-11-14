package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private UserDto userDto;
    private User user;
    private User userResponse;
    private UserDto userResponseDto;
    private UserDto userResponseUpdateDto;
    private User userResponseUpdate;
    private static final long TEST_USER_ID = 1;


    @BeforeEach
    public void initData() {

        MockitoAnnotations.openMocks(this);

        userDto = new UserDto();
        userDto.setName("user-test");
        userDto.setEmail("user@mail.ru");

        user = new User();
        user.setName("user-test");
        user.setEmail("user@mail.ru");

        userResponse = new User(TEST_USER_ID, "user-test", "user@mail.ru");
        userResponseDto = new UserDto(TEST_USER_ID, "user-test", "user@mail.ru");

        userResponseUpdate = new User(TEST_USER_ID, "user-test-update", "update@mail.ru");

        userResponseUpdateDto = new UserDto(TEST_USER_ID, "user-test-update", "update@mail.ru");
    }

    @Test
    @DisplayName("Should create user")
    public void createUserTest() {

        when(userMapper.toUser(any(UserDto.class))).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(userResponse);
        when(userMapper.toUserDto(any(User.class))).thenReturn(userDto);

        UserDto result = userService.save(this.userDto);

        assertNotNull(result);
        assertEquals(userResponse.getName(), result.getName());
        assertEquals(userResponse.getEmail(), result.getEmail());

        verify(userMapper).toUser(any(UserDto.class));
        verify(userRepository).save(any(User.class));
        verify(userMapper).toUserDto(any(User.class));

    }

    @Test
    @DisplayName("Should update user")
    public void updateUserTest() {

        when(userMapper.toUser(any(UserDto.class))).thenReturn(user);
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(userResponse));
        when(userRepository.save(any(User.class))).thenReturn(userResponseUpdate);
        when(userMapper.toUserDto(any(User.class))).thenReturn(userResponseUpdateDto);

        UserDto result = userService.update(TEST_USER_ID, this.userDto);

        assertNotNull(result);
        assertEquals(userResponseUpdate.getId(), result.getId());
        assertEquals(userResponseUpdate.getName(), result.getName());
        assertEquals(userResponseUpdate.getEmail(), result.getEmail());

        verify(userMapper, never()).toUser(any(UserDto.class));
        verify(userRepository).save(any(User.class));
        verify(userMapper).toUserDto(any(User.class));

    }

    @Test
    @DisplayName("Should return user by id")
    public void findByIdTest() {

        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(userResponse));
        when(userMapper.toUserDto(any(User.class))).thenReturn(userResponseDto);

        UserDto result = userService.findById(TEST_USER_ID);

        assertNotNull(result);
        assertEquals(userResponse.getId(), result.getId());
        assertEquals(userResponse.getName(), result.getName());
        assertEquals(userResponse.getEmail(), result.getEmail());

        verify(userRepository).findById(TEST_USER_ID);
        verify(userMapper).toUserDto(any(User.class));

    }


}
