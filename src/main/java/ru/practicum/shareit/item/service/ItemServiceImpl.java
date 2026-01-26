package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.NotOwnerException;
import ru.practicum.shareit.item.dao.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.user.dao.*;

import java.util.List;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public ItemDto getItemById(Long itemId) {
        return itemRepository.getItemById(itemId);
    }

    @Override
    public List<ItemDto> getItemsByUserId(Long userId) {
        checkUserExists(userId);
        return itemRepository.getItemsByUserId(userId);
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }
        return itemRepository.searchItems(text);
    }

    @Override
    public ItemDto addItem(ItemDto itemDto, Long userId) {
        checkUserExists(userId);
        return itemRepository.addItem(itemDto, userId);
    }

    @Override
    public ItemDto updateItem(Long itemId, ItemDto itemDto, Long userId) {
        checkUserExists(userId);
        checkOwner(itemId, userId);
        return itemRepository.updateItem(itemId, itemDto);
    }

    private void checkOwner(Long itemId, Long userId) {
        if (!itemRepository.checkOwner(itemId, userId)) {
            throw new NotOwnerException("Пользователь не является владельцем предмета");
        }
    }

    private void checkUserExists(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь не найден");
        }
    }


}
