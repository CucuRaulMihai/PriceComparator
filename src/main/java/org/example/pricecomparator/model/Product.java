package org.example.pricecomparator.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    private String productId;

    @NotBlank
    private String productName;

    @NotBlank
    private String productCategory;

    @NotBlank
    private String productBrand;

    @NotNull
    @DecimalMin("0.1")
    private Double productPackageQuantity;

    @NotBlank
    private String productPackageUnit;

    @NotNull
    @DecimalMin(value = "0.1", message = "The price must be greater or equal to 0.1 RON!")
    private Double productPrice;

    @NotBlank
    private String productCurrency;

}
