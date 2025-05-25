package org.example.pricecomparator.service;


import org.example.pricecomparator.model.ShoppingList;
import org.example.pricecomparator.model.ShoppingListItem;
import org.example.pricecomparator.model.StoreProduct;
import org.example.pricecomparator.repository.ShoppingListItemRepository;
import org.example.pricecomparator.repository.ShoppingListRepository;
import org.example.pricecomparator.repository.StoreProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShoppingListService {

    private final ShoppingListRepository shoppingListRepository;
    private final ShoppingListItemRepository shoppingListItemRepository;
    private final StoreProductRepository storeProductRepository;

    public ShoppingListService(ShoppingListRepository shoppingListRepository,
                               ShoppingListItemRepository shoppingListItemRepository,
                               StoreProductRepository storeProductRepository) {
        this.shoppingListRepository = shoppingListRepository;
        this.shoppingListItemRepository = shoppingListItemRepository;
        this.storeProductRepository = storeProductRepository;
    }

    public List<ShoppingList> findAllLists() {
        return shoppingListRepository.findAll();
    }

    public Optional<ShoppingList> findListById(String listId) {
        return shoppingListRepository.findById(listId);
    }

    public ShoppingList createList(String listId, String name) {
        ShoppingList list = new ShoppingList(listId, name);
        return shoppingListRepository.save(list);
    }

    public void addProductToList(String listId, Long storeProductId, int quantity) {
        ShoppingList list = shoppingListRepository.findById(listId)
                .orElseThrow(() -> new IllegalArgumentException("Shopping list not found: " + listId));
        StoreProduct storeProduct = storeProductRepository.findById(storeProductId)
                .orElseThrow(() -> new IllegalArgumentException("StoreProduct not found: " + storeProductId));

        ShoppingListItem item = new ShoppingListItem();
        item.setShoppingList(list);
        item.setStoreProduct(storeProduct);
        item.setQuantity(quantity);

        shoppingListItemRepository.save(item);
    }

    public List<ShoppingListItem> getItemsInList(String listId) {
        return shoppingListItemRepository.findByShoppingListId(listId);
    }
}
