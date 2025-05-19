package me.leeyeeun.bookhub.member.repository;

import me.leeyeeun.bookhub.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByEmail(String email);
    Optional<Member> findById(Long id);
    Optional<Member> findByKakaoId(Long id);
    Optional<Member>findByEmail(String email);
    Optional<Member> findByRefreshToken(String refreshToken);
    boolean existsByNickname(String nickname);
}