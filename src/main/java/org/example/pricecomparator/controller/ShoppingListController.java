package org.example.pricecomparator.controller;

import org.example.pricecomparator.model.ShoppingList;
import org.example.pricecomparator.model.ShoppingListItem;
import org.example.pricecomparator.service.ShoppingListService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shopping-lists")
public class ShoppingListController {

    private final ShoppingListService shoppingListService;

    public ShoppingListController(ShoppingListService shoppingListService) {
        this.shoppingListService = shoppingListService;
    }

    // Get all shopping lists
    @GetMapping
    public ResponseEntity<?> getAllLists() {
        List<ShoppingList> lists = shoppingListService.findAllLists();
        if (lists.isEmpty()) {
            return ResponseEntity.ok("📝 No shopping lists available.");
        }
        return ResponseEntity.ok(lists);
    }

    // Get one list by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getListById(@PathVariable String id) {
        return shoppingListService.findListById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404).body("📭 List not found: " + id));
    }

    // Get all items in a list
    @GetMapping("/{id}/items")
    public ResponseEntity<?> getItemsInList(@PathVariable String id) {
        List<ShoppingListItem> items = shoppingListService.getItemsInList(id);
        if (items.isEmpty()) {
            return ResponseEntity.ok("🛒 No items in list: " + id);
        }
        return ResponseEntity.ok(items);
    }

    // Create a new list
    @PostMapping("/create")
    public ResponseEntity<?> createList(@RequestParam String listId, @RequestParam String name) {
        ShoppingList list = shoppingListService.createList(listId, name);
        return ResponseEntity.ok(list);
    }

    // Add a product to a list
    @PostMapping("/{id}/add")
    public ResponseEntity<?> addProductToList(@PathVariable String id,
                                              @RequestParam Long storeProductId,
                                              @RequestParam(defaultValue = "1") int quantity) {
        if (quantity <= 0) {
            return ResponseEntity.badRequest().body("❗ Quantity must be a positive number.");
        }

        try {
            shoppingListService.addProductToList(id, storeProductId, quantity);
            return ResponseEntity.ok("✅ Product added to list: " + id);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("❌ " + e.getMessage());
        }
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<?> deleteItem(@PathVariable Long itemId) {
        try {
            shoppingListService.removeItemById(itemId);
            return ResponseEntity.ok("🗑️ Item with ID " + itemId + " removed.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("❌ " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}/clear")
    public ResponseEntity<?> clearList(@PathVariable String id) {
        try {
            shoppingListService.clearList(id);
            return ResponseEntity.ok("🧹 Shopping list '" + id + "' has been cleared.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("❌ " + e.getMessage());
        }
    }
}
