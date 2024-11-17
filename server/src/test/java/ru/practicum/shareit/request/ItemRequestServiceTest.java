package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ItemRequestServiceTest {

    @Mock
    private ItemRequestRepository itemRequestRepository;

    @Mock
    private ItemRequestMapper itemRequestMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRequestDto itemRequestDto;

    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;

    private User user;
    private ItemRequest itemRequest;
    private ItemRequestDto itemRequestResponseDto;
    private ItemRequest itemRequestResponse;
    private ItemDto itemDto;
    private Item item;
    private List<ItemRequestDto> itemRequestResponseDtoList;
    private List<ItemRequest> itemRequestResponseList;
    private List<Item> itemList;
    private static final long TEST_USER_ID = 1;
    private static final long TEST_REQUEST_ID = 1;
    private static final long TEST_ITEM_ID = 1;

    @BeforeEach
    public void initData() {

        MockitoAnnotations.openMocks(this);

        LocalDateTime created = LocalDateTime.now();

        user = new User(TEST_USER_ID, "test-user", "test@mail.ru");

        itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("description-test");

        itemRequest = new ItemRequest();
        itemRequest.setDescription("description-test");

        itemRequestResponseDto = new ItemRequestDto();
        itemRequestResponseDto.setId(TEST_REQUEST_ID);
        itemRequestResponseDto.setDescription("description-test");
        itemRequestResponseDto.setItems(Arrays.asList(itemDto));
        itemRequestResponseDto.setCreated(created);

        itemRequestResponse = new ItemRequest();
        itemRequestResponse.setId(TEST_REQUEST_ID);
        itemRequestResponse.setDescription("description-test");
        itemRequestResponse.setRequester(user);
        itemRequestResponse.setCreated(created);

        itemDto = new ItemDto();
        itemDto.setId(TEST_ITEM_ID);
        itemDto.setName("item-test");
        itemDto.setDescription("item-test");
        itemDto.setRequestId(TEST_REQUEST_ID);

        item = new Item();
        item.setId(TEST_REQUEST_ID);
        item.setName("item-test");
        item.setDescription("item-test");
        item.setRequest(itemRequestResponse);

        itemList = Arrays.asList(item);

        itemRequestResponseDtoList = Arrays.asList(itemRequestResponseDto);
        itemRequestResponseList = Arrays.asList(itemRequestResponse);


    }

    @Test
    @DisplayName("Should crete request")
    void createItemRequestTest() {

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRequestMapper.toItemRequest(any(ItemRequestDto.class))).thenReturn(itemRequest);
        when(itemRequestRepository.save(any(ItemRequest.class))).thenReturn(itemRequest);
        when(itemRequestMapper.toItemRequestDto(any(ItemRequest.class))).thenReturn(itemRequestResponseDto);

        ItemRequestDto result = itemRequestService.createItemRequest(itemRequestDto, TEST_USER_ID);

        assertNotNull(result);
        assertEquals(itemRequestResponseDto.getId(), result.getId());
        assertEquals(itemRequestResponseDto.getDescription(), result.getDescription());

        verify(userRepository).findById(anyLong());
        verify(itemRequestMapper).toItemRequest(any(ItemRequestDto.class));
        verify(itemRequestRepository).save(any(ItemRequest.class));
    }

}
