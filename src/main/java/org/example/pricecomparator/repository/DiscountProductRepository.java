package org.example.pricecomparator.repository;

import org.example.pricecomparator.model.DiscountProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface DiscountProductRepository extends JpaRepository<DiscountProduct, Long> {

    List<DiscountProduct> findByFromDateAfter(LocalDate fromDate);

    List<DiscountProduct> findByStoreProductId(Long storeProductId);

    List<DiscountProduct> findAllByOrderByDiscountDesc(Pageable pageable);

    Optional<DiscountProduct> findByStoreProductIdAndFromDateAndToDate(Long storeProductId, LocalDate fromDate, LocalDate toDate);

    List<DiscountProduct> findByStoreProductStoreNameIgnoreCase(String storeName);

}
