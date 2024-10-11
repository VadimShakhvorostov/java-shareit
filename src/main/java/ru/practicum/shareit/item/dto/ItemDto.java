package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ItemDto {
    private long id;
    @NotBlank(message = ("name не может быть пустым"))
    private String name;
    @NotBlank(message = ("description не может быть пустым"))
    private String description;
    @NotNull(message = ("available не может быть пустым"))
    private Boolean available;
    private Long request;
}
