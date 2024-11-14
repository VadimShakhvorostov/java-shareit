package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestDto {

    private long id;

    private String description;

    private LocalDateTime created;

    private List<ItemDto> items = new ArrayList<>();

    public void addItems(ItemDto itemDto) {
        items.add(itemDto);
    }

    @Override
    public String toString() {
        return "ItemRequestDto{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", created=" + created +
                ", items=" + items +
                '}';
    }
}
