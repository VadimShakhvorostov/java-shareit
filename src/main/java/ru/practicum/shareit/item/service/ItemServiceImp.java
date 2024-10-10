package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.reposirory.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.Collection;

@Service
@AllArgsConstructor
@Slf4j
public class ItemServiceImp implements ItemService {
    ItemRepository itemRepository;
    UserRepository userRepository;

    @Override
    public ItemDto findById(Long id) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new NotFoundException("Вещ с id: " + " не найдена"));
        return ItemMapper.toItemDto(item);
    }

    @Override
    public Collection<ItemDto> findAllByUser(Long id) {
        return itemRepository.findAllByUser(id).stream().map(ItemMapper::toItemDto).toList();
    }

    @Override
    public Collection<ItemDto> search(String text, Long id) {
        log.trace("search text: {} userId: {}", text, id);
        if (text.isBlank()) {
            return new ArrayList<ItemDto>();
        }
        String searchText = text.toLowerCase();
        return itemRepository.search(searchText).stream().map(ItemMapper::toItemDto).toList();
    }

    @Override
    public ItemDto update(Long id, Long userId, Item updateItem) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new NotFoundException("Вещ с id: " + " не найдена"));
        if (item.getOwner().getId() != userId) {
            throw new NotFoundException("Пользователь с id: " + id + " не является владельцем");
        }
        if (updateItem.getName() != null) {
            item.setName(updateItem.getName());
        }
        if (updateItem.getDescription() != null) {
            item.setDescription(updateItem.getDescription());
        }
        if (updateItem.getAvailable() != null) {
            item.setAvailable(updateItem.getAvailable());
        }

        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto save(Item item, Long id) {
        log.trace("save item name: {}, description: {}, userId: {} ", item.getName(), item.getDescription(), id);
        item.setOwner(userRepository.findById(id).orElseThrow(() -> new NotFoundException("Пользователь с id: " + id + " не найден")));
        return ItemMapper.toItemDto(itemRepository.save(item));
    }
}
