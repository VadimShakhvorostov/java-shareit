package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import lombok.*;

import ru.practicum.shareit.request.dto.ItemRequest;
import ru.practicum.shareit.user.dto.User;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private String description;

    private Boolean available;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @ManyToOne
    @JoinColumn(name = "requests_id")
    private ItemRequest request;
}
