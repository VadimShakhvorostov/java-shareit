package ru.practicum.shareit.request.service;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

@Component
public interface ItemRequestService {

    ItemRequestDto createItemRequest(ItemRequestDto itemRequestDto, long userId);

    List<ItemRequestDto> getItemRequestOwner(long userId);

    List<ItemRequestDto> getAllItemRequests(long userId);

    ItemRequestDto getItemRequestById(long requestId, long userId);


}
