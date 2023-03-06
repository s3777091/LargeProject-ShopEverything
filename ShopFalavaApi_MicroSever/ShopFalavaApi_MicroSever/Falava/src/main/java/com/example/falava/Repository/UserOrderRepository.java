package com.example.falava.Repository;

import com.example.falava.Entity.User.UserBuyingEntity;
import com.example.falava.Entity.User.UserProductBuyingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserOrderRepository extends JpaRepository<UserProductBuyingEntity,Long> {

    List<UserProductBuyingEntity> findByUserBuyingEntity(UserBuyingEntity userBuyingEntity);

    UserProductBuyingEntity findByProductname(String name);
}
