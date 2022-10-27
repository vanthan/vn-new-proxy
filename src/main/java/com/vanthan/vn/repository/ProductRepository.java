package com.vanthan.vn.repository;

import com.vanthan.vn.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findBySku(String sku);

    Optional<Product> findById(Integer id);

    @Query("select p from Product p where p.name like %?1% ")
    Page<Product> searchProductByName(String name, PageRequest pageRequest);
}
