package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.Collection;

public interface BookingService {

    Booking createBooking(BookingDto bookingDto, Long userId);

    Booking bookingApprove(Long bookingId, boolean bookingApprove, Long userid);

    Booking getById(Long bookingId, Long userid);

    Collection<Booking> getAll(Long userId);

}
