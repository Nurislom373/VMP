package org.khasanof.service.dto.product;

import lombok.*;

import java.math.BigDecimal;

/**
 * @author Nurislom
 * @see org.khasanof.service.dto.product
 * @since 6/8/2024 11:47 AM
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OptionVariantCreationDTO {

    private String name;
    private Long stock;
    private BigDecimal price;
}
