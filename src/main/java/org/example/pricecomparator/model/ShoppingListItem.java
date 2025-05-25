package org.example.pricecomparator.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ShoppingListItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private ShoppingList shoppingList;

    @ManyToOne(optional = false)
    private StoreProduct storeProduct;

    @NotNull
    @Min(value = 1, message = "Quantity must be at least 1!")
    private Integer quantity;
}
