package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingMapper;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.reposirory.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookingMapper bookingMapper;
    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private Booking bookingResponse;
    private BookingDto bookingRequest;
    private Item bookingItem;
    private User booker;
    List<Booking> bookingsResponse;
    private static final long TEST_USER_ID = 1;
    private User owner;

    @BeforeEach
    public void initData() {

        MockitoAnnotations.openMocks(this);

        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusHours(1);

        booker = new User(1, "booker-test", "booker@mail.ru");
        owner = new User(2, "owner-test", "owner@mail.ru");

        bookingItem = new Item();
        bookingItem.setId(1);
        bookingItem.setDescription("booking-description");
        bookingItem.setAvailable(true);
        bookingItem.setRequest(null);
        bookingItem.setName("item-name");
        bookingItem.setOwner(owner);

        bookingResponse = new Booking();
        bookingResponse.setId(1);
        bookingResponse.setItem(bookingItem);
        bookingResponse.setStart(start);
        bookingResponse.setEnd(end);
        bookingResponse.setStatus(Status.APPROVED);
        bookingResponse.setBooker(booker);

        bookingRequest = new BookingDto();
        bookingRequest.setStart(start);
        bookingRequest.setEnd(end);
        bookingRequest.setItemId(bookingItem.getId());

        bookingsResponse = new ArrayList<>(Arrays.asList(bookingResponse));
    }

    @Test
    @DisplayName("Should crete booking")
    public void creteBooking() {

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(booker));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(bookingItem));
        when(bookingMapper.toBooking(any(BookingDto.class))).thenReturn(bookingResponse);
        when(bookingRepository.save(any())).thenReturn(bookingResponse);


        Booking result = bookingService.createBooking(bookingRequest, TEST_USER_ID);

        assertNotNull(result);
        assertEquals(bookingRequest.getItemId(), result.getItem().getId());

        verify(bookingRepository).save(any());
        verify(bookingMapper).toBooking(any(BookingDto.class));
        verify(userRepository).findById(anyLong());
        verify(itemRepository).findById(anyLong());

    }

    @Test
    @DisplayName("Should booking approved")
    public void bookingApprovedTest() {

        when(bookingRepository.findByIdAndItemOwnerId(anyLong(), anyLong())).thenReturn(Optional.of(bookingResponse));
        when(bookingRepository.save(any())).thenReturn(bookingResponse);

        Booking result = bookingService.bookingApprove(bookingResponse.getId(), true, TEST_USER_ID);

        assertNotNull(result);
        assertEquals(bookingResponse.getId(), result.getId());
        assertEquals(bookingResponse.getItem().getId(), result.getItem().getId());
        assertEquals(Status.APPROVED, result.getStatus());


        verify(bookingRepository).findByIdAndItemOwnerId(bookingResponse.getId(), TEST_USER_ID);
        verify(bookingRepository).save(any());

    }

    @Test
    @DisplayName("Should return booking by ID")
    public void getByIdTest() {

        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(bookingResponse));

        Booking result = bookingService.getById(bookingResponse.getId(), TEST_USER_ID);

        assertNotNull(result);
        assertEquals(bookingResponse.getId(), result.getId());
        assertEquals(bookingResponse.getItem().getId(), result.getItem().getId());
        assertEquals(Status.APPROVED, result.getStatus());

        verify(bookingRepository).findById(anyLong());
    }

    @Test
    @DisplayName("Should return all booking by owner ID")
    public void getAllByOwnerTest() {

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(owner));
        when(bookingRepository.findAllByItemOwnerIdOrderByStartDesc(anyLong())).thenReturn(bookingsResponse);

        List<Booking> result = bookingService.getAllByOwner(owner.getId(), BookingState.ALL);

        assertNotNull(result);
        assertEquals(bookingsResponse.size(), result.size());

        verify(userRepository).findById(anyLong());
        verify(bookingRepository).findAllByItemOwnerIdOrderByStartDesc(anyLong());

    }

}
