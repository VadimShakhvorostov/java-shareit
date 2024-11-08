package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@AllArgsConstructor
public class ItemRequestController {

    private ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto createItemRequest(
            @Valid @RequestBody ItemRequestDto itemRequestDto,
            @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestService.createItemRequest(itemRequestDto, userId);
    }

    @GetMapping
    public List<ItemRequestDto> getItemRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestService.getItemRequestOwner(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllItemRequests(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(defaultValue = "0", required = false) int from,
            @RequestParam(defaultValue = "10", required = false) int size) {
        return itemRequestService.getAllItemRequestsAll(userId, PageRequest.of(from, size));
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getItemRequestById(
            @PathVariable Long requestId,
            @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestService.getItemRequestById(requestId, userId);
    }


}
