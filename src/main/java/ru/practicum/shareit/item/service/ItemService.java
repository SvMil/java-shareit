package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    ItemDto getItemById(Long itemId);

    List<ItemDto> getItemsByUserId(Long userId);

    List<ItemDto> searchItems(String text);
    ItemDto addItem(ItemDto itemDto, Long userId);

    ItemDto updateItem(Long itemId, ItemUpdateDto itemDto, Long userId);

}
