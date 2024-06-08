package org.khasanof.service.dto.product;

import lombok.*;
import org.khasanof.domain.enumeration.StockType;

import java.math.BigDecimal;
import java.util.Set;

/**
 * @author Nurislom
 * @see org.khasanof.service.dto.product
 * @since 6/8/2024 11:29 AM
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class InventoryDTO {

    private Boolean onSale;
    private BigDecimal price;
    private BigDecimal salePrice;

    private Long stock;
    private StockType stockType;

    /**
     * Product Option
     */
    private Set<OptionCreationDTO> options;
}
