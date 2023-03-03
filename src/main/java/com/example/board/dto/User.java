package com.example.board.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
public class User {
    private int userId;
    private String email;
    private String name;
    private String password;
    private LocalDateTime regdate; // 원래는 날짜 type으로 읽어온 후 문자열로 변환
}
