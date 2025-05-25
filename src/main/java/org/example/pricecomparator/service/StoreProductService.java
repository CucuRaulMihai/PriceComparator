package org.example.pricecomparator.service;


import org.example.pricecomparator.model.StoreProduct;
import org.example.pricecomparator.repository.DiscountProductRepository;
import org.example.pricecomparator.repository.StoreProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StoreProductService {

    private final StoreProductRepository storeProductRepository;

    public StoreProductService(StoreProductRepository storeProductRepository) {
        this.storeProductRepository = storeProductRepository;
    }

    public List<StoreProduct> findAll() {
        return storeProductRepository.findAll();
    }

    public Optional<StoreProduct> findById(Long id) {
        return storeProductRepository.findById(id);
    }

    public List<StoreProduct> findByAbstractProductId(String abstractProductId) {
        return storeProductRepository.findByAbstractProductId(abstractProductId);
    }

    public List<StoreProduct> findByStoreName(String storeName) {
        return storeProductRepository.findByStoreNameIgnoreCase(storeName);
    }

    public List<StoreProduct> findBestValuePerUnit(String abstractProductId) {
        return storeProductRepository.findByAbstractProductId(abstractProductId).stream()
                .sorted((p1, p2) -> Double.compare(
                        p1.getProductPrice() / p1.getPackageQuantity(),
                        p2.getProductPrice() / p2.getPackageQuantity()
                ))
                .toList();
    }
}
