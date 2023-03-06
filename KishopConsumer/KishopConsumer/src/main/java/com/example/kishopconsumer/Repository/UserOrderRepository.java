package com.example.kishopconsumer.Repository;

import com.example.kishopconsumer.Entity.UserProductBuyingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserOrderRepository extends JpaRepository<UserProductBuyingEntity,Long> {

    UserProductBuyingEntity findByProductname(String productName);
}
