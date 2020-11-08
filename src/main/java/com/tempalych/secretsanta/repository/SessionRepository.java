package com.tempalych.secretsanta.repository;

import com.tempalych.secretsanta.domain.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    Session getSessionByUser(long userId);
    List<Session> findAll();
}
