package ru.practicum.shareit.request;

import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.ItemShortResponseDto;

import java.util.List;

public class ItemRequestMapper {
    public static ItemRequest toEntity(ItemRequestDto itemRequestDto) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(itemRequestDto.getDescription());

        return itemRequest;
    }

    public static ItemRequestDto toDto(ItemRequest itemRequest) {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setId(itemRequest.getId());
        dto.setDescription(itemRequest.getDescription());
        dto.setCreated(itemRequest.getCreated());

        return dto;
    }

    public static List<ItemRequestDto> toDto(List<ItemRequest> itemRequests) {
        return itemRequests.stream()
                .map(ItemRequestMapper::toDto)
                .toList();
    }

    public static ItemShortResponseDto toShortDto(Item item) {
        ItemShortResponseDto dto = new ItemShortResponseDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setOwnerId(item.getOwner().getId());

        return dto;
    }

    public static List<ItemShortResponseDto> toShortDto(List<Item> items) {
        return items.stream()
                .map(ItemRequestMapper::toShortDto)
                .toList();
    }
}
