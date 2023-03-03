package com.example.board.service;

import com.example.board.domain.Role;
import com.example.board.domain.User;
import com.example.board.repository.RoleRepository;
import com.example.board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

// 트랜잭션 단위로 실행될 메소드를 선언하고 있는 클래스
@Service
@RequiredArgsConstructor // lombok이 final 필드를 초기화하는 생성자를 자동으로 생성한다.
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    // 보통 서비스에서는 @Transactional을 붙여서 하나의 트랜잭셔으로 처리하게 한다.
    @Transactional
    public User addUser(String name, String email, String password) {
        // 트랜잭션이 시작된다.
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            throw new RuntimeException("이미 가입된 이메일입니다.");
        }

        Role role = roleRepository.findByName("ROLE_USER").get();
        User user = new User();
        user.setPassword(password);
        user.setRoles(Set.of(role));
        user.setEmail(email);
        user.setName(name);

        user = userRepository.save(user);

        return user;
        // 트랜잭션이 끝난다.
    }

    @Transactional
    public User getUser(String email) {
        return userRepository.findByEmail(email).orElseThrow();
    }

    @Transactional(readOnly = true)
    public List<String> getRoles(int userId) {
        Set<Role> roles = userRepository.findById(userId).orElseThrow().getRoles();
        List<String> list = new ArrayList<>();
        for (Role role: roles) {
            list.add(role.getName() );
        }
        return list;
    }
}
