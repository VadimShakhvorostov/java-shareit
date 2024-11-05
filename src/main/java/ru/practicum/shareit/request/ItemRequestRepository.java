package ru.practicum.shareit.request;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.dto.ItemRequest;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
}
