package org.example.pricecomparator.controller;

import org.example.pricecomparator.model.DiscountProduct;
import org.example.pricecomparator.service.DiscountProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/discounts")
public class DiscountProductController {

    private final DiscountProductService discountProductService;

    public DiscountProductController(DiscountProductService discountProductService) {
        this.discountProductService = discountProductService;
    }

    // Get all discounts
    @GetMapping
    public List<DiscountProduct> getAllDiscounts() {
        return discountProductService.findAll();
    }

    // Get discounts added in the last X days
    @GetMapping("/recent")
    public List<DiscountProduct> getRecentDiscounts(@RequestParam(defaultValue = "1") int days) {
        return discountProductService.findRecentDiscounts(days);
    }

    // Get top N discounts by percentage
    @GetMapping("/top")
    public List<DiscountProduct> getTopDiscounts(@RequestParam(defaultValue = "5") int limit) {
        return discountProductService.getTopDiscounts(limit);
    }

    // Get all discounts for a specific store product
    @GetMapping("/store-product/{id}")
    public List<DiscountProduct> getByStoreProduct(@PathVariable Long id) {
        return discountProductService.findByStoreProductId(id);
    }

    // Get all discounts available at a given store
    @GetMapping("/by-store")
    public ResponseEntity<?> getDiscountsByStore(@RequestParam String store) {
        List<DiscountProduct> discounts = discountProductService.findByStoreName(store);

        if (discounts.isEmpty()) {
            String message = "📢 No discounts currently available for store: " + store;
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }

        return ResponseEntity.ok(discounts);
    }
}
