package org.example.pricecomparator.service;

import org.example.pricecomparator.model.DiscountProduct;
import org.example.pricecomparator.repository.DiscountProductRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DiscountProductService {

    private final DiscountProductRepository discountProductRepository;

    public DiscountProductService(DiscountProductRepository discountProductRepository) {
        this.discountProductRepository = discountProductRepository;
    }

    public List<DiscountProduct> findAll() {
        return discountProductRepository.findAll();
    }

    public List<DiscountProduct> findRecentDiscounts(int daysBack) {
        LocalDate cutoff = LocalDate.now().minusDays(daysBack);
        return discountProductRepository.findByFromDateAfter(cutoff);
    }

    public List<DiscountProduct> findByStoreProductId(Long storeProductId) {
        return discountProductRepository.findByStoreProductId(storeProductId);
    }

    public List<DiscountProduct> getTopDiscounts(int limit) {
        return discountProductRepository.findTopByOrderByDiscountDesc(PageRequest.of(0, limit));
    }
}
