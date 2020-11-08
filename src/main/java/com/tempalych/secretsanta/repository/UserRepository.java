package com.tempalych.secretsanta.repository;

import com.tempalych.secretsanta.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByName(String name);
    List<User> findAll();
    List<User> findByGroup(String group);
    User findById(long id);
    User findByTelegramId(int telegramId);
}
