package ru.practicum.shareit.request;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.reposirory.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;

import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;


import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class ItemRequesIntegrationTest {

    @Autowired
    private ItemRequestServiceImpl itemRequestService;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private ItemRequestRepository itemRequestRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemService itemService;

    UserDto user;
    UserDto owner;
    ItemDto item;
    ItemRequestDto request;


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

    }

    @AfterEach
    void cleanup() {

        itemRepository.deleteAll();
        itemRequestRepository.deleteAll();
        userRepository.deleteAll();

    }

    @Test
    public void createItemRequest() {
        UserDto userSave = userService.save(user);

        ItemRequestDto itemRequestSaved = itemRequestService.createItemRequest(request, userSave.getId());

        assertEquals(request.getDescription(), itemRequestSaved.getDescription());

    }


    @Test
    public void getItemRequestOwner() {

        UserDto userSave = userService.save(user);
        UserDto ownerSave = userService.save(owner);

        ItemRequestDto itemRequestSaved = itemRequestService.createItemRequest(request, userSave.getId());

        item.setRequestId(itemRequestSaved.getId());
        ItemDto itemSaved = itemService.save(item, ownerSave.getId());

        List<ItemRequestDto> result = itemRequestService.getItemRequestOwner(userSave.getId());
        assertEquals(1, result.size());
        assertEquals(itemRequestSaved.getId(), result.getFirst().getId());
        assertEquals(itemRequestSaved.getDescription(), result.getFirst().getDescription());

    }

    @Test
    public void getAllItemRequest() {

        UserDto userSave = userService.save(user);
        UserDto ownerSave = userService.save(owner);

        ItemRequestDto itemRequestSaved = itemRequestService.createItemRequest(request, userSave.getId());

        item.setRequestId(itemRequestSaved.getId());
        ItemDto itemSaved = itemService.save(item, ownerSave.getId());

        List<ItemRequestDto> result = itemRequestService.getAllItemRequests(userSave.getId());
        assertEquals(1, result.size());
        assertEquals(itemRequestSaved.getId(), result.getFirst().getId());
        assertEquals(itemRequestSaved.getDescription(), result.getFirst().getDescription());

    }

    @Test
    public void getItemRequestById() {

        UserDto userSave = userService.save(user);
        UserDto ownerSave = userService.save(owner);

        ItemRequestDto itemRequestSaved = itemRequestService.createItemRequest(request, userSave.getId());

        item.setRequestId(itemRequestSaved.getId());
        ItemDto itemSaved = itemService.save(item, ownerSave.getId());

        ItemRequestDto result = itemRequestService.getItemRequestById(itemRequestSaved.getId(), userSave.getId());

        assertNotNull(result);
        assertEquals(itemRequestSaved.getId(), result.getId());
        assertEquals(itemRequestSaved.getDescription(), result.getDescription());
    }
}
