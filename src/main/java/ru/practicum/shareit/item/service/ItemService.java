package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

@Component
public interface ItemService {

    ItemDto findById(Long id);

    Collection<ItemDto> findAllByUser(Long id);

    Collection<ItemDto> search(String text);

    ItemDto update(Long id, Long userId, ItemDto itemDto);

    ItemDto save(ItemDto itemDto, Long id);

}
