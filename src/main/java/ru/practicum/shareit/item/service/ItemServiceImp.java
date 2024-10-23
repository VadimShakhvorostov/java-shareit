package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.Item;
import ru.practicum.shareit.item.reposirory.ItemRepository;
import ru.practicum.shareit.user.dto.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.Collection;

@Service
@AllArgsConstructor
@Slf4j
public class ItemServiceImp implements ItemService {
    private ItemRepository itemRepository;
    private UserRepository userRepository;

    @Override
    public ItemDto findById(Long id) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new NotFoundException("Вещ с id: " + " не найдена"));
        return ItemMapper.toItemDto(item);
    }

    @Override
    public Collection<ItemDto> findAllByUser(Long id) {
        return itemRepository.findAllByOwnerId(id).stream().map(ItemMapper::toItemDto).toList();
    }

    @Override
    public Collection<ItemDto> search(String text) {
        log.trace("search text: {}", text);
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        final String searchText = text.toLowerCase();
        return itemRepository.findByNameContainingIgnoreCase(searchText).stream().map(ItemMapper::toItemDto).toList();
    }

    @Override
    public ItemDto update(Long id, Long userId, ItemDto itemDto) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new NotFoundException("Вещ с id: " + " не найдена"));
        if (item.getOwner().getId() != userId) {
            throw new NotFoundException("Пользователь с id: " + id + " не является владельцем");
        }
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }

        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto save(ItemDto itemDto, Long id) {
        log.trace("save item name: {}, description: {}, userId: {} ", itemDto.getName(), itemDto.getDescription(), id);
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("Пользователь с id: " + id + " не найден"));
        Item item = ItemMapper.toItem(itemDto, user);
        return ItemMapper.toItemDto(itemRepository.save(item));
    }
}
