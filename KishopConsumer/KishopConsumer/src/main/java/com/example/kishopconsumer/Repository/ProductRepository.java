package com.example.kishopconsumer.Repository;

import com.example.kishopconsumer.Entity.ProductCatagory;
import com.example.kishopconsumer.Entity.Product_items;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product_items,Long> {
    Product_items findById(long id);
    Page<Product_items> findAllByProductCatagory(Pageable paging, ProductCatagory productCatagory);
}
