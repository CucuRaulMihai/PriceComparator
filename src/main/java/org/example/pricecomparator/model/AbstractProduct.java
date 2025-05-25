package org.example.pricecomparator.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "abstract_products")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AbstractProduct {

    @Id
    private String id;

    private String productName;

    private String productCategory;
}
