package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

@Component
public interface ItemService {

    public ItemDto findById(Long id);

    public Collection<ItemDto> findAllByUser(Long id);

    public Collection<ItemDto> search(String text, Long id);

    public ItemDto update(Long id, Long userId, Item item);

    public ItemDto save(Item item, Long id);

}
