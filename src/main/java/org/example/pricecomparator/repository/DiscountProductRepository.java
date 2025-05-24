package org.example.pricecomparator.repository;

import org.example.pricecomparator.model.DiscountProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface DiscountProductRepository extends JpaRepository<DiscountProduct, String> {

    List<DiscountProduct> findByFromDateAfter(LocalDate date);

    List<DiscountProduct> findTopByOrderByDiscountDesc();

    List<DiscountProduct> findByProductProductId(String productId);
}
