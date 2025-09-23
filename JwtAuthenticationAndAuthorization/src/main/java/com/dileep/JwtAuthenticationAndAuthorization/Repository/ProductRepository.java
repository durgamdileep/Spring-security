package com.dileep.JwtAuthenticationAndAuthorization.Repository;

import com.dileep.JwtAuthenticationAndAuthorization.Entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {
    Page<Product> findAll(Pageable pageable);
}
