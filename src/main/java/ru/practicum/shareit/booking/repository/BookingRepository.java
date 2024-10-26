package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Collection<Booking> findBookingByBooker_Id(Long userId);


    @Query(
            """
                    select bo
                    from Booking as bo
                    where bo.booker.id = ?1 and bo.item.id = ?2 and bo.end < ?3    \s
                    """
    )
    Optional<Booking> findBookingByUserIdAndItemId(Long userId, Long itemId, LocalDateTime localDateTime);

    //Optional<Booking> findFirstByItemIdAndStartOrderByDesc(Long itemId);


    @Query(
            """
                    select bo
                    from Booking as bo
                    where bo.booker.id = ?1 and bo.end < ?2
                    order by bo.end
                    limit 1
                    """
    )
    Optional<Booking> getLastBooking(Long itemId, LocalDateTime start);


    @Query(
            """
                    select bo
                    from Booking as bo
                    where bo.booker.id = ?1 and bo.start > ?2
                    order by bo.start
                    limit 1
                    """
    )
    Optional<Booking> getNextBooking(Long itemId, LocalDateTime end);


}
