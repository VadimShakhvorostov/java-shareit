package ru.practicum.shareit.item.reposirory;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.Optional;

@Component
public interface ItemRepository {

    Item save(Item item);

    Optional<Item> findById(Long id);

    Item update(Item item);

    public Collection<Item> findAllByUser(Long id);

    Collection<Item> search(String text);
}
