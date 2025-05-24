package org.example.pricecomparator.repository;

import org.example.pricecomparator.model.DiscountProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface DiscountProductRepository extends JpaRepository<DiscountProduct, Long> {

    List<DiscountProduct> findByFromDateAfter(LocalDate fromDate);

    List<DiscountProduct> findByStoreProductId(Long storeProductId);

    List<DiscountProduct> findTopByOrderByDiscountDesc(Pageable pageable);
}
