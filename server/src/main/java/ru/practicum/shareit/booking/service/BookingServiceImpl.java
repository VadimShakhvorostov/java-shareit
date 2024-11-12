package ru.practicum.shareit.booking.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.reposirory.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

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
        User user = getUser(userId);
        Item item = itemRepository.findById(bookingDto.getItemId()).orElseThrow(() -> new NotFoundException("Вещь с id: " + bookingDto.getItemId() + " не найдена"));

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
    public Booking bookingApprove(Long bookingId, boolean approved, Long userid) {

        Booking booking = bookingRepository.findByIdAndItemOwnerId(bookingId, userid)
                .orElseThrow(() -> new OwnerException("Не найдено bookingId: " + bookingId + " userId: " + userid));

        if (approved) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        return bookingRepository.save(booking);
    }

    @Override
    public Booking getById(Long bookingId, Long userid) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование с id: " + bookingId + " не найдена"));
        Item item = booking.getItem();
        User owner = item.getOwner();
        if (booking.getBooker().getId() != userid && owner.getId() != userid) {
            throw new OwnerException("Пользователь не является собственником или арендатором");
        }
        return booking;
    }

    @Override
    public List<Booking> getAllByBooker(Long userId, BookingState state) {
        log.trace("getAllByBooker user id = {}, status = {}", userId, state);

        getUser(userId);

        List<Booking> bookingList;

        switch (state) {
            case ALL:
                bookingList = bookingRepository.findAllByBookerIdOrderByStartDesc(userId);
                break;
            case WAITING, REJECTED:
                bookingList = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, state);
                break;
            case CURRENT:
                bookingList = bookingRepository.findCurrentBookingsForBooker(userId, LocalDateTime.now());
                break;
            case PAST:
                bookingList = bookingRepository.findPastBookingsForBooker(userId, LocalDateTime.now());
                break;
            case FUTURE:
                bookingList = bookingRepository.findFutureBookingsForBooker(userId, LocalDateTime.now());
                break;
            default:
                throw new NotFoundException("Неизвестный статус");
        }
        return bookingList;
    }

    @Override
    public List<Booking> getAllByOwner(Long userId, BookingState state) {
        log.trace("getAllByOwner user id = {}, status = {}", userId, state);

        getUser(userId);
        List<Booking> bookingList;

        switch (state) {
            case ALL:
                bookingList = bookingRepository.findAllByItemOwnerIdOrderByStartDesc(userId);
                break;
            case WAITING, REJECTED:
                bookingList = bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(userId, state);
                break;
            case CURRENT:
                bookingList = bookingRepository.findCurrentBookingsForOwner(userId, LocalDateTime.now());
                break;
            case PAST:
                bookingList = bookingRepository.findPastBookingsForOwner(userId, LocalDateTime.now());
                break;
            case FUTURE:
                bookingList = bookingRepository.findFutureBookingsForOwner(userId, LocalDateTime.now());
                break;
            default:
                throw new NotFoundException("Неизвестный статус");
        }
        return bookingList;
    }

    private User getUser(long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с id: " + userId + " не найден"));
    }
}
