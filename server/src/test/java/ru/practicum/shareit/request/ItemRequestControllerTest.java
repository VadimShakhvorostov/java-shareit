package ru.practicum.shareit.request;

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
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ItemRequestController.class)
public class ItemRequestControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemRequestService itemRequestService;

    @Autowired
    private MockMvc mvc;

    private ItemRequestDto itemRequest;
    private ItemRequestDto itemRequestResponse;
    private ItemDto itemResponse;
    private List<ItemRequestDto> itemRequestResponseList;
    private static final long TEST_USER_ID = 1;

    @BeforeEach
    public void initData() {

        LocalDateTime created = LocalDateTime.now();

        itemResponse = new ItemDto();
        itemResponse.setId(1);
        itemResponse.setName("ItemResponse-test");
        itemResponse.setDescription("ItemResponse-test");

        itemRequest = new ItemRequestDto();
        itemRequest.setDescription("description-test");

        itemRequestResponse = new ItemRequestDto();
        itemRequestResponse.setId(1L);
        itemRequestResponse.setDescription("description-test");
        itemRequestResponse.setItems(Arrays.asList(itemResponse));
        itemRequestResponse.setCreated(created);

        itemRequestResponseList = Arrays.asList(itemRequestResponse);
    }


    @Test
    @DisplayName("Should crete request")
    void createItemRequestTest() throws Exception {

        when(itemRequestService.createItemRequest(any(ItemRequestDto.class), anyLong())).thenReturn(itemRequestResponse);

        mvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemRequest))
                        .header("X-Sharer-User-Id", TEST_USER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.id").value(itemRequestResponse.getId()))
                .andExpect(jsonPath("$.description").value(itemRequestResponse.getDescription()))
                .andExpect(jsonPath("$.items.[0].name").value("ItemResponse-test"));

        Mockito.verify(itemRequestService, Mockito.times(1)).createItemRequest(itemRequest, TEST_USER_ID);
    }


    @Test
    @DisplayName("Should return list request by owner id")
    void getItemRequestsTest() throws Exception {

        when(itemRequestService.getItemRequestOwner(anyLong())).thenReturn(itemRequestResponseList);

        mvc.perform(get("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemRequest))
                        .header("X-Sharer-User-Id", TEST_USER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.[0].id").value(itemRequestResponse.getId()))
                .andExpect(jsonPath("$.[0].description").value(itemRequestResponse.getDescription()))
                .andExpect(jsonPath("$.[0].items.[0].name").value("ItemResponse-test"));

        Mockito.verify(itemRequestService, Mockito.times(1)).getItemRequestOwner(TEST_USER_ID);
    }

    @Test
    @DisplayName("Should return list all request")
    void getAllItemRequestsTest() throws Exception {

        when(itemRequestService.getAllItemRequests(anyLong())).thenReturn(itemRequestResponseList);

        mvc.perform(get("/requests/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", TEST_USER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.[0].id").value(itemRequestResponse.getId()))
                .andExpect(jsonPath("$.[0].description").value(itemRequestResponse.getDescription()))
                .andExpect(jsonPath("$.[0].items.[0].name").value("ItemResponse-test"));

        Mockito.verify(itemRequestService, Mockito.times(1)).getAllItemRequests(TEST_USER_ID);
    }

    @Test
    @DisplayName("Should return request by id")
    void getItemRequestById() throws Exception {

        when(itemRequestService.getItemRequestById(anyLong(), anyLong())).thenReturn(itemRequestResponse);

        mvc.perform(get("/requests/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", TEST_USER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.id").value(itemRequestResponse.getId()))
                .andExpect(jsonPath("$.description").value(itemRequestResponse.getDescription()))
                .andExpect(jsonPath("$.items.[0].name").value("ItemResponse-test"));

        Mockito.verify(itemRequestService, Mockito.times(1)).getItemRequestById(1L, TEST_USER_ID);
    }

}
