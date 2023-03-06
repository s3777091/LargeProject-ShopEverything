package com.example.falava.Repository;

import com.example.falava.Entity.User.UserBuyingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBuyingRepository extends JpaRepository<UserBuyingEntity,Long> {
    UserBuyingEntity findByToken (String token);
    UserBuyingEntity findById(long id);

}
