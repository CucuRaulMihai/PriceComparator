package org.example.pricecomparator.parser;

import org.example.pricecomparator.model.AbstractProduct;
import org.example.pricecomparator.model.StoreProduct;
import org.example.pricecomparator.repository.AbstractProductRepository;
import org.example.pricecomparator.repository.StoreProductRepository;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;

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

                // Create or get AbstractProduct
                AbstractProduct abstractProduct = abstractProductRepository
                        .findById(productId)
                        .orElseGet(() -> abstractProductRepository.save(
                                new AbstractProduct(productId, name, category)
                        ));

                // Create StoreProduct (store-specific)
                StoreProduct storeProduct = new StoreProduct();
                storeProduct.setAbstractProduct(abstractProduct);
                storeProduct.setProductBrand(fields[3]);
                storeProduct.setPackageQuantity(Double.parseDouble(fields[4]));
                storeProduct.setPackageUnit(fields[5]);
                storeProduct.setProductPrice(Double.parseDouble(fields[6]));
                storeProduct.setProductCurrency(fields[7]);
                storeProduct.setStoreName(storeName);

                storeProductRepository.save(storeProduct);
            }

            System.out.println("Parsed and saved standard products from: " + filePath.getFileName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String extractStoreNameFromFileName(String filename) {
        return filename.split("_")[0].toUpperCase(); //kaufland_2025-05-20.csv = "KAUFLAND"
    }
}

