package com.example.falava.Repository;

import com.example.falava.Entity.User.ProductCatagory;
import com.example.falava.Entity.User.Product_items;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product_items,Long> {
    Product_items findById(long id);
    Product_items findByProductname(String productname);
    Page<Product_items> findAllByProductCatagory(Pageable paging, ProductCatagory productCatagory);


    List<Product_items> findAllByProductCatagory(ProductCatagory productCatagory);
}
