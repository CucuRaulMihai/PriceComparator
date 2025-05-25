package org.example.pricecomparator.controller;


import org.example.pricecomparator.model.StoreProduct;
import org.example.pricecomparator.service.StoreProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/store_products")
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
}
