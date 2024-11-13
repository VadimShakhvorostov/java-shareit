package ru.practicum.shareit.item;

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
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
public class ItemControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemService itemService;

    @Autowired
    private MockMvc mvc;

    private ItemDto itemRequest;
    private ItemDto itemResponse;
    private ItemDto itemResponseUpdate;
    private List<ItemDto> itemResponseList;
    private CommentDto commentRequest;
    private CommentDto commentResponse;
    private static final long TEST_USER_ID = 1;
    private static final String TEST_SEARCH_TEXT = "test";

    @BeforeEach
    public void initData() {
        itemRequest = new ItemDto();
        itemRequest.setName("ItemRequest-test");
        itemRequest.setDescription("ItemRequest-test");

        itemResponse = new ItemDto();
        itemResponse.setId(1);
        itemResponse.setName("ItemRequest-test");
        itemResponse.setDescription("ItemRequest-test");

        itemResponseUpdate = new ItemDto();
        itemResponseUpdate.setId(1);
        itemResponseUpdate.setName("ItemRequest-test-update");
        itemResponseUpdate.setDescription("ItemRequest-test-update");

        itemResponseList = new ArrayList<>(Arrays.asList(itemResponse));

        commentRequest = new CommentDto();
        commentRequest.setText("test-text");

        commentResponse = new CommentDto();
        commentResponse.setId(1L);
        commentResponse.setText("test-text");
        commentResponse.setAuthorName("user-name");
        commentResponse.setCreated(LocalDateTime.now());
    }

    @Test
    @DisplayName("Should crete item")
    void saveItemTest() throws Exception {

        when(itemService.save(any(ItemDto.class), anyLong())).thenReturn(itemResponse);

        mvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemRequest))
                        .header("X-Sharer-User-Id", TEST_USER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.id").value(itemResponse.getId()))
                .andExpect(jsonPath("$.name").value(itemResponse.getName()))
                .andExpect(jsonPath("$.description").value(itemResponse.getDescription()));

        Mockito.verify(itemService, Mockito.times(1)).save(itemRequest, TEST_USER_ID);
    }

    @Test
    @DisplayName("Should update item")
    void updateItemTest() throws Exception {

        when(itemService.update(anyLong(), anyLong(), any(ItemDto.class))).thenReturn(itemResponseUpdate);

        mvc.perform(patch("/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemRequest))
                        .header("X-Sharer-User-Id", TEST_USER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.id").value(itemResponseUpdate.getId()))
                .andExpect(jsonPath("$.name").value(itemResponseUpdate.getName()))
                .andExpect(jsonPath("$.description").value(itemResponseUpdate.getDescription()));

        Mockito.verify(itemService, Mockito.times(1)).update(itemResponseUpdate.getId(), TEST_USER_ID, itemRequest);
    }

    @Test
    @DisplayName("Should return item by id")
    void findItemByIdTest() throws Exception {

        when(itemService.findById(anyLong())).thenReturn(itemResponse);

        mvc.perform(get("/items/1")
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.id").value(itemResponse.getId()))
                .andExpect(jsonPath("$.name").value(itemResponse.getName()))
                .andExpect(jsonPath("$.description").value(itemResponse.getDescription()));

        Mockito.verify(itemService, Mockito.times(1)).findById(itemResponse.getId());
    }

    @Test
    @DisplayName("Should return all items by user id")
    void findAllItemsByUserTest() throws Exception {

        when(itemService.findAllItemByUser(anyLong())).thenReturn(itemResponseList);

        mvc.perform(get("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", TEST_USER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.[0].id").value(itemResponse.getId()))
                .andExpect(jsonPath("$.[0].name").value(itemResponse.getName()))
                .andExpect(jsonPath("$.[0].description").value(itemResponse.getDescription()));

        Mockito.verify(itemService, Mockito.times(1)).findAllItemByUser(TEST_USER_ID);
    }

    @Test
    @DisplayName("Should return all items by user id")
    void searchTest() throws Exception {

        when(itemService.search(anyString())).thenReturn(itemResponseList);

        mvc.perform(get("/items/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("text", TEST_SEARCH_TEXT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.[0].id").value(itemResponse.getId()))
                .andExpect(jsonPath("$.[0].name").value(itemResponse.getName()))
                .andExpect(jsonPath("$.[0].description").value(itemResponse.getDescription()));

        Mockito.verify(itemService, Mockito.times(1)).search(TEST_SEARCH_TEXT);
    }

    @Test
    @DisplayName("Should create comment")
    void createComment() throws Exception {

        when(itemService.createComment(anyLong(), anyLong(), any(CommentDto.class))).thenReturn(commentResponse);

        mvc.perform(post("/items/1/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentRequest))
                        .header("X-Sharer-User-Id", TEST_USER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.id").value(commentResponse.getId()))
                .andExpect(jsonPath("$.text").value(commentResponse.getText()));

        Mockito.verify(itemService, Mockito.times(1)).createComment(1L, TEST_USER_ID, commentRequest);
    }

}
