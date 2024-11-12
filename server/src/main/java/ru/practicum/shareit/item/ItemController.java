package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;

@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {

    private ItemService itemService;

    @PostMapping
    public ItemDto saveItem(
            @RequestBody ItemDto itemDto,
            @RequestHeader("X-Sharer-User-Id") Long id) {
        return itemService.save(itemDto, id);
    }

    @PatchMapping("/{id}")
    public ItemDto updateItem(
            @PathVariable Long id,
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestBody ItemDto itemDto) {
        return itemService.update(id, userId, itemDto);
    }

    @GetMapping("/{id}")
    public ItemDto findItemById(@PathVariable Long id) {
        return itemService.findById(id);
    }

    @GetMapping
    public Collection<ItemDto> findAllItemsByUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.findAllItemByUser(userId);
    }

    @GetMapping("/search")
    public Collection<ItemDto> search(@RequestParam String text) {
        return itemService.search(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(
            @PathVariable Long itemId,
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestBody CommentDto commentDto) {
        return itemService.createComment(itemId, userId, commentDto);
    }
}
