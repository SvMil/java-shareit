package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ItemRequestServiceIntegrationTest {

    @Autowired
    private ItemRequestService itemRequestService;

    @Autowired
    private UserService userService;

    @Autowired
    private ItemService itemService;

    private UserDto requester;
    private UserDto owner;

    @BeforeEach
    void setUp() {
        requester = userService.addUser(new UserDto(null, "Requester", "requester@test.com"));
        owner = userService.addUser(new UserDto(null, "Owner", "owner@test.com"));
    }

    @Test
    void createRequest_shouldCreateRequestSuccessfully() {
        ItemRequestDto createDto = new ItemRequestDto();
        createDto.setDescription("Нужна дрель");
        ItemRequestDto result = itemRequestService.createRequest(requester.getId(), createDto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("Нужна дрель", result.getDescription());
        assertNotNull(result.getCreated());
        assertTrue(result.getItems().isEmpty());
    }

    @Test
    void createRequest_shouldThrowExceptionWhenUserNotFound() {
        ItemRequestDto createDto = new ItemRequestDto();
        createDto.setDescription("Нужна дрель");
        Long nonExistentUserId = 999L;

        assertThrows(NotFoundException.class, () ->
            itemRequestService.createRequest(nonExistentUserId, createDto)
        );
    }

    @Test
    void getOwnRequests_shouldReturnUserRequests() throws InterruptedException {
        itemRequestService.createRequest(requester.getId(), new ItemRequestDto("Нужна дрель"));
        Thread.sleep(10);
        itemRequestService.createRequest(requester.getId(), new ItemRequestDto("Нужен молоток"));
        List<ItemRequestDto> result = itemRequestService.getRequests(requester.getId());

        assertEquals(2, result.size());
        assertEquals("Нужен молоток", result.get(0).getDescription());
        assertEquals("Нужна дрель", result.get(1).getDescription());
    }

    @Test
    void getAllRequests_shouldReturnOtherUsersRequests() {
        ItemRequestDto request1 = itemRequestService.createRequest(requester.getId(), new ItemRequestDto("Нужна дрель"));
        ItemRequestDto request2 = itemRequestService.createRequest(owner.getId(), new ItemRequestDto("Нужен молоток"));
        List<ItemRequestDto> result = itemRequestService.getAllRequests(requester.getId());

        assertEquals(1, result.size());
        assertEquals("Нужен молоток", result.get(0).getDescription());
    }

    @Test
    void getAllRequests_shouldNotReturnOwnRequests() {
        ItemRequestDto request1 = itemRequestService.createRequest(requester.getId(), new ItemRequestDto());
        request1.setDescription("Нужна дрель");
        ItemRequestDto request2 = itemRequestService.createRequest(requester.getId(), new ItemRequestDto());
        request2.setDescription("Нужен молоток");
        List<ItemRequestDto> result = itemRequestService.getAllRequests(requester.getId());

        assertTrue(result.isEmpty());
    }

    @Test
    void getRequestById_shouldReturnRequestWithItems() {
        ItemRequestDto request = itemRequestService.createRequest(
                requester.getId(), new ItemRequestDto("Нужна дрель"));

        ItemDto itemDto = new ItemDto(null, "Дрель", "Мощная дрель", true, request.getId(), null);
        itemService.addItem(itemDto, owner.getId());
        ItemRequestDto result = itemRequestService.getRequestById(owner.getId(), request.getId());

        assertNotNull(result);
        assertEquals(request.getId(), result.getId());
        assertEquals("Нужна дрель", result.getDescription());
        assertEquals(1, result.getItems().size());
        assertEquals("Дрель", result.getItems().get(0).getName());
    }

    @Test
    void getRequestById_shouldThrowExceptionWhenRequestNotFound() {
        Long nonExistentRequestId = 999L;

        assertThrows(NotFoundException.class, () ->
            itemRequestService.getRequestById(nonExistentRequestId, requester.getId())
        );
    }

    @Test
    void getRequestById_shouldThrowExceptionWhenUserNotFound() {
        ItemRequestDto request = itemRequestService.createRequest(
                requester.getId(), new ItemRequestDto());
        request.setDescription("Нужна дрель");
        Long nonExistentUserId = 999L;

        assertThrows(NotFoundException.class, () ->
            itemRequestService.getRequestById(nonExistentUserId, request.getId())
        );
    }
}

