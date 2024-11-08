package ru.practicum.shareit.request.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

@Component
public interface ItemRequestService {

    ItemRequestDto createItemRequest(ItemRequestDto itemRequestDto, long userId);

    List<ItemRequestDto> getItemRequestOwner(long userId);

    List<ItemRequestDto> getAllItemRequestsAll(long userId, PageRequest pageRequest);

    ItemRequestDto getItemRequestById(long requestId, long userId);


}
