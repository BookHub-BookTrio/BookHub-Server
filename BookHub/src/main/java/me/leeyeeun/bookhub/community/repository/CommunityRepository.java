package me.leeyeeun.bookhub.community.repository;

import me.leeyeeun.bookhub.community.entity.Community;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommunityRepository extends JpaRepository<Community, Long> {
    Optional<Community> findById(Long id);
    List<Community> findAllByMemberId(Long memberId);
}
