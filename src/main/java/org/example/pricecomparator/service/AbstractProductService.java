package org.example.pricecomparator.service;

import org.example.pricecomparator.model.AbstractProduct;
import org.example.pricecomparator.repository.AbstractProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AbstractProductService {

    private final AbstractProductRepository abstractProductRepository;

    public AbstractProductService(AbstractProductRepository abstractProductRepository) {
        this.abstractProductRepository = abstractProductRepository;
    }

    public List<AbstractProduct> findAll() {
        return abstractProductRepository.findAll();
    }

    public Optional<AbstractProduct> findById(String id) {
        return abstractProductRepository.findById(id);
    }

    public List<AbstractProduct> searchByName(String name) {
        return abstractProductRepository.findByProductNameContainingIgnoreCase(name);
    }

    public AbstractProduct save(AbstractProduct product) {
        return abstractProductRepository.save(product);
    }
}
