package org.example.pricecomparator.service;

import org.example.pricecomparator.model.PriceAlert;
import org.example.pricecomparator.model.StoreProduct;
import org.example.pricecomparator.repository.PriceAlertRepository;
import org.example.pricecomparator.repository.StoreProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PriceAlertService {

    private final PriceAlertRepository priceAlertRepository;
    private final StoreProductRepository storeProductRepository;

    public PriceAlertService(PriceAlertRepository priceAlertRepository,
                             StoreProductRepository storeProductRepository) {
        this.priceAlertRepository = priceAlertRepository;
        this.storeProductRepository = storeProductRepository;
    }

    public List<PriceAlert> findAll() {
        return priceAlertRepository.findAll();
    }

    public Optional<PriceAlert> findById(Long id) {
        return priceAlertRepository.findById(id);
    }

    public List<PriceAlert> findByStoreProduct(Long storeProductId) {
        return priceAlertRepository.findByStoreProductId(storeProductId);
    }

    public PriceAlert create(Long storeProductId, double targetPrice) {
        StoreProduct storeProduct = storeProductRepository.findById(storeProductId)
                .orElseThrow(() -> new IllegalArgumentException("StoreProduct not found: " + storeProductId));

        PriceAlert alert = new PriceAlert();
        alert.setStoreProduct(storeProduct);
        alert.setTargetPrice(targetPrice);

        return priceAlertRepository.save(alert);
    }

    public List<PriceAlert> getMatchedAlerts() {
        return priceAlertRepository.findAll().stream()
                .filter(alert -> alert.getStoreProduct().getProductPrice() <= alert.getTargetPrice())
                .toList();
    }
}
