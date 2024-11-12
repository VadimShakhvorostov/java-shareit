package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingService {

    Booking createBooking(BookingDto bookingDto, Long userId);

    Booking bookingApprove(Long bookingId, boolean approved, Long userid);

    Booking getById(Long bookingId, Long userid);

    List<Booking> getAllByBooker(Long userId, BookingState state);

    List<Booking> getAllByOwner(Long userId, BookingState state);

}
