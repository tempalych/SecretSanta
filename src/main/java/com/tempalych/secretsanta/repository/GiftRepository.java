package com.tempalych.secretsanta.repository;

import com.tempalych.secretsanta.domain.Gift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GiftRepository extends JpaRepository<Gift, Long> {
    List<Gift> findAll();
    Gift getByGiver(long giver);
    Gift getByReceiver(long receiver);
}
