package org.example.pricecomparator.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "store_products")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StoreProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private AbstractProduct abstractProduct;

    @NotBlank
    private String productBrand;

    @NotNull
    private Double packageQuantity;

    @NotBlank
    private String packageUnit;

    @NotNull
    @DecimalMin(value = "0.1", message = "The price must be greater than 0 RON!")
    private Double productPrice;

    @NotBlank
    private String productCurrency;

    @NotBlank
    private String storeName;

}
