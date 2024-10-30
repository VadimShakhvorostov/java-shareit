package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Comment;

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

    private Set<Comment> comments;

    private Booking lastBooking = new Booking();
    private Booking nextBooking = new Booking();
}
