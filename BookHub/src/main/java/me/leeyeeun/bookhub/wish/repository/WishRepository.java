package me.leeyeeun.bookhub.wish.repository;

import me.leeyeeun.bookhub.wish.entity.Wish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface WishRepository extends JpaRepository<Wish, Long> {
    Optional<Wish> findById(Long id);
    List<Wish> findByBooknameContainingIgnoreCase(String keyword); // Containing: LIKE %keyword%
    List<Wish> findAllByMemberId(Long memberId);
    Optional<Wish> findByIdAndMemberId(Long id, Long memberId);
    List<Wish> findAllByMemberIdAndCreatedAtBetween(Long memberId, LocalDateTime start, LocalDateTime end);
}
