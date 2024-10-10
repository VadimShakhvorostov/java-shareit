package ru.practicum.shareit.item.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    private long id;
    @NotBlank(message = ("name не может быть пустым"))
    private String name;
    @NotBlank(message = ("description не может быть пустым"))
    private String description;
    @NotNull(message = ("available не может быть пустым"))
    private Boolean available;
    private User owner;
    private ItemRequest request;

}
