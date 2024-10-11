package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserDto {
    private long id;
    private String name;
    @NotBlank(message = "email не может быть пустым")
    @Email(message = "Неверный формат email")
    private String email;
}
