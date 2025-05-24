package org.example.pricecomparator.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "discount_products")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DiscountProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private StoreProduct storeProduct;

    @NotNull
    private LocalDate fromDate;

    @NotNull
    private LocalDate toDate;

    @NotNull
    @Min(1)
    private Integer discount; // in percent
}
