package org.example.pricecomparator.repository;

import org.example.pricecomparator.model.AbstractProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AbstractProductRepository extends JpaRepository<AbstractProduct, String> {

    List<AbstractProduct> findByProductNameContainingIgnoreCase(String name);
}
