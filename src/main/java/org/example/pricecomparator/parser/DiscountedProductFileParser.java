package org.example.pricecomparator.parser;

import org.example.pricecomparator.dto.DiscountedProductDTO;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

@Component
public class DiscountedProductFileParser {

    public void parse(Path filePath) {
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            reader.readLine(); // Skip header
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(";");
                DiscountedProductDTO dto = new DiscountedProductDTO(
                        fields[0], fields[1], fields[2],
                        Double.parseDouble(fields[3]), fields[4], fields[5],
                        LocalDate.parse(fields[6]), LocalDate.parse(fields[7]),
                        Double.parseDouble(fields[8])
                );
                System.out.println(dto); // placeholder — later save to DB
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
