package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.comment.CreateCommentRequest;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemForOwnerDto;
import ru.practicum.shareit.item.dto.ItemFullResponseDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
class ItemControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    private final ItemFullResponseDto itemReturnDto = new ItemFullResponseDto(1L,
            "Test Item",
            "Test Description",
            true, null, null, List.of());

    private final ItemDto itemDto = new ItemDto(1L, "Test Item", "Test Description", true, null, List.of(), null, null);

    private final ItemForOwnerDto itemForOwnerDto = new ItemForOwnerDto(1L,
            "Test Item",
            "Test Description",
            true, null, null, List.of());

    @SneakyThrows
    @Test
    void getItemById() {
        when(itemService.getItemById(anyLong())).thenReturn(itemDto);

        String result = mockMvc.perform(get("/items/{id}", 1L)
                        .header("X-Sharer-User-Id", "123"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemDto), result);
    }

    @SneakyThrows
    @Test
    void getItemsByOwner() {
        List<ItemForOwnerDto> items = List.of(itemForOwnerDto);
        when(itemService.getItemsByOwner(anyLong())).thenReturn(items);

        String result = mockMvc.perform(get("/items", 1L)
                        .header("X-Sharer-User-Id", "123"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(items), result);
    }

    @SneakyThrows
    @Test
    void searchItems() {

        when(itemService.searchItems(anyString())).thenReturn(List.of(itemDto));

        String result = mockMvc.perform(get("/items/search")
                        .param("text", "test"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(List.of(itemDto)), result);
    }

    @SneakyThrows
    @Test
    void addItem() {

        when(itemService.addItem(any(ItemDto.class), anyLong())).thenReturn(itemDto);

        String result = mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", "123")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemDto), result);
    }

    @SneakyThrows
    @Test
    void updateItem() {

        when(itemService.updateItem(anyLong(), any(ItemUpdateDto.class), anyLong())).thenReturn(itemDto);

        String result = mockMvc.perform(patch("/items/{id}", 1L)
                        .header("X-Sharer-User-Id", "123")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemDto), result);
    }

    @SneakyThrows
    @Test
    void addComment() {

        CreateCommentRequest  createCommentRequest = new  CreateCommentRequest();
        createCommentRequest.setText("test");

        CommentDto commentReturnDto = new CommentDto();
        commentReturnDto.setText("test");

        when(itemService.addComment(anyLong(), anyLong(), eq(createCommentRequest))).thenReturn(commentReturnDto);

        String result = mockMvc.perform(post("/items/{itemId}/comment", 1L)
                        .header("X-Sharer-User-Id", "123")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(createCommentRequest)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(commentReturnDto), result);
    }
}