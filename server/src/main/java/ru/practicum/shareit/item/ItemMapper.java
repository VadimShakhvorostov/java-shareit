package ru.practicum.shareit.item;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    @Mapping(source = "requestId", target = "request.id")
    Item toItem(ItemDto itemDto);

    @Mapping(source = "request.id", target = "requestId")
    ItemDto toItemDto(Item item);
}
