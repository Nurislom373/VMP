package org.khasanof.service.dto.product;

import lombok.*;

import java.util.Set;

/**
 * @author Nurislom
 * @see org.khasanof.service.dto.product
 * @since 6/8/2024 11:48 AM
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OptionCreationDTO {

    private String name;
    private Set<OptionVariantCreationDTO> variants;
}
