package org.example.pricecomparator.parser;

import org.example.pricecomparator.model.AbstractProduct;
import org.example.pricecomparator.model.StoreProduct;
import org.example.pricecomparator.repository.AbstractProductRepository;
import org.example.pricecomparator.repository.StoreProductRepository;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Component
public class StandardProductFileParser {

    private final AbstractProductRepository abstractProductRepository;
    private final StoreProductRepository storeProductRepository;

    public StandardProductFileParser(AbstractProductRepository abstractProductRepository,
                                     StoreProductRepository storeProductRepository) {
        this.abstractProductRepository = abstractProductRepository;
        this.storeProductRepository = storeProductRepository;
    }

    public void parse(Path filePath) {
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            reader.readLine(); // Skip header

            String storeName = extractStoreNameFromFileName(filePath.getFileName().toString());

            String line;
            while ((line = reader.readLine()) != null) {

                String[] fields = line.split(";");

                String productId = fields[0];
                String name = fields[1];
                String category = fields[2];
                String brand = fields[3];
                Double quantity = Double.parseDouble(fields[4]);
                String unit = fields[5];
                Double price = Double.parseDouble(fields[6]);
                String currency = fields[7];

                // Get or create AbstractProduct
                AbstractProduct abstractProduct = abstractProductRepository
                        .findById(productId)
                        .orElseGet(() -> abstractProductRepository.save(
                                new AbstractProduct(productId, name, category)
                        ));

                // Check if this store product already exists
                Optional<StoreProduct> existing = storeProductRepository
                        .findByAbstractProductIdAndStoreNameIgnoreCaseAndProductBrandIgnoreCaseAndPackageQuantityAndPackageUnit(
                                abstractProduct.getId(), storeName, brand, quantity, unit);

                if (existing.isPresent()) {
                    System.out.println("🔁 Skipping duplicate product: " + productId + " at " + storeName);
                    continue;
                }

                // Save new store-specific product
                StoreProduct storeProduct = new StoreProduct();
                storeProduct.setAbstractProduct(abstractProduct);
                storeProduct.setStoreName(storeName);
                storeProduct.setProductBrand(brand);
                storeProduct.setPackageQuantity(quantity);
                storeProduct.setPackageUnit(unit);
                storeProduct.setProductPrice(price);
                storeProduct.setProductCurrency(currency);

                storeProductRepository.save(storeProduct);
            }

            System.out.println("✅ Parsed and saved standard products from: " + filePath.getFileName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String extractStoreNameFromFileName(String filename) {
        return filename.split("_")[0].toUpperCase(); //kaufland_2025-05-20.csv = "KAUFLAND"
    }
}

