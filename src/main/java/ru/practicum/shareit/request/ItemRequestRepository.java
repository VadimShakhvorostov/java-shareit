package ru.practicum.shareit.request;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.request.dto.ItemRequest;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {


    List<ItemRequest> findAllByRequestorId(long userId);

    @Query(
            """
                    select ir
                    from ItemRequest as ir
                    """
    )
    List<ItemRequest> findAllItemRequest(PageRequest pageRequest);
}
