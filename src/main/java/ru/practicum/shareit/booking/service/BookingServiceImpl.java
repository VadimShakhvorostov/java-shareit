package ru.practicum.shareit.booking.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.reposirory.ItemRepository;
import ru.practicum.shareit.user.dto.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;

@Service
@AllArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    private BookingRepository bookingRepository;
    private UserRepository userRepository;
    private BookingMapper bookingMapper;
    private ItemRepository itemRepository;

    @Override
    public Booking createBooking(BookingDto bookingDto, Long userId) {
        log.trace("create booking item_id: {}, start: {}, end: {} ", bookingDto.getItemId(), bookingDto.getStart(), bookingDto.getEnd());
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с id: " + userId + " не найден"));
        Item item = itemRepository.findById(bookingDto.getItemId()).orElseThrow(() -> new NotFoundException("Вещь с id: " + userId + " не найдена"));

        if (bookingDto.getStart().equals(bookingDto.getEnd()) ||
                bookingDto.getEnd().isBefore(bookingDto.getStart()) ||
                bookingDto.getStart().isAfter(bookingDto.getEnd())

        ) {
            throw new DateException("Некорректные даты бронирования");
        }

        if (item.getAvailable().equals(false)) {
            throw new AvailableException("Вещь с id: " + userId + " не доступна");
        }

        Booking booking = bookingMapper.toBooking(bookingDto);

        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(Status.WAITING);

        return bookingRepository.save(booking);

    }

    @Override
    public Booking bookingApprove(Long bookingId, boolean bookingApprove, Long userid) {

        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException("Вещь с id: " + bookingId + " не найдена"));
        Item item = booking.getItem();
        User owner = item.getOwner();

        if (owner.getId() != userid) {
            throw new OwnerException("User не владелец");
        }
        if (bookingApprove) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        return bookingRepository.save(booking);
    }

    @Override
    public Booking getById(Long bookingId, Long userid) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException("Вещь с id: " + bookingId + " не найдена"));
        Item item = booking.getItem();
        User owner = item.getOwner();
        if (booking.getBooker().getId() != userid && owner.getId() != userid) {
            throw new OwnerException("Пользователь не является собственником или арендатором");
        }
        return booking;
    }

    @Override
    public Collection<Booking> getAll(Long userId) {
        return bookingRepository.findBookingByBooker_Id(userId);
    }
}
