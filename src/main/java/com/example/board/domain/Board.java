package com.example.board.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="board")
@NoArgsConstructor // 기본생성자가 필요하다.
@Setter
@Getter
public class Board {
    @Id
    @Column(name="board_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY) // userId는 자동으로 생성되도록 한다. 1,2,3,4,...
    private Integer boardId;

    @Column(length = 100)
    private String title;

    @Lob
    private String content;

    private int viewCnt;

    @CreationTimestamp
    private LocalDateTime regdate;

    @ManyToOne(fetch = FetchType.EAGER) // 게시물 N --- 1 사용자 EAGER = 무조건 데이터를 가지고 와라. LAZY = 반대
    @JoinColumn(name = "user_id")
    private User user;

    @Override
    public String toString() {
        return "Board{" +
                "boardId=" + boardId +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", viewCnt=" + viewCnt +
                ", regdate=" + regdate +
                '}';
    }
}