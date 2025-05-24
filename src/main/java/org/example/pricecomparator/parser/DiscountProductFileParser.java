package org.example.pricecomparator.parser;

import org.example.pricecomparator.model.AbstractProduct;
import org.example.pricecomparator.model.DiscountProduct;
import org.example.pricecomparator.model.StoreProduct;
import org.example.pricecomparator.repository.AbstractProductRepository;
import org.example.pricecomparator.repository.DiscountProductRepository;
import org.example.pricecomparator.repository.StoreProductRepository;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class DiscountProductFileParser {

    private final AbstractProductRepository abstractProductRepository;
    private final StoreProductRepository storeProductRepository;
    private final DiscountProductRepository discountProductRepository;

    public DiscountProductFileParser(AbstractProductRepository abstractProductRepository,
                                     StoreProductRepository storeProductRepository,
                                     DiscountProductRepository discountProductRepository) {
        this.abstractProductRepository = abstractProductRepository;
        this.storeProductRepository = storeProductRepository;
        this.discountProductRepository = discountProductRepository;
    }

    public void parse(Path filePath) {
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            reader.readLine(); // Skip header

            String storeName = extractStoreNameFromFileName(filePath.getFileName().toString());

            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(";");

                String productId = fields[0];

                Optional<AbstractProduct> abstractProductOpt = abstractProductRepository.findById(productId);
                if (abstractProductOpt.isEmpty()) {
                    System.out.println("Skipping discount: abstract product not found for ID " + productId);
                    continue;
                }

                // Find the matching StoreProduct
                List<StoreProduct> candidates = storeProductRepository.findByAbstractProductId(productId);
                StoreProduct storeProduct = candidates.stream()
                        .filter(p ->
                                p.getStoreName().equalsIgnoreCase(storeName) &&
                                        p.getProductBrand().equalsIgnoreCase(fields[2]) &&
                                        p.getPackageQuantity().equals(Double.parseDouble(fields[3])) &&
                                        p.getPackageUnit().equalsIgnoreCase(fields[4])
                        )
                        .findFirst()
                        .orElse(null);

                if (storeProduct == null) {
                    System.out.println("Skipping discount: no store product match for ID " + productId);
                    continue;
                }

                // Create discount
                DiscountProduct discount = new DiscountProduct();
                discount.setStoreProduct(storeProduct);
                discount.setFromDate(LocalDate.parse(fields[6]));
                discount.setToDate(LocalDate.parse(fields[7]));
                discount.setDiscount((int) Double.parseDouble(fields[8]));

                discountProductRepository.save(discount);
            }

            System.out.println("Parsed and saved discounts from: " + filePath.getFileName());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String extractStoreNameFromFileName(String filename) {
        return filename.split("_")[0].toUpperCase(); // e.g., kaufland_discount2025-05-20.csv
    }
}

