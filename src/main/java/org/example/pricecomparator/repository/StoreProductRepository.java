package org.example.pricecomparator.repository;

import org.example.pricecomparator.model.StoreProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
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

    //Even uglier. Not good.
    //This is to ensure that a product is stored if it has a new price or a new date.
    Optional<StoreProduct> findByAbstractProductIdAndStoreNameIgnoreCaseAndProductBrandIgnoreCaseAndPackageQuantityAndPackageUnitAndProductPriceAndDateAdded(
            String abstractProductId,
            String storeName,
            String productBrand,
            Double packageQuantity,
            String packageUnit,
            Double productPrice,
            LocalDate dateAdded
    );

    //Jesus H. Christ...
    //used for the discount parser to correctly add all the different discounts.
    Optional<StoreProduct> findByAbstractProductIdAndStoreNameIgnoreCaseAndProductBrandIgnoreCaseAndPackageQuantityAndPackageUnitAndDateAdded(
            String abstractProductId,
            String storeName,
            String productBrand,
            Double packageQuantity,
            String packageUnit,
            LocalDate dateAdded
    );

    @Query("""
    SELECT sp FROM StoreProduct sp
    WHERE (:store IS NULL OR UPPER(sp.storeName) = UPPER(:store))
      AND UPPER(sp.abstractProduct.productName) LIKE UPPER(CONCAT('%', :productName, '%'))
      AND sp.dateAdded BETWEEN :startDate AND :endDate
    ORDER BY sp.dateAdded
""")
    List<StoreProduct> findPriceHistoryFlexible(
            @Param("store") String store,
            @Param("productName") String productName,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );



}
