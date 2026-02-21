package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.ItemRequestDto;
import ru.practicum.shareit.request.ItemRequest;

import java.util.List;

public interface ItemRequestService {

    List<ItemRequestDto> getAllRequests(Long userId);

    List<ItemRequestDto> getRequests(Long userId);

    ItemRequestDto getRequestById(Long userId, Long requestId);

    ItemRequestDto createRequest(Long userId, ItemRequestDto requestDto);

    ItemRequest findItemRequestById(Long requestId);
}
