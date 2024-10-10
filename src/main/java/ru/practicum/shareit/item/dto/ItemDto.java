package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor

@Data
public class ItemDto {
    private long id;
    private String name;
    private String description;
    private boolean available;
    private Long owner;
    private Long request;


}
