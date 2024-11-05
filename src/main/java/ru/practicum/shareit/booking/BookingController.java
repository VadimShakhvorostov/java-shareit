package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.Collection;

@RestController
@RequestMapping(path = "/bookings")
@AllArgsConstructor
public class BookingController {

    private BookingService bookingService;

    @PostMapping
    public Booking createBooking(
            @Valid @RequestBody BookingDto booking,
            @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.createBooking(booking, userId);
    }

    @PatchMapping("/{bookingId}")
    public Booking appriveBooking(
            @PathVariable Long bookingId,
            @RequestParam Boolean approved,
            @RequestHeader("X-Sharer-User-Id") Long userid) {
        return bookingService.bookingApprove(bookingId, approved, userid);
    }

    @GetMapping("/{bookingId}")
    public Booking getById(
            @PathVariable Long bookingId,
            @RequestHeader("X-Sharer-User-Id") Long userid) {
        return bookingService.getById(bookingId, userid);
    }

    @GetMapping
    public Collection<Booking> getAllByBooker(@RequestHeader("X-Sharer-User-Id") Long userid,
                                              @RequestParam(defaultValue = "ALL") BookingState state) {
        return bookingService.getAllByBooker(userid, state);
    }

    @GetMapping("/owner")
    public Collection<Booking> getAllByOwner(@RequestHeader("X-Sharer-User-Id") Long userid,
                                             @RequestParam(defaultValue = "ALL") BookingState state) {
        return bookingService.getAllByOwner(userid, state);
    }
}
