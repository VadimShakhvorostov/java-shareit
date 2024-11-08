package ru.practicum.shareit.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.dto.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    @Query("""
            SELECT u
            FROM User u
            where lower(u.email) like  lower(concat('%', ?1,'%'))
            and u.id != ?2
            """)
    Optional<User> getUserByEmailAndId(String email, long userId);

    boolean existsByEmailIgnoreCase(String email);


}
