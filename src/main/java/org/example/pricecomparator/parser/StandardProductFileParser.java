package org.example.pricecomparator.parser;

import org.example.pricecomparator.model.Product;
import org.example.pricecomparator.repository.ProductRepository;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Component
public class StandardProductFileParser {

    private final ProductRepository productRepository;

    ///Opted for Constructor injection, instead of @Autowired. Better practice.
    public StandardProductFileParser(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void parse(Path filePath){
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            reader.readLine(); // Skip header
            String line;
            while ((line = reader.readLine()) != null) {

                /// Since the fields are separted with ";"
                String[] fields = line.split(";");

                if(fields.length == 8){

                    Product product = new Product(
                            fields[0], // productId
                            fields[1], // productName
                            fields[2], // productCategory
                            fields[3], // productBrand
                            Double.parseDouble(fields[4]), // quantity
                            fields[5], // unit
                            Double.parseDouble(fields[6]), // price
                            fields[7]  // currency
                    );

                    ///Saves us from overwriting products unnecessarily
                    Optional<Product> existing = productRepository.findById(product.getProductId());

                    if (existing.isEmpty() || !existing.get().equals(product)) {
                        productRepository.save(product);
                    }
                }
                else{
                    System.out.println("The product you are trying to add is missing data. Please check your CSV file!");
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace(); // Replace with proper logging later
        }
    }


}
