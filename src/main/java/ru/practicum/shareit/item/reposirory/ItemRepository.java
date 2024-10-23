package ru.practicum.shareit.item.reposirory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.Item;

import java.util.Collection;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    Collection<Item> findAllByOwnerId(Long id);

    // Collection<Item> findByNameOrDescriptionContainingIgnoreCase(String searchText);

    Collection<Item> findByNameContainingIgnoreCase(String searchText);


}

