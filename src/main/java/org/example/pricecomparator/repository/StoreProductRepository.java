package org.example.pricecomparator.repository;

import org.example.pricecomparator.model.StoreProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreProductRepository extends JpaRepository<StoreProduct, Long> {

    List<StoreProduct> findByAbstractProductId(String abstractProductId);

    List<StoreProduct> findByStoreNameIgnoreCase(String storeName);

    //this is ugly, I wholeheartedly agree.
    //Basically, to avoid any duplications in the database, but also allow new discounts, we check if every field
    //is present in the DB, if it is, we do not add it again. If the product in identical but in a new store, we add it.

    Optional<StoreProduct> findByAbstractProductIdAndStoreNameIgnoreCaseAndProductBrandIgnoreCaseAndPackageQuantityAndPackageUnit(
            String abstractProductId,
            String storeName,
            String productBrand,
            Double packageQuantity,
            String packageUnit
    );
}
