package com.example.board.repository;

import com.example.board.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Integer> {

    // JPQL 사용할 수 있다.
    // JPQL 은 SQL과 모양이 비슷하다.
    // JPQL 은 객체지향 언어이다.
    @Query(value = "select b from Board b join fetch b.user")
    List<Board> getBoards();

    // 페이징 처리로 가지고 오는데 날짜를 desc
    Page<Board> findByOrderByRegdateDesc(Pageable pageable);

    @Query(value = "select count(b) from Board b")
    Long getBoardCount();

    @Query(value = "select b, u from Board b inner join b.user u inner join u.roles r where r.name = :roleName")
    List<Board> getBoards(@Param("roleName") String roleName);
}
