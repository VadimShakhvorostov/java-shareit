package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserDto {
    private long id;
    private String name;
    private String email;
}
