package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(BookingController.class)
public class BookingControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingService bookingService;

    @Autowired
    private MockMvc mvc;

    private Booking bookingResponse;
    private BookingDto bookingRequest;
    private Item bookingItem;
    private User booker;
    List<Booking> bookingsResponse;

    private User owner;

    @BeforeEach
    public void initData() {

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
    void createBookingTest() throws Exception {

        when(bookingService.createBooking(any(), anyLong())).thenReturn(bookingResponse);

        mvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingRequest))
                        .header("X-Sharer-User-Id", owner.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.id").value(bookingResponse.getId()))
                .andExpect(jsonPath("$.item.id").value(bookingItem.getId()))
                .andExpect(jsonPath("$.item.name").value("item-name"));

        Mockito.verify(bookingService, Mockito.times(1)).createBooking(bookingRequest, owner.getId());
    }

    @Test
    @DisplayName("Should approve booking")
    void approveBookingTest() throws Exception {

        when(bookingService.bookingApprove(anyLong(), anyBoolean(), anyLong())).thenReturn(bookingResponse);

        mvc.perform(patch("/bookings/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", owner.getId())
                        .param("approved", "true")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.id").value(bookingResponse.getId()))
                .andExpect(jsonPath("$.item.id").value(bookingItem.getId()))
                .andExpect(jsonPath("$.item.name").value("item-name"))
                .andExpect(jsonPath("$.status").value("APPROVED"));

        Mockito.verify(bookingService, Mockito.times(1))
                .bookingApprove(bookingResponse.getId(), true, owner.getId());
    }

    @Test
    @DisplayName("Should return booking by id")
    void getByIdTest() throws Exception {

        when(bookingService.getById(anyLong(), anyLong())).thenReturn(bookingResponse);

        mvc.perform(get("/bookings/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", owner.getId())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.id").value(bookingResponse.getId()))
                .andExpect(jsonPath("$.item.id").value(bookingItem.getId()))
                .andExpect(jsonPath("$.item.name").value("item-name"));

        Mockito.verify(bookingService, Mockito.times(1)).getById(bookingResponse.getId(), owner.getId());
    }


    @Test
    @DisplayName("Should return all booking by booker id")
    void getAllByBooker() throws Exception {

        when(bookingService.getAllByBooker(anyLong(), any(BookingState.class))).thenReturn(bookingsResponse);

        mvc.perform(get("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", booker.getId())
                        .param("state", "ALL")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.[0].id").value(bookingResponse.getId()))
                .andExpect(jsonPath("$.[0].item.id").value(bookingItem.getId()))
                .andExpect(jsonPath("$.[0].item.name").value("item-name"))

        ;

        Mockito.verify(bookingService, Mockito.times(1))
                .getAllByBooker(booker.getId(), BookingState.ALL);
    }
}
