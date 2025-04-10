package me.leeyeeun.bookhub.wish.repository;

import me.leeyeeun.bookhub.member.entity.Member;
import me.leeyeeun.bookhub.wish.entity.Wish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WishRepository extends JpaRepository<Wish, Long> {
    Optional<Wish> findById(Long id);
}
