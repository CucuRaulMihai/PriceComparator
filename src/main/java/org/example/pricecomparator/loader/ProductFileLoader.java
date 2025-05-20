package org.example.pricecomparator.loader;

import org.example.pricecomparator.parser.FileParserHandler;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class ProductFileLoader implements ApplicationRunner {

    private final FileParserHandler fileParserHandler;

    public ProductFileLoader(FileParserHandler fileParserHandler) {
        this.fileParserHandler = fileParserHandler;
    }

    @Override
    public void run(ApplicationArguments args) {
        Path directory = Path.of("src/main/resources/test-data");

        try {
            Files.list(directory)
                    .filter(Files::isRegularFile)
                    .forEach(fileParserHandler::handleFile);
        } catch (IOException e) {
            System.err.println("Error reading files in directory: " + directory);
            e.printStackTrace();
        }

    }
}
