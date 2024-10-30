package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Optional<Booking> findByIdAndItemOwnerId(Long bookingId, Long userid);

    List<Booking> findAllByBookerIdOrderByStartDesc(Long userId);

    List<Booking> findAllByBookerIdAndStatusOrderByStartDesc(Long userId, String status);

    @Query(
            """
                    select bo
                    from Booking as bo
                    where bo.booker.id = ?1
                    and bo.start < ?2
                    and bo.end > ?2
                    order by bo.start desc
                    """
    )
    List<Booking> findCurrentBookingsForBooker(Long userId, LocalDateTime time);

    @Query(
            """
                    select bo
                    from Booking as bo
                    where bo.booker.id = ?1
                    and bo.start > ?2
                    order by bo.start desc
                    """
    )
    List<Booking> findFutureBookingsForBooker(Long userId, LocalDateTime time);

    @Query(
            """
                    select bo
                    from Booking as bo
                    where bo.booker.id = ?1
                    and bo.end < ?2
                    order by bo.start desc
                    """
    )
    List<Booking> findPastBookingsForBooker(Long userId, LocalDateTime time);


    @Query(
            """
                    select bo
                    from Booking as bo
                    where bo.booker.id = ?1
                    and bo.item.id = ?2
                    and bo.end < ?3
                    """
    )
    Optional<Booking> findBookingByUserIdAndItemIdRented(Long userId, Long itemId, LocalDateTime localDateTime);

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

    List<Booking> findAllByItemOwnerIdOrderByStartDesc(Long userId);

    List<Booking> findAllByItemOwnerIdAndStatusOrderByStartDesc(Long userId, String status);

    @Query(
            """
                    select bo
                    from Booking as bo
                    where bo.item.owner.id = ?1
                    and bo.start < ?2
                    and bo.end > ?2
                    order by bo.start desc
                    """
    )
    List<Booking> findCurrentBookingsForOwner(Long userId, LocalDateTime time);

    @Query(
            """
                    select bo
                    from Booking as bo
                    where bo.item.owner.id = ?1
                    and bo.start > ?2
                    order by bo.start desc
                    """
    )
    List<Booking> findFutureBookingsForOwner(Long userId, LocalDateTime time);

    @Query(
            """
                    select bo
                    from Booking as bo
                    where bo.item.owner.id = ?1
                    and bo.end < ?2
                    order by bo.start desc
                    """
    )
    List<Booking> findPastBookingsForOwner(Long userId, LocalDateTime time);

}
