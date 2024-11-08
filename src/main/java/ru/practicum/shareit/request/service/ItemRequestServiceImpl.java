package ru.practicum.shareit.request.service;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.reposirory.ItemRepository;
import ru.practicum.shareit.request.ItemRequestMapper;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.dto.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    ItemRequestRepository itemRequestRepository;
    ItemRequestMapper itemRequestMapper;
    private ItemRepository itemRepository;
    private UserRepository userRepository;
    private ItemMapper itemMapper;

    @Override
    public ItemRequestDto createItemRequest(@Valid ItemRequestDto itemRequestDto, long userId) {
        User user = getUser(userId);
        ItemRequest itemRequest = itemRequestMapper.toItemRequest(itemRequestDto);
        itemRequest.setRequestor(user);
        itemRequest.setCreated(LocalDateTime.now());
        return itemRequestMapper.toItemRequestDto(itemRequestRepository.save(itemRequest));
    }

    @Override
    public List<ItemRequestDto> getItemRequestOwner(long userId) {
        getUser(userId);
        List<ItemRequest> itemRequests = itemRequestRepository.findAllByRequestorId(userId);
        return addItemsToItemRequest(itemRequests);
    }

    @Override
    public List<ItemRequestDto> getAllItemRequestsAll(long userId, PageRequest pageRequest) {
        getUser(userId);
        List<ItemRequest> itemRequests = itemRequestRepository.findAllItemRequest(pageRequest);
        return addItemsToItemRequest(itemRequests);
    }

    @Override
    public ItemRequestDto getItemRequestById(long requestId, long userId) {
        log.trace("getItemRequestById requestId = {}, userId = {}", requestId, userId);
        ItemRequest itemRequest = itemRequestRepository.findById(requestId).orElseThrow(() -> new NotFoundException("Запрос с id: " + requestId + " не найден"));
        getUser(userId);
        return addItemsToItemRequest(Collections.singletonList(itemRequest)).getFirst();
    }


    private List<ItemRequestDto> addItemsToItemRequest(List<ItemRequest> itemRequests) {
        List<Long> itemRequestIds = itemRequests.stream().map(ItemRequest::getId).toList();
        List<Item> items = itemRepository.findAllByRequestIdIn(itemRequestIds);
        List<ItemRequestDto> itemRequestDtos = itemRequests.stream().map(itemRequestMapper::toItemRequestDto).toList();
        for (ItemRequestDto itemRequestDto : itemRequestDtos) {
            for (Item item : items) {
                if (item.getRequest().getId() == itemRequestDto.getId()) {
                    itemRequestDto.addItems(itemMapper.toItemDto(item));
                }
            }
        }
        return itemRequestDtos;
    }

    private User getUser(long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с id: " + userId + " не найден"));
    }
}
