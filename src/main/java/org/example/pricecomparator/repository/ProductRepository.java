package org.example.pricecomparator.repository;

import org.example.pricecomparator.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    List<Product> findByProductNameContainingIgnoreCase(String name);

    List<Product> findByProductCategoryIgnoreCase(String category);

    List<Product> findByProductBrandIgnoreCase(String brand);

    List<Product> findByProductIdIn(List<String> productIds);
}
