package com.example.kishopconsumer.Repository;

import com.example.kishopconsumer.Entity.ProductCatagory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCatogoryRepository extends JpaRepository<ProductCatagory,Long> {
    ProductCatagory findByProducttype(String type);
}

