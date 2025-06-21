package me.leeyeeun.bookhub.community.entity;

import jakarta.persistence.*;
import lombok.*;
import me.leeyeeun.bookhub.community.controller.dto.request.CommunityRequestDto;
import me.leeyeeun.bookhub.member.entity.Member;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Table(name = "community")
public class Community {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "createdat", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @PrePersist
    public void setCreatedAt() {
        this.createdAt = LocalDateTime.now();
    }

    public void update(CommunityRequestDto commnuityRequestDto) { // 부분 수정 가능
        if (commnuityRequestDto.title() != null) this.title = commnuityRequestDto.title();
        if (commnuityRequestDto.content() != null) this.content = commnuityRequestDto.content();
    }
}