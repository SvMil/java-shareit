package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import lombok.SneakyThrows;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@WebMvcTest(ItemRequestController.class)
class ItemRequestControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemRequestService itemRequestService;

    private final ItemRequestDto itemRequestForTests = new ItemRequestDto(1L, "test", LocalDateTime.now(), List.of());

    @SneakyThrows
    @Test
    void getAllRequests() {
        List<ItemRequestDto> allRequests = List.of(itemRequestForTests);

        when(itemRequestService.getAllRequests(anyLong())).thenReturn(allRequests);

        String result = mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", "123"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(allRequests), result);
    }


   @SneakyThrows
   @Test
    void saveRequest() {
        when(itemRequestService.createRequest(anyLong(), any(ItemRequestDto.class))).thenReturn(itemRequestForTests);

        String result = mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", "123")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemRequestForTests)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemRequestForTests), result);
    }

    @SneakyThrows
    @Test
    void getRequests() {
        List<ItemRequestDto> allRequests = List.of(itemRequestForTests);

        when(itemRequestService.getRequests(anyLong())).thenReturn(allRequests);

        String result = mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", "123"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(allRequests), result);
    }

    @SneakyThrows
    @Test
    void getRequest() {
        when(itemRequestService.getRequestById(anyLong(), anyLong())).thenReturn(itemRequestForTests);

        String result = mockMvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", "123"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemRequestForTests), result);
    }
}