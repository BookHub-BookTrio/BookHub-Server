package me.leeyeeun.bookhub.wish.entity;

import jakarta.persistence.*;
import lombok.*;
import me.leeyeeun.bookhub.member.entity.Member;
import me.leeyeeun.bookhub.wish.controller.dto.request.WishRequestDto;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "wish")
public class Wish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "bookname", nullable = false)
    private String bookname;

    @Column(name = "author")
    private String author;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "progress", nullable = false)
    @Enumerated(EnumType.STRING)
    private Progress progress;

    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(name = "star")
    @Enumerated(EnumType.STRING)
    private Star star;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public void update(WishRequestDto wishRequestDto) { // 부분 수정 가능
        if (wishRequestDto.bookname() != null) this.bookname = wishRequestDto.bookname();
        if (wishRequestDto.author() != null) this.author = wishRequestDto.author();
        if (wishRequestDto.content() != null) this.content = wishRequestDto.content();
        if (wishRequestDto.progress() != null) this.progress = wishRequestDto.progress();
        if (wishRequestDto.category() != null) this.category = wishRequestDto.category();
        if (wishRequestDto.star() != null) this.star = wishRequestDto.star();
    }
}
