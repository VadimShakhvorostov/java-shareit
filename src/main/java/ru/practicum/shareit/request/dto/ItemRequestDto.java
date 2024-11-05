package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class ItemRequestDto {
    private long id;
    @NotBlank(message = ("description не может быть пустым"))
    private String description;
    private Long requestor;
    private LocalDateTime created;
}
