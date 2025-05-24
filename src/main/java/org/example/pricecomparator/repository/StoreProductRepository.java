package org.example.pricecomparator.repository;

import org.example.pricecomparator.model.StoreProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreProductRepository extends JpaRepository<StoreProduct, Long> {

    List<StoreProduct> findByAbstractProductId(String abstractProductId);

    List<StoreProduct> findByStoreNameIgnoreCase(String storeName);
}
