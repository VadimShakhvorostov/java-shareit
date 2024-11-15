package ru.practicum.shareit.booking;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.reposirory.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class BookingIntegrationTest {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private BookingServiceImpl bookingServiceImpl;

    UserDto user;
    UserDto owner;
    ItemDto item;
    ItemRequestDto request;
    BookingDto booking;


    @BeforeEach
    void initData() {

        request = new ItemRequestDto();
        request.setDescription("test-request");

        user = new UserDto();
        user.setName("test-user");
        user.setEmail("test-user@test.com");

        owner = new UserDto();
        owner.setName("test-owner");
        owner.setEmail("test-owner@test.com");

        item = new ItemDto();
        item.setName("test-item");
        item.setDescription("test-item");
        item.setAvailable(true);

        booking = new BookingDto();
        booking.setStart(LocalDateTime.of(2024, 10, 10, 12, 0, 0));
        booking.setEnd(LocalDateTime.of(2024, 10, 11, 12, 0, 0));

    }

    @AfterEach
    void cleanup() {

        bookingRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();

    }

    @Test
    void creteBooking() {

        UserDto userSave = userService.save(user);
        UserDto ownerSave = userService.save(owner);
        ItemDto itemSaved = itemService.save(item, ownerSave.getId());

        booking.setItemId(itemSaved.getId());

        Booking result = bookingServiceImpl.createBooking(booking, userSave.getId());

        assertEquals(booking.getItemId(), result.getItem().getId());
        assertEquals(booking.getStart(), result.getStart());
        assertEquals(booking.getEnd(), result.getEnd());

    }

    @Test
    void bookingApprove() {

        UserDto userSave = userService.save(user);
        UserDto ownerSave = userService.save(owner);
        ItemDto itemSaved = itemService.save(item, ownerSave.getId());

        booking.setItemId(itemSaved.getId());
        Booking bookingSave = bookingServiceImpl.createBooking(booking, userSave.getId());

        Booking bookingApproved = bookingServiceImpl.bookingApprove(bookingSave.getId(), true, ownerSave.getId());

        assertNotNull(bookingApproved);
        assertEquals(Status.APPROVED, bookingApproved.getStatus());
    }
}
