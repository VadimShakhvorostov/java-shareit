package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;

@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {

    private ItemService itemService;

    @PostMapping
    public ItemDto saveItem(@Valid @RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") Long id) {
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
    public Collection<ItemDto> findAllItemsByUser(@RequestHeader("X-Sharer-User-Id") Long id) {
        return itemService.findAllByUser(id);
    }

    @GetMapping("/search")
    public Collection<ItemDto> search(@RequestParam String text) {
        return itemService.search(text);
    }

}
