package me.leeyeeun.bookhub.bookmark.entity;

import jakarta.persistence.*;
import lombok.*;
import me.leeyeeun.bookhub.community.entity.Community;
import me.leeyeeun.bookhub.member.entity.Member;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "bookmark")
public class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_id")
    private Community community;

    @Builder
    public Bookmark(Member member, Community community) {
        this.member = member;
        this.community = community;
    }
}
