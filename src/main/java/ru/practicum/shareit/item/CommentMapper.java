package ru.practicum.shareit.item;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    Comment toComment(CommentDto commentDto);

    @Mapping(source = "author.name", target = "authorName")
    CommentDto toCommentDto(Comment comment);
}
