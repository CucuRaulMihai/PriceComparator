package org.example.pricecomparator.controller;


import org.example.pricecomparator.exception.ResourceNotFoundException;
import org.example.pricecomparator.model.AbstractProduct;
import org.example.pricecomparator.service.AbstractProductService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@RestController
@RequestMapping("/api/abstract_products")
public class AbstractProductController {

    private final AbstractProductService abstractProductService;

    public AbstractProductController(AbstractProductService abstractProductService) {
        this.abstractProductService = abstractProductService;
    }

    @GetMapping
    public List<AbstractProduct> getAll(){
        return abstractProductService.findAll();
    }

    @GetMapping("/{id}")
    public AbstractProduct getById(@PathVariable String id) {

        //returning a 404 if the product ID doesn't exist, not a 500.
        return abstractProductService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AbstractProduct not found: " + id));
    }

    @GetMapping("/search")
    public List<AbstractProduct> searchByName(@RequestParam String name) {
        return abstractProductService.searchByName(name);
    }
}
