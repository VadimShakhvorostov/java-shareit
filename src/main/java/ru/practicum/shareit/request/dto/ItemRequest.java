package ru.practicum.shareit.request.dto;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.shareit.user.dto.User;


import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "requests")
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String description;
    @ManyToOne
    private User requestor;
    private LocalDateTime created;
}
