package ru.practicum.shareit.item.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.*;

import java.util.HashMap;
import java.util.List;

@Repository
public class ItemRepository {
    private static final HashMap<Long, Item> items = new HashMap<>();
    private long counterId = 1;

    public ItemDto addItem(ItemDto itemDto, Long userId) {
        itemDto.setId(counterId++);
        Item item = ItemMapper.toEntity(itemDto);
        item.setOwner(userId);
        items.put(item.getId(), item);
        return itemDto;
    }

    public ItemDto updateItem(Long itemId, ItemDto itemDto, Item item) {
        item.setAvailable(itemDto.getAvailable());
        return ItemMapper.toDto(item);
    }

    public ItemDto getItemById(Long itemId) {
        return ItemMapper.toDto(items.get(itemId));
    }

    public List<ItemDto> getItemsByUserId(Long userId) {
        return ItemMapper.toDto(items.values().stream()
                .filter(item -> item.getOwner().equals(userId))
                .toList()
        );
    }

    public List<ItemDto> searchItems(String text) {

        String searchText = text.toLowerCase();
        return ItemMapper.toDto(
                items.values().stream()
                .filter(item -> item.getAvailable() != null && item.getAvailable())
                .filter(item -> (item.getName() != null && item.getName().toLowerCase().contains(searchText)) ||
                        (item.getDescription() != null && item.getDescription().toLowerCase().contains(searchText)))
                .toList()
        );
    }

    public boolean checkOwner(Long itemId, Long userId) {
        Item item = items.get(itemId);
        return item.getOwner().equals(userId);
    }
}
