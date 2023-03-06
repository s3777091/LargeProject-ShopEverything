package com.example.falava.Repository;

import com.example.falava.Entity.User.ProductCatagory;
import com.example.falava.Entity.User.Product_items;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCatogoryRepository extends JpaRepository<ProductCatagory,Long> {
    ProductCatagory findByProducttype(String type);
}

