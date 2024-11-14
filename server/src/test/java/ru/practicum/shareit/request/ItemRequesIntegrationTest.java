package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemServiceImp;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;

import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.service.UserServiceImpl;

@SpringBootTest
public class ItemRequesIntegrationTest {

    @Autowired
    private ItemRequestServiceImpl itemRequestService;
    @Autowired
    public UserServiceImpl userService;
    @Autowired
    public ItemServiceImp itemService;
    @Autowired
    private BookingServiceImpl bookingService;

    UserDto user;
    ItemDto item;


    @BeforeEach
    void initData() {
    }

    @Test
    public void createResponse() {

    }

}
