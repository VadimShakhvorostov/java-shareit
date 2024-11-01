package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.Set;

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

    private Set<CommentDto> comments;

    private BookingDto lastBooking = new BookingDto();
    private BookingDto nextBooking = new BookingDto();
}
