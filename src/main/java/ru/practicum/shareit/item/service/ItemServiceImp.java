package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.CommentException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.CommentMapper;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.reposirory.CommentRepository;
import ru.practicum.shareit.item.reposirory.ItemRepository;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequest;
import ru.practicum.shareit.user.dto.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

@Service
@AllArgsConstructor
@Slf4j
public class ItemServiceImp implements ItemService {
    private ItemRepository itemRepository;
    private UserRepository userRepository;
    private ItemMapper itemMapper;
    private ItemRequestRepository itemRequestRepository;
    private CommentMapper commentMapper;
    private CommentRepository commentRepository;
    private BookingRepository bookingRepository;

    @Override
    public ItemDto findById(Long itemId) {
        log.trace("findById itemId = {}", itemId);

        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Вещ с id: " + " не найдена"));
        ItemDto itemDto = itemMapper.toItemDto(item);
        Set<Comment> comments = commentRepository.findAllByItem_Id(itemId);
        log.trace("comments = {}", comments);
        Set<CommentDto> commentDtos = commentMapper.toListCommentDto(comments);
        log.trace("commentDtos = {}", commentDtos);
        itemDto.setComments(commentDtos);

        if (bookingRepository.findByItemId(itemId).size() > 1) {
            Booking lastBooking = bookingRepository.getLastBooking(itemId, LocalDateTime.now()).orElse(null);
            Booking nextBooking = bookingRepository.getNextBooking(itemId, LocalDateTime.now()).orElse(null);
            log.trace("findById nextBooking = {}, lastBooking = {}", nextBooking, lastBooking);
            itemDto.setLastBooking(lastBooking);
            itemDto.setNextBooking(nextBooking);
            log.trace("findByIdAfterSet nextBooking = {}, lastBooking = {}", nextBooking, lastBooking);
        }
        itemDto.setLastBooking(null);
        itemDto.setNextBooking(null);

        return itemDto;
    }

    @Override
    public Collection<ItemDto> findAllItemByUser(Long userId) {
        log.trace("findAllItemByUser userId = {}", userId);
        return itemRepository.findAllByOwnerId(userId).stream().map(itemMapper::toItemDto).toList();
    }

    @Override
    public Collection<ItemDto> search(String text) {
        log.trace("search text: {}", text);
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        final String searchText = text.toLowerCase();
        return itemRepository.search(searchText).stream().map(itemMapper::toItemDto).toList();

    }

    @Override
    public ItemDto update(Long itemId, Long userId, ItemDto itemDto) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Вещ с id: " + " не найдена"));
        if (item.getOwner().getId() != userId) {
            throw new NotFoundException("Пользователь с id: " + itemId + " не является владельцем");
        }
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        ItemDto itemDtoResponse = itemMapper.toItemDto(item);
        Booking lastBooking = bookingRepository.getLastBooking(itemId, LocalDateTime.now()).orElse(null);
        Booking nextBooking = bookingRepository.getNextBooking(itemId, LocalDateTime.now()).orElse(null);

        log.trace("itemDtoUpdate nextBooking = {}, lastBooking = {}", nextBooking, lastBooking);

        itemDto.setLastBooking(lastBooking);
        itemDto.setNextBooking(nextBooking);

        log.trace("itemDtoUpdateAfterSet nextBooking = {}, lastBooking = {}", nextBooking, lastBooking);

        return itemDtoResponse;

    }

    @Override
    public ItemDto save(ItemDto itemDto, Long userId) {
        log.trace("save item name: {}, description: {}, userId: {} ", itemDto.getName(), itemDto.getDescription(), userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с id: " + userId + " не найден"));
        ItemRequest itemRequest = itemRequestRepository.findById(user.getId()).orElse(null);
        Item item = itemMapper.toItem(itemDto);
        item.setOwner(user);
        item.setRequest(itemRequest);
        return itemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public CommentDto createComment(Long itemId, Long userId, CommentDto commentDto) {
        log.trace("createComment itemId = {}, userId={}, commentDto = {}", itemId, userId, commentDto);

        Comment comment = commentMapper.toComment(commentDto);
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Вещ с id: " + " не найдена"));
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с id: " + userId + " не найден"));

        if (bookingRepository
                .findBookingByUserIdAndItemIdRented(
                        user.getId(),
                        item.getId(),
                        LocalDateTime.now()).isEmpty()) {
            throw new CommentException("User не брал вещь в аренду");
        }

        comment.setItem(item);
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());
        return commentMapper.toCommentDto(commentRepository.save(comment));

    }
}
