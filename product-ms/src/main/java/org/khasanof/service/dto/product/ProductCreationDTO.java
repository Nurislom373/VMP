package org.khasanof.service.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

/**
 * @author Nurislom
 * @see org.khasanof.service.dto.product
 * @since 6/8/2024 11:17 AM
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreationDTO {

    @NotBlank
    @Size(min = 1, max = 255)
    private String name;

    private String description;

    private Set<String> imageKeys;

    private InventoryDTO inventory;
}
