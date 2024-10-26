package ru.practicum.shareit.user;

import org.mapstruct.Mapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(UserDto userDto);
    UserDto toUserDto(User user);
}
