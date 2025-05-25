package org.example.pricecomparator.controller;


import org.example.pricecomparator.model.StoreProduct;
import org.example.pricecomparator.service.StoreProductService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/store-products")
public class StoreProductController {

    private final StoreProductService storeProductService;

    public StoreProductController(StoreProductService storeProductService) {
        this.storeProductService = storeProductService;
    }

    // GET all store products
    @GetMapping
    public List<StoreProduct> getAll() {
        return storeProductService.findAll();
    }

    // GET a specific store product by its DB ID
    @GetMapping("/{id}")
    public StoreProduct getById(@PathVariable Long id) {
        return storeProductService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("StoreProduct not found: " + id));
    }

    // GET all store products linked to a certain abstract product
    @GetMapping("/by-abstract/{abstractProductId}")
    public List<StoreProduct> getByAbstractProduct(@PathVariable String abstractProductId) {
        return storeProductService.findByAbstractProductId(abstractProductId);
    }

    // GET all store products from a given store
    @GetMapping("/by-store")
    public List<StoreProduct> getByStoreName(@RequestParam String storeName) {
        return storeProductService.findByStoreName(storeName);
    }

    @GetMapping("/best-value/{abstractProductId}")
    public List<StoreProduct> getBestValue(@PathVariable String abstractProductId) {
        return storeProductService.findBestValuePerUnit(abstractProductId);
    }

    @GetMapping("/price-history")
    public ResponseEntity<?> getPriceHistory(
            @RequestParam(required = false) String store,
            @RequestParam String productName,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {

        LocalDate now = LocalDate.now();
        LocalDate defaultStart = now.minusDays(30);

        LocalDate effectiveStart = (start != null) ? start : defaultStart;
        LocalDate effectiveEnd = (end != null) ? end : now;

        List<StoreProduct> results = storeProductService.getPriceHistoryFlexible(store, productName, effectiveStart, effectiveEnd);

        if (results.isEmpty()) {
            String storeText = (store == null) ? "all stores" : store;
            return ResponseEntity.ok("📉 No price history found for '" + productName + "' at " + storeText +
                    " between " + effectiveStart + " and " + effectiveEnd);
        }

        return ResponseEntity.ok(results);
    }


}
