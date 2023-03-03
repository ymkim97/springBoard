package com.example.board.repository;

import com.example.board.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

// Spring Data JPA Repository를 완성
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    Optional<User> findByName(String name); // Query Method

    Optional<User> findByNameAndEmail(String name, String email);

    List<User> findByNameOrEmail(String name, String email);

    List<User> findByUserIdBetween(int startUserId, int endUserId);

    List<User> findByNameLike(String name);
}
