package org.example.pricecomparator.parser;

import org.example.pricecomparator.model.Product;
import org.example.pricecomparator.repository.ProductRepository;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class StandardProductFileParser {

//    private final ProductRepository productRepository;
//
//    ///Opted for Constructor injection, instead of @Autowired. Better practice.
//    public StandardProductFileParser(ProductRepository productRepository) {
//        this.productRepository = productRepository;
//    }

    public void parse(Path filePath){
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            reader.readLine(); // Skip header
            String line;
            while ((line = reader.readLine()) != null) {

                /// Since the fields are separted with ";"
                String[] fields = line.split(";");
                Product product = new Product(
                        fields[0], fields[1], fields[2], fields[3],
                        Double.parseDouble(fields[4]), fields[5],
                        Double.parseDouble(fields[6]), fields[7]
                );
                System.out.println(product);
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace(); // Replace with proper logging later
        }
    }


}
