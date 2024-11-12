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
    @NotBlank(message = "name empty")
    private String name;
    @NotBlank(message = "description empty")
    private String description;
    @NotNull(message = "available empty")
    private Boolean available;
    private Long requestId;
}
