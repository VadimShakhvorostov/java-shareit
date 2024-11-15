package ru.practicum.shareit.item;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.repository.BookingRepository;
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
public class ItemIntegrationTest {

    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemService itemService;
    @Autowired
    BookingRepository bookingRepository;

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
    void itemSaveTest() {

        UserDto ownerSave = userService.save(owner);
        ItemDto itemSaved = itemService.save(item, ownerSave.getId());

        ItemDto result = itemService.save(itemSaved, ownerSave.getId());

        assertNotNull(result);
        assertEquals(itemSaved.getDescription(), result.getDescription());
        assertEquals(itemSaved.getAvailable(), result.getAvailable());
        assertEquals(itemSaved.getName(), result.getName());

    }

    @Test
    void itemGetByIdTest() {
        UserDto ownerSave = userService.save(owner);
        ItemDto itemSaved = itemService.save(item, ownerSave.getId());

        ItemDto result = itemService.findById(itemSaved.getId());

        assertNotNull(result);
        assertEquals(itemSaved.getDescription(), result.getDescription());
        assertEquals(itemSaved.getAvailable(), result.getAvailable());
        assertEquals(itemSaved.getName(), result.getName());


    }

}
