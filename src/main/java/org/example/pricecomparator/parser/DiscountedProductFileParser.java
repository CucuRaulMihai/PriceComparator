package org.example.pricecomparator.parser;

import org.example.pricecomparator.model.DiscountProduct;
import org.example.pricecomparator.model.Product;
import org.example.pricecomparator.repository.DiscountProductRepository;
import org.example.pricecomparator.repository.ProductRepository;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Optional;

@Component
public class DiscountedProductFileParser {

    private final ProductRepository productRepository;
    private final DiscountProductRepository discountProductRepository;

    public DiscountedProductFileParser(ProductRepository productRepository,
                                       DiscountProductRepository discountProductRepository) {
        this.productRepository = productRepository;
        this.discountProductRepository = discountProductRepository;
    }

    public void parse(Path filePath) {
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            reader.readLine(); // Skip header
            String line;
            while ((line = reader.readLine()) != null) {

                String[] fields = line.split(";");

                ///checking for valid discounted products. Protects the DB from empty values.
                if (fields.length == 9){

                    String productId = fields[0];

                    /// we can only check for a discount on an already existing product.
                    Optional<Product> productOpt = productRepository.findById(productId);
                    if (productOpt.isEmpty()) {
                        System.out.println("Product not found for ID: " + productId + " — skipping discount");
                        continue;
                    }

                    DiscountProduct discount = new DiscountProduct(
                            productId,
                            productOpt.get(),
                            LocalDate.parse(fields[6]), // from_date
                            LocalDate.parse(fields[7]), // to_date
                            Integer.parseInt(fields[8]) // discount %
                    );

                    if (discountProductRepository.findById(productId).isEmpty()) {
                        discountProductRepository.save(discount);
                        System.out.println("Saved discount for: " + productId);
                    } else {
                        System.out.println("Discount already exists for: " + productId);
                    }
                    System.out.println(discount); // placeholder — later save to DB
                }
                else{
                    System.out.println("The discounted product you are trying to add is incomplete. Please check the CSV file!");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
