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
                String productName = fields[1];
                String brand = fields[2];
                Double quantity = Double.parseDouble(fields[3]);
                String unit = fields[4];
                String category = fields[5];
                LocalDate fromDate = LocalDate.parse(fields[6]);
                LocalDate toDate = LocalDate.parse(fields[7]);
                Integer discountPercent = (int) Double.parseDouble(fields[8]);

                // Create or find abstract product
                AbstractProduct abstractProduct = abstractProductRepository.findById(productId)
                        .orElseGet(() -> abstractProductRepository.save(
                                new AbstractProduct(productId, productName, category)));

                // Find corresponding store product
                Optional<StoreProduct> storeProductOpt = storeProductRepository
                        .findByAbstractProductIdAndStoreNameIgnoreCaseAndProductBrandIgnoreCaseAndPackageQuantityAndPackageUnit(
                                productId, storeName, brand, quantity, unit);

                if (storeProductOpt.isEmpty()) {
                    System.out.println("⚠️ No matching StoreProduct found for discount: " + productId);
                    continue;
                }

                StoreProduct storeProduct = storeProductOpt.get();

                // Check for duplicate discount
                boolean alreadyExists = discountProductRepository
                        .findByStoreProductIdAndFromDateAndToDate(
                                storeProduct.getId(), fromDate, toDate)
                        .isPresent();

                if (alreadyExists) {
                    System.out.println("🔁 Duplicate discount skipped: " + productId + " at " + storeName);
                    continue;
                }

                // Save new discount
                DiscountProduct discount = new DiscountProduct();
                discount.setStoreProduct(storeProduct);
                discount.setFromDate(fromDate);
                discount.setToDate(toDate);
                discount.setDiscount(discountPercent);

                discountProductRepository.save(discount);
            }

            System.out.println("✅ Parsed and saved discount products from: " + filePath.getFileName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String extractStoreNameFromFileName(String filename) {
        return filename.split("_")[0].toUpperCase(); // kaufland_discount2025-05-20.csv = kaufland
    }
}

