package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.service.UserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mvc;

    private UserDto userRequest;
    private UserDto userResponse;
    private UserDto userResponseUpdate;


    @BeforeEach
    public void initData() {

        userRequest = new UserDto();
        userRequest.setName("user-test");
        userRequest.setEmail("user@mail.ru");

        userResponse = new UserDto(1, "user-test", "user@mail.ru");
        userResponseUpdate = new UserDto(1, "user-test", "update@mail.ru");
    }

    @Test
    @DisplayName("Should crete user")
    void createBookingTest() throws Exception {

        when(userService.save(any(UserDto.class))).thenReturn(userResponse);

        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.id").value(userResponse.getId()))
                .andExpect(jsonPath("$..name").value("user-test"));

        Mockito.verify(userService, Mockito.times(1)).save(userRequest);
    }

    @Test
    @DisplayName("Should return user by id")
    void findUserByIdTest() throws Exception {

        when(userService.findById(anyLong())).thenReturn(userResponse);

        mvc.perform(get("/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.id").value(userResponse.getId()))
                .andExpect(jsonPath("$..name").value("user-test"));

        Mockito.verify(userService, Mockito.times(1)).findById(userResponse.getId());
    }

    @Test
    @DisplayName("Should update user")
    void updateUserTest() throws Exception {

        when(userService.update(anyLong(), any(UserDto.class))).thenReturn(userResponseUpdate);

        mvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.id").value(userResponse.getId()))
                .andExpect(jsonPath("$..name").value("user-test"))
                .andExpect(jsonPath("$..email").value("update@mail.ru"));

        Mockito.verify(userService, Mockito.times(1)).update(userResponseUpdate.getId(), userRequest);
    }

    @Test
    @DisplayName("Should delete user by id")
    void deleteUserTest() throws Exception {

        doNothing().when(userService).delete(anyLong());

        mvc.perform(delete("/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.verify(userService, Mockito.times(1)).delete(1L);
    }

}
