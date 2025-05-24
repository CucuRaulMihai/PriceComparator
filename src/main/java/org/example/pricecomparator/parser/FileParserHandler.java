package org.example.pricecomparator.parser;

import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
public class FileParserHandler {

    private final StandardProductFileParser standardParser;
    private final DiscountProductFileParser discountedParser;


    public FileParserHandler(StandardProductFileParser standardParser, DiscountProductFileParser discountedParser) {
        this.standardParser = standardParser;
        this.discountedParser = discountedParser;
    }

    public void handleFile(Path filePath) {
        String filename = filePath.getFileName().toString().toLowerCase();

        if (filename.contains("_discount")) {
            discountedParser.parse(filePath); // This would return a list later
        } else {
            standardParser.parse(filePath);
        }
    }
}
