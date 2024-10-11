package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

@Component
public interface ItemService {

    public ItemDto findById(Long id);

    public Collection<ItemDto> findAllByUser(Long id);

    public Collection<ItemDto> search(String text);

    public ItemDto update(Long id, Long userId, ItemDto itemDto);

    public ItemDto save(ItemDto itemDto, Long id);

}
