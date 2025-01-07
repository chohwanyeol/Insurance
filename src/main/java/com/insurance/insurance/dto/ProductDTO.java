package com.insurance.insurance.dto;

import com.insurance.insurance.entity.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ProductDTO {
    private String title;
    private String description;
    public void EntityToDTO(Product product){
        this.title = product.getTitle();
        this.description = product.getDescription();
    }
}
