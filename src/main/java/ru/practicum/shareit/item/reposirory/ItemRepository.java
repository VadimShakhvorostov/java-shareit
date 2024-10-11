package ru.practicum.shareit.item.reposirory;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.Optional;

public interface ItemRepository {

    Item save(Item item);

    Optional<Item> findById(Long id);

    Item update(Item item);

    Collection<Item> findAllByUser(Long id);

    Collection<Item> search(String text);
}
