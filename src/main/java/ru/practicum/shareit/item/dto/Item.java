package ru.practicum.shareit.item.dto;


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
    private User owner;

    @ManyToOne
    private ItemRequest request;
}
