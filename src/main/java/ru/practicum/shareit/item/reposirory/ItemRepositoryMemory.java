package ru.practicum.shareit.item.reposirory;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Repository
public class ItemRepositoryMemory implements ItemRepository {
    private final Map<Long, Item> items = new HashMap<>();
    private long idCount = 0;

    @Override
    public Item save(Item item) {
        item.setId(getId());
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Optional<Item> findById(Long id) {
        return Optional.ofNullable(items.get(id));
    }

    @Override
    public Item update(Item item) {
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Collection<Item> findAllByUser(Long id) {
        return items.values().stream().filter(item -> item.getOwner().getId() == id).toList();
    }

    @Override
    public Collection<Item> search(String text) {
        return items
                .values()
                .stream()
                .filter(item -> item.getName().toLowerCase().contains(text)
                        || item.getDescription().toLowerCase().contains(text))
                .filter(item -> item.getAvailable().equals(true))
                .toList();
    }

    private long getId() {
        idCount++;
        return idCount;
    }
}
