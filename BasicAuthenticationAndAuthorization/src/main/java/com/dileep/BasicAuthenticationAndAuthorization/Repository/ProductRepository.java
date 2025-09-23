package com.dileep.BasicAuthenticationAndAuthorization.Repository;

import com.dileep.BasicAuthenticationAndAuthorization.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {
}
