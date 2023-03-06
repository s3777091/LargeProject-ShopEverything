package com.example.kishopconsumer.Repository;

import com.example.kishopconsumer.Entity.UserBuyingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBuyingRepository extends JpaRepository<UserBuyingEntity,Long> {
    UserBuyingEntity findByToken (String token);
    UserBuyingEntity findById(long id);
}
