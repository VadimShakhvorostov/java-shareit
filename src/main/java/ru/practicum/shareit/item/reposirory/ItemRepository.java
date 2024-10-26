package ru.practicum.shareit.item.reposirory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    Collection<Item> findAllByOwnerId(Long id);

    @Query("""
            select it
            from Item it 
            where (upper(it.name) like upper(concat('%', ?1, '%')) 
            or upper(it.description) like upper(concat('%', ?1, '%')))
            and it.available = true            
            """
    )
    Collection<Item> search(String searchText);


}

