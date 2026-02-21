package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    boolean existsById(Long id);

    List<Item> findByRequestId(Long requestId);

    List<Item> findByOwnerId(Long ownerId);

    @Query("SELECT i FROM Item i " +
            "WHERE i.available = true " +
            "AND (LOWER(i.name) LIKE LOWER(CONCAT('%', :text, '%')) " +
            "OR LOWER(i.description) LIKE LOWER(CONCAT('%', :text, '%')))")
    List<Item> search(@Param("text") String text);

    @Query("SELECT i FROM Item i " +
            "LEFT JOIN FETCH i.comments " +
            "WHERE i.id = :id")
    Optional<Item> findByIdWithComments(@Param("id") Long id);
}
