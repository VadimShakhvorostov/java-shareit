package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.Status;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {

    private long id;

    @NotNull(message = "start не может быть пустым")
    private LocalDateTime start;

    @NotNull(message = "end не может быть пустым")
    private LocalDateTime end;

    private long itemId;
    private Status status;


}
