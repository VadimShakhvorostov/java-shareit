package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingMapper;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.reposirory.CommentRepository;
import ru.practicum.shareit.item.reposirory.ItemRepository;
import ru.practicum.shareit.item.service.ItemServiceImp;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemMapper itemMapper;
    @Mock
    private ItemRequestRepository itemRequestRepository;
    @Mock
    private CommentMapper commentMapper;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private BookingMapper bookingMapper;

    @InjectMocks
    private ItemServiceImp itemService;

    private static final long TEST_ITEM_ID = 1;

    private ItemDto itemDto;
    private ItemDto itemDtoResponse;
    private Item item;
    private ItemDto itemResponseUpdate;
    private Collection<Item> itemResponseList;
    private CommentDto commentRequestDto;
    private Comment commentResponse;
    private CommentDto commentResponseDto;
    private User user;
    private Booking booking;
    private static final long TEST_USER_ID = 1;

    @BeforeEach
    public void initData() {

        MockitoAnnotations.openMocks(this);

        user = new User(TEST_USER_ID, "user-test", "user@mail.ru");

        itemDto = new ItemDto();
        itemDto.setName("Item-test");
        itemDto.setDescription("Item-test");

        item = new Item();
        item.setId(TEST_ITEM_ID);
        item.setName("Item-test");
        item.setDescription("Item-test");
        item.setAvailable(true);
        item.setOwner(user);

        itemDtoResponse = new ItemDto();
        itemDtoResponse.setId(TEST_ITEM_ID);
        itemDtoResponse.setName("Item-test");
        itemDtoResponse.setDescription("Item-test");
        itemDtoResponse.setAvailable(true);

        itemResponseUpdate = new ItemDto();
        itemResponseUpdate.setId(TEST_ITEM_ID);
        itemResponseUpdate.setName("ItemRequest-test-update");
        itemResponseUpdate.setDescription("ItemRequest-test-update");
        itemResponseUpdate.setAvailable(true);

        itemResponseList = new ArrayList<>(Arrays.asList(item));

        commentRequestDto = new CommentDto();
        commentRequestDto.setText("test-text");

        commentResponse = new Comment();
        commentResponse.setId(1L);
        commentResponse.setText("test-text");
        commentResponse.setCreated(LocalDateTime.now());

        commentResponseDto = new CommentDto();
        commentResponseDto.setId(1L);
        commentResponseDto.setText("test-text");
        commentResponseDto.setCreated(LocalDateTime.now());

        booking = new Booking(
                1L,
                LocalDateTime.now().minusHours(1),
                LocalDateTime.now().minusHours(2),
                item,
                user,
                Status.APPROVED);
    }

    @Test
    @DisplayName("Should return item by id")
    public void findByIdTest() {

        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(item));
        when(itemMapper.toItemDto(any(Item.class))).thenReturn(itemDtoResponse);
        when(commentRepository.findAllByItem_Id(anyLong())).thenReturn(null);
        when(commentMapper.toListCommentDto(anySet())).thenReturn(null);

        ItemDto result = itemService.findById(TEST_ITEM_ID);

        assertNotNull(result);
        assertEquals(item.getId(), result.getId());
        assertEquals(item.getName(), result.getName());
        assertEquals(item.getDescription(), result.getDescription());

        verify(itemRepository).findById(TEST_ITEM_ID);
        verify(commentRepository).findAllByItem_Id(TEST_ITEM_ID);
        verify(itemMapper).toItemDto(any(Item.class));

    }


    @Test
    @DisplayName("Should return item by id")
    public void findAllItemByUserTest() {

        when(itemRepository.findAllByOwnerId(anyLong())).thenReturn(itemResponseList);
        when(itemMapper.toItemDto(any(Item.class))).thenReturn(itemDtoResponse);

        Collection<ItemDto> result = itemService.findAllItemByUser(TEST_USER_ID);

        assertNotNull(result);
        assertEquals(itemResponseList.size(), result.size());

        verify(itemRepository).findAllByOwnerId(TEST_USER_ID);
    }

    @Test
    @DisplayName("Should save item")
    public void saveItemTest() {

        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.empty());

        when(itemMapper.toItem(any(ItemDto.class))).thenReturn(item);
        when(itemMapper.toItemDto(any(Item.class))).thenReturn(itemDtoResponse);

        when(itemRepository.save(item)).thenReturn(item);

        ItemDto result = itemService.save(itemDto, TEST_USER_ID);

        assertNotNull(result);
        assertEquals(item.getName(), result.getName());
        assertEquals(item.getDescription(), result.getDescription());

        verify(userRepository).findById(TEST_USER_ID);
        verify(itemMapper).toItem(any(ItemDto.class));
        verify(itemMapper).toItemDto(any(Item.class));

    }


    @Test
    @DisplayName("Should create comment")
    public void createCommentTest() {

        when(commentMapper.toComment(commentRequestDto)).thenReturn(commentResponse);

        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(item));
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));

        when(bookingRepository
                .findBookingByUserIdAndItemIdRented(anyLong(), anyLong(), any(LocalDateTime.class)))
                .thenReturn(Optional.of(booking));

        when(commentRepository.save(any(Comment.class))).thenReturn(commentResponse);
        when(commentMapper.toCommentDto(commentResponse)).thenReturn(commentResponseDto);

        CommentDto result = itemService.createComment(TEST_ITEM_ID, TEST_USER_ID, commentRequestDto);

        assertNotNull(result);
        assertEquals(commentResponseDto.getText(), result.getText());

        verify(commentRepository).save(any(Comment.class));
        verify(itemRepository).findById(TEST_ITEM_ID);
        verify(userRepository).findById(TEST_USER_ID);
    }

    @Test
    @DisplayName("Should update item")
    public void updateTest() {

        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(itemMapper.toItemDto(any(Item.class))).thenReturn(itemDtoResponse);

        when(bookingRepository.findByItemId(anyLong())).thenReturn(List.of());

        ItemDto result = itemService.update(TEST_ITEM_ID, TEST_USER_ID, itemDto);

        assertNotNull(result);
        assertEquals(item.getName(), result.getName());
        assertEquals(item.getDescription(), result.getDescription());

        verify(itemRepository).findById(TEST_ITEM_ID);
        verify(itemMapper).toItemDto(any(Item.class));
    }
}