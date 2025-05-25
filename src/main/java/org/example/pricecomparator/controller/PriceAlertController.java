package org.example.pricecomparator.controller;

import org.example.pricecomparator.model.PriceAlert;
import org.example.pricecomparator.service.PriceAlertService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
public class PriceAlertController {

    private final PriceAlertService priceAlertService;

    public PriceAlertController(PriceAlertService priceAlertService) {
        this.priceAlertService = priceAlertService;
    }

    // Get all alerts
    @GetMapping
    public ResponseEntity<?> getAllAlerts() {
        List<PriceAlert> alerts = priceAlertService.findAll();

        if (alerts.isEmpty()) {
            return ResponseEntity.ok("📭 You have no alerts as of now.");
        }

        return ResponseEntity.ok(alerts);
    }

    // Get alerts triggered by current price
    @GetMapping("/matched")
    public List<PriceAlert> getMatchedAlerts() {
        return priceAlertService.getMatchedAlerts();
    }

    // Get alerts for a specific store product
    @GetMapping("/store-product/{id}")
    public List<PriceAlert> getAlertsForStoreProduct(@PathVariable Long id) {
        return priceAlertService.findByStoreProduct(id);
    }

    // Create a new alert
    @PostMapping("/create")
    public ResponseEntity<?> createAlert(@RequestParam Long storeProductId,
                                         @RequestParam Double targetPrice) {
        if (targetPrice <= 0) {
            return ResponseEntity.badRequest().body("❗ Target price must be greater than 0.");
        }

        PriceAlert created = priceAlertService.create(storeProductId, targetPrice);
        return ResponseEntity.ok(created);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAlert(@PathVariable Long id) {
        priceAlertService.deleteById(id);
        return ResponseEntity.ok("✅ PriceAlert with ID " + id + " has been deleted.");
    }
}